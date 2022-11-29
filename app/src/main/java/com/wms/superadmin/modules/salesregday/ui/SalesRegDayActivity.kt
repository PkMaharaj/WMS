package com.wms.superadmin.modules.salesregday.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.appcomponents.views.DatePickerFragment
import com.wms.superadmin.databinding.ActivitySalesRegDayBinding
import com.wms.superadmin.extensions.*
import com.wms.superadmin.modules.salesregday.data.model.SalesRegDay1RowModel
import com.wms.superadmin.modules.salesregday.data.model.SalesRegDayModel
import com.wms.superadmin.modules.salesregday.data.viewmodel.SalesRegDayVM
import com.wms.superadmin.modules.salesregmonth.ui.RecyclerSalesRegMonthAdapter
import com.wms.superadmin.modules.salesregvoucher.ui.SalesRegVoucherActivity
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.Salesregister.SalesRegisterRequest
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import com.wms.superadmin.network.models.Salesregister.SalesRegisterResponse
import com.wms.superadmin.network.models.pojos.BranchListItem
import java.lang.Exception
import kotlin.Int
import kotlin.String
import kotlin.Unit
import org.json.JSONObject
import org.koin.android.ext.android.inject
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class SalesRegDayActivity :
    BaseActivity<ActivitySalesRegDayBinding>(R.layout.activity_sales_reg_day),
  AdapterView.OnItemSelectedListener {

  private var SalesorPurchase: String? = ""
  private val viewModel: SalesRegDayVM by viewModels<SalesRegDayVM>()
  private lateinit var request: SalesRegisterRequest

  private var branchname: String=""
  private val prefs: PreferenceHelper by inject()
  var user=prefs.getSADetails<LoginResponse>()!!
  var SuperAdminDetails=prefs.getSADetails<LoginResponse>()
  lateinit var salesRegDayModel: SalesRegDayModel
  private var specificbranch: BranchListItem?=null
  var spinnerBranchList = arrayListOf<BranchListItem>()
  lateinit var recyclersaleregdayAdapter: RecyclerSalesRegDayAdapter

  private var fromDate= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
  private var toDate= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
  var mFromDate = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
  var mToDate = LocalDate.parse(toDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))

  var screenName="Branch"
  var branch:String="0"
  var flag_screen_name:String=""
  var flag_branchID: Int? = 0
  private var branchID: Int? = 0
  var month = 0
  var dd:Int = 0
  var year:Int = 0

  override fun onInitialized(): Unit {
    viewModel.navArguments = intent.extras?.getBundle("bundle")
    salesRegDayModel=viewModel.salesRegDayModel.value!!

    SalesorPurchase = intent.getStringExtra("SalesORPurchase")!!
    Log.e("CheckSalesPurchase ", SalesorPurchase.toString())
    if (SalesorPurchase== ReportTypes.SALESREGISTER){ viewModel.salesRegDayModel.value?.txtOrder="Sales Register" }
    else{ viewModel.salesRegDayModel.value?.txtOrder="Purchase Register" }

    if (SalesorPurchase==ReportTypes.SALESREGISTER) viewModel.salesRegDayModel.value?.txtNetSales = "Net Sales"
    else if (SalesorPurchase==ReportTypes.PURCHASEREGISTER) viewModel.salesRegDayModel.value?.txtNetSales = "Net Purchase"

    request= intent.getSerializableExtra("RequestDay") as SalesRegisterRequest
    branchname=intent.getStringExtra("branch_name")!!
    Log.e("check_request_passed ", request.toString())
    Log.e("check_branchname_passed ",branchname )
    Log.e("CheckBranchID ", request.branchID!!)
    screenName = request.screenName!!
    branchID = request.branchID?.toIntOrNull()?:0

    fromDate = request.fromDate!!
    toDate = request.toDate!!

    salesRegDayModel.txtFromdate = fromDate
    mFromDate = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    salesRegDayModel.txtTodate = toDate
    mToDate = LocalDate.parse(toDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));


    val branchList = prefs.getBranchlist<ArrayList<BranchListItem>>()!!

    if (!user.branchID.isNullOrEmpty()){
      specificbranch = branchList.find { it.branchID == user.branchID }
      spinnerBranchList.add(specificbranch!!)
      val branchlistAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerBranchList.map { it.branchName })
      branchlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      binding.spinnersalesregday.adapter=branchlistAdapter
      binding.spinnersalesregday.onItemSelectedListener=this
    }
    else{
      val branchlistAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, branchList.map { it.branchName })
      branchlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      binding.spinnersalesregday.adapter=branchlistAdapter
      var branch=branchList.find { it.branchID==branchID.toString()}
      var position = branchList.indexOf(branch)
      binding.spinnersalesregday.setSelection(position)
      binding.spinnersalesregday.onItemSelectedListener=this
    }

    recyclersaleregdayAdapter = RecyclerSalesRegDayAdapter(viewModel.recyclerSalesRegDayList.value?: mutableListOf())
    binding.recyclerSalesRegDay.adapter = recyclersaleregdayAdapter

    recyclersaleregdayAdapter.setOnItemClickListener(
      object : RecyclerSalesRegDayAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int, item: SalesRegDay1RowModel) {
          onClickRecyclerSalesRegDay(view, position, item)
        }
      }
    )
    viewModel.recyclerSalesRegDayList.observe(this) {
      recyclersaleregdayAdapter.updateData(it as ArrayList<SalesRegDay1RowModel>)
    }
    binding.salesRegDayVM = viewModel
    this@SalesRegDayActivity.hideKeyboard()
    getSalesRegDayList()
  }

  override fun setUpClicks(): Unit {
    binding.txtFromdate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesRegDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        fromDate = SalesRegDatetime
        mFromDate=LocalDate.parse(SalesRegDatetime, DateTimeFormatter.ofPattern("dd-MM-yyyy"))

        if (!user.branchID.isNullOrEmpty()) {
          branchID = request.branchID!!.toInt()
        }

        viewModel.salesRegDayModel.value!!.txtFromdate = SalesRegDatetime
        binding.txtFromdate.text=SalesRegDatetime
        getSalesRegDayList()
      }
    }
    binding.txtTodate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesRegDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        toDate = SalesRegDatetime
        mToDate=LocalDate.parse(SalesRegDatetime, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        if (!user.branchID.isNullOrEmpty()) {
          branchID = request.branchID!!.toInt()
        }

        viewModel.salesRegDayModel.value?.txtTodate = SalesRegDatetime
        binding.txtTodate.text=SalesRegDatetime
        getSalesRegDayList()
      }
    }

    binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {

        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        recyclersaleregdayAdapter.filter.filter(newText)
        return false
      }
    })

    binding.backLSRD.setOnClickListener { super.onBackPressed() }
  }

  fun onClickRecyclerSalesRegDay(view: View, position: Int, item: SalesRegDay1RowModel): Unit {
    val destIntent =  SalesRegVoucherActivity.getIntent(this, null)
    val request_voucher = SalesRegisterRequest(user.orgID,"Voucher",item.creationdate,item.creationdate,item.branchid)
    destIntent.putExtra("RequestVoucher",request_voucher)
    destIntent.putExtra("SalesORPurchase",SalesorPurchase)
    destIntent.putExtra("branch_name",item.branchname)
    startActivity(destIntent)
  }

  override fun addObservers() {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@SalesRegDayActivity) {
      if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@SalesRegDayActivity.showProgressDialog()
      } else  {
        progressDialog?.dismiss()
      }
    }
    viewModel.createSalesRegisterLiveData.observe(this@SalesRegDayActivity) {
      if(it is SuccessResponse) {
        val response = it.getContentIfNotHandled()
        onSuccessCreateSalesRegister(it)
      } else if(it is ErrorResponse)  {
        onErrorCreateSalesRegister(it.data ?:Exception())
      }
    }
  }

  private fun onSuccessCreateSalesRegister(response: SuccessResponse<SalesRegisterResponse>) {
    Snackbar.make(binding.root, MyApp.getInstance().getString(R.string.lbl_success), Snackbar.LENGTH_LONG).show()
    if (SalesorPurchase==ReportTypes.SALESREGISTER) {
      if (!response.data.salesList!!.outputDataListObject.isNullOrEmpty()) {
        viewModel.bindCreateSalesRegisterResponse(response.data, true)
      }
      else{
        recyclersaleregdayAdapter.updateData(arrayListOf())
        binding.txtTotaldebitSalesregday.text=""
        binding.txtTotalnetsalesSalesregday.text=""
      }
    }

    else if (SalesorPurchase==ReportTypes.PURCHASEREGISTER)
      if (!response.data.purchaseList!!.outputDataListObject.isNullOrEmpty()) {
        viewModel.bindCreateSalesRegisterResponse(response.data, false)
      }
      else{
        recyclersaleregdayAdapter.updateData(arrayListOf())
        binding.txtTotaldebitSalesregday.text=""
        binding.txtTotalnetsalesSalesregday.text=""
      }
  }

  private fun onErrorCreateSalesRegister(exception: Exception) {
    recyclersaleregdayAdapter.updateData(arrayListOf())
    binding.txtTotaldebitSalesregday.text=""
    binding.txtTotalnetsalesSalesregday.text=""
    when(exception) {
      is NoInternetConnection -> {
        Snackbar.make(binding.root, exception.message?:"", Snackbar.LENGTH_LONG).show()
      }
      is HttpException -> {
        val errorBody = exception.response()?.errorBody()?.string()
        val errorObject = if (errorBody != null  && errorBody.isJSONObject()) JSONObject(errorBody)
            else JSONObject()
        Snackbar.make(binding.root, MyApp.getInstance().getString(R.string.lbl_failure),
            Snackbar.LENGTH_LONG).show()
      }
    }
  }

  companion object {
    const val TAG: String = "SALES_REG_DAY_ACTIVITY"

    fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, SalesRegDayActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }

  override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    if(parent!!.id==binding.spinnersalesregday.id)
    {
      val branchList= prefs.getBranchlist<ArrayList<BranchListItem>>()!!
      branchID= branchList[position].branchID!!.toIntOrNull()

      if (user.branchID.isNullOrEmpty()) {
        getSalesRegDayList()
      }
    }
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    TODO("Not yet implemented")
  }

  private fun getSalesRegDayList() {
    salesRegDayModel.txtFromdate=fromDate
    salesRegDayModel.txtTodate=toDate
    binding.txtFromdate.text=fromDate
    binding.txtTodate.text=toDate
    Log.e("fromdate_SRM ", fromDate)
    Log.e("todate_SRM ", toDate)
    Log.e("Flag_screen_name_SRM ", screenName)
    Log.e("Flag_branchID_SRM ", branchID.toString())
    if (flag_screen_name.equals("Month") || flag_screen_name.equals("Voucher")) {
      screenName = flag_screen_name
    }
    var request = SalesRegisterRequest(user.orgID, screenName, fromDate, toDate, branchID.toString())
    if(mFromDate>mToDate) {
      Toast.makeText(this, "invalid dates", Toast.LENGTH_SHORT).show()
      recyclersaleregdayAdapter.updateData(arrayListOf())
      binding.txtTotaldebitSalesregday.text=""
      binding.txtTotalnetsalesSalesregday.text=""
    }
    else
      viewModel.SalesRegDay(request,SalesorPurchase)
  }
}
