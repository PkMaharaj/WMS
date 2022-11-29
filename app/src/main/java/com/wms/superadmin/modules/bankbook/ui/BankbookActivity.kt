package com.wms.superadmin.modules.bankbook.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.network.models.pojos.BranchListItem
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.appcomponents.views.DatePickerFragment
import com.wms.superadmin.databinding.ActivityBankbookBinding
import com.wms.superadmin.extensions.*
import com.wms.superadmin.modules.bankbook.`data`.model.Bankbook1RowModel
import com.wms.superadmin.modules.bankbook.data.model.BankbookModel
import com.wms.superadmin.modules.bankbook.`data`.viewmodel.BankbookVM
import com.wms.superadmin.modules.bankwisebook.ui.BankwisebookActivity
import com.wms.superadmin.modules.vocherbankbook.data.model.Vocherbankbook1RowModel
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.bankbook.BankBookRequest
import com.wms.superadmin.network.models.bankbook.BankBookResponse
import com.wms.superadmin.network.models.bankbook.GroupItem
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import java.lang.Exception
import kotlin.Int
import kotlin.String
import kotlin.Unit
import org.json.JSONObject
import org.koin.android.ext.android.inject
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

public class BankbookActivity : BaseActivity<ActivityBankbookBinding>(R.layout.activity_bankbook), AdapterView.OnItemSelectedListener {
  private val viewModel: BankbookVM by viewModels<BankbookVM>()
  private val prefs: PreferenceHelper by inject()
  val mcurrentTime = Calendar.getInstance()
  var superAdminDetails=prefs.getSADetails<LoginResponse>()
  val year = mcurrentTime.get(Calendar.YEAR)
  val month = mcurrentTime.get(Calendar.MONTH)+1
  val day = mcurrentTime.get(Calendar.DAY_OF_WEEK)-1
  var user=prefs.getSADetails<LoginResponse>()!!
  private var fromDate= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
  private var toDate= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
  var screenName="Branch"
  private var specificbranch: BranchListItem?=null
  var spinnerBranchList = arrayListOf<BranchListItem>()
  var reportType=prefs.getBookType()
  var branch:String="0"
  private lateinit var bankbookModel:BankbookModel

  public override fun onInitialized(): Unit {
    bankbookModel=viewModel.bankbookModel.value!!
    bankbookModel.Fromdate=fromDate
    bankbookModel.Todate=toDate
    viewModel.navArguments = intent.extras
    var periodPosition=intent.getIntExtra(IntentParams.PERIOD,0)
    val timePeriodAdapter = ArrayAdapter(this,R.layout.spinner_item, getTimePeriodList().map { it.PeriodName })
    binding.periodSpinner.adapter=timePeriodAdapter
    binding.periodSpinner.setSelection(periodPosition)
    binding.txtBankBook.text=reportType

    if(reportType.equals("CashBook")){
      binding.txttotalCash.text="Cash In Hand :"
      binding.txtBankBook.text="Cash Book"
    }
    else if(reportType.equals("BankBook")){
      binding.txttotalCash.text="Cash In Bank :"
      binding.txtBankBook.text="Bank Book"
    }
    else{
      binding.txttotalCash.text=" Total Closing Bal:"
    }


    val branchList= prefs.getBranchlist<ArrayList<BranchListItem>>()!!
    if (!user.branchID.isNullOrEmpty()) {
      branch = user.branchID?:"0"
      specificbranch = branchList.find { it.branchID == user.branchID }
      spinnerBranchList.add(specificbranch!!)

    }
    else {
      spinnerBranchList=branchList
    }
    val branchlistAdapter = ArrayAdapter(this, R.layout.spinner_item, spinnerBranchList.map {it.branchName })
    branch=intent.getStringExtra(IntentParams.BRANCH_ID)?:"0"
    val branch=spinnerBranchList.find { it.branchID==branch }
    val position=spinnerBranchList.indexOf(branch)
    binding.branchlistspinner.adapter=branchlistAdapter
    binding.branchlistspinner.setSelection(position)
    binding.branchlistspinner.onItemSelectedListener=this
    binding.periodSpinner.onItemSelectedListener=this

    val recyclerBankbooklistAdapter = RecyclerBankbooklistAdapter(viewModel.recyclerBankbooklistList.value?:mutableListOf())
    binding.recyclerBankbooklist.adapter = recyclerBankbooklistAdapter
    binding.recyclerBankbooklist.addOnItemTouchListener(
      PinchZoomItemTouchListener(this, object:PinchZoomItemTouchListener.PinchZoomListener{
        override fun onPinchZoom(position: Int) {
          Toast.makeText(this@BankbookActivity,"position is..$position",Toast.LENGTH_SHORT).show()
        }
      })
    )
    recyclerBankbooklistAdapter.setOnItemClickListener(
      object : RecyclerBankbooklistAdapter.OnItemClickListener {
        override fun onItemClick(view:View, position:Int, item : Bankbook1RowModel) {
          onClickRecyclerBankbooklist(view, position, item)
        }
      }
    )
    viewModel.recyclerBankbooklistList.observe(this) {
      if(it!=null)
        recyclerBankbooklistAdapter.updateData(it)
      else{
        //Toast.makeText(this,"not Available",Toast.LENGTH_SHORT).show()
        var emptylist= arrayListOf<Bankbook1RowModel>()
        recyclerBankbooklistAdapter.updateData(emptylist)
      }
    }
    binding.bankbookVM = viewModel
    this@BankbookActivity.hideKeyboard()
  }
  public override fun setUpClicks(): Unit {
    binding.txtFromdate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        fromDate = SalesDatetime
        binding.txtFromdate.text=SalesDatetime


        viewModel.bankbookModel.value!!.Fromdate = SalesDatetime
        binding.txtFromdate.text=SalesDatetime
        getBankBookList()
      }
    }
    binding.txtTodate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        toDate = SalesDatetime
        binding.txtTodate.text=SalesDatetime


        viewModel.bankbookModel.value!!.Todate = SalesDatetime
        binding.txtTodate.text=SalesDatetime
        getBankBookList()
      }
    }
    binding.imageBackImg.setOnClickListener { super.onBackPressed() }
  }

  public fun onClickRecyclerBankbooklist(view: View, position: Int, item: Bankbook1RowModel): Unit {
    if (reportType.equals("BankBook")) {
      var destIntent = BankwisebookActivity.getIntent(this, null)
      var request = BankBookRequest(superAdminDetails!!.orgID, "Ledger", fromDate, toDate, item.BranchId, reportType,
        listOf(GroupItem(item.groupid)))
      destIntent.putExtra("Request", request)
      destIntent.putExtra("branch_name", item.BranchName)
      destIntent.putExtra("from_date", fromDate)
      destIntent.putExtra("to_date", toDate)
      startActivity(destIntent)
    } else {
      var destIntent=BankwisebookActivity.getIntent(this,null)
      var request=BankBookRequest(superAdminDetails!!.orgID,"Ledger",fromDate,toDate,item.BranchId,reportType)
      destIntent.putExtra("Request",request)
      destIntent.putExtra("branch_name",item.BranchName)
      destIntent.putExtra("from_date",fromDate)
      destIntent.putExtra("to_date",toDate)
      startActivity(destIntent)
    }
    }
  public override fun addObservers(): Unit {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@BankbookActivity) {
      if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@BankbookActivity.showProgressDialog()
      } else  {
        progressDialog?.dismiss()
      }
    }
    viewModel.createBankAccountLiveData.observe(this@BankbookActivity) {
      if(it is SuccessResponse) {
        val response = it.getContentIfNotHandled()
        onSuccessCreateBankAccount(it)
      } else if(it is ErrorResponse)  {
        onErrorCreateBankAccount(it.data ?:Exception())
      }
    }
  }

  private fun onSuccessCreateBankAccount(response: SuccessResponse<BankBookResponse>): Unit
  {
    if(response.data.status)
      viewModel.bindBankBookResponse(response.data)
   /* else
      Toast.makeText(this,"not Available",Toast.LENGTH_SHORT).show()*/
  }

  private fun onErrorCreateBankAccount(exception: Exception): Unit {
    when(exception) {
      is NoInternetConnection -> {
        Snackbar.make(binding.root, exception.message?:"", Snackbar.LENGTH_LONG).show()
      }
      is HttpException -> {
        val errorBody = exception.response()?.errorBody()?.string()
        val errorObject = if (errorBody != null  && errorBody.isJSONObject()) JSONObject(errorBody)
        else JSONObject()
        Toast.makeText(this@BankbookActivity,MyApp.getInstance().getString(R.string.lbl_network_error),Toast.LENGTH_LONG).show()
      }
    }
  }

  public companion object {
    public const val TAG: String = "BANKBOOK_ACTIVITY"

    public fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, BankbookActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }

  override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, p3: Long)
  {
    if(parent!!.id==binding.branchlistspinner.id)
    {
        branch = spinnerBranchList[position].branchID!!
      getBankBookList()
    }
    if(parent.id==binding.periodSpinner.id)
    {
        when(position){
          0->{
            toDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            fromDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            Log.e("dates","From:..$fromDate....To:..$toDate")
            getBankBookList()
          }
          1->{
            toDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            var FromDate = Date(Date().getTime() - ( day* 86400000))
            fromDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(FromDate)
            getBankBookList()
          }
          2->{
            toDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            fromDate=String.format("%02d-%02d-%d",1,month,year)
            getBankBookList()
          }
          3->{
            toDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            if(month>=4){
              fromDate=String.format("%02d-%02d-%d",1,4,year)
            } else{
              fromDate=String.format("%02d-%02d-%d",1,4,year-1)
              toDate=String.format("%02d-%02d-%d",31,3,year)
            }
            getBankBookList()
          }
        }
      }
    }


  private fun getBankBookList() {
    bankbookModel.Fromdate=fromDate
    bankbookModel.Todate=toDate
    binding.txtFromdate.text=fromDate
    binding.txtTodate.text=toDate
    val request = BankBookRequest(superAdminDetails!!.orgID, screenName, fromDate, toDate, branch,reportType)
    if(fromDate>toDate) {
      Toast.makeText(this, "invalid dates", Toast.LENGTH_SHORT).show()
    }else {
      viewModel.onClickOnCreate(request)
    }
  }
  override fun onNothingSelected(p0: AdapterView<*>?) {
  }
}
