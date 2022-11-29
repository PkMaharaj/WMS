package com.wms.superadmin.modules.salesregbranches.ui

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
import androidx.appcompat.widget.SearchView
import androidx.core.view.contains
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.appcomponents.views.DatePickerFragment
import com.wms.superadmin.databinding.ActivitySalesRegBranchesBinding
import com.wms.superadmin.extensions.*
import com.wms.superadmin.modules.salesregbranches.data.model.SalesRegBranches1RowModel
import com.wms.superadmin.modules.salesregbranches.data.model.SalesRegBranchesModel
import com.wms.superadmin.modules.salesregbranches.data.viewmodel.SalesRegBranchesVM
import com.wms.superadmin.modules.salesregmonth.ui.SalesRegMonthActivity
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.Salesregister.SalesRegisterRequest
import com.wms.superadmin.network.models.Salesregister.SalesRegisterResponse
import com.wms.superadmin.network.models.pojos.BranchListItem
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
import kotlin.collections.contains as contains1

class SalesRegBranchesActivity : BaseActivity<ActivitySalesRegBranchesBinding>(R.layout.activity_sales_reg_branches), AdapterView.OnItemSelectedListener {

  private var SalesorPurchase: String? = ""
  private var specificbranch: BranchListItem?=null
  private val prefs: PreferenceHelper by inject()
  var user=prefs.getSADetails<LoginResponse>()!!
  var spinnerBranchList = arrayListOf<BranchListItem>()
  var SuperAdminDetails=prefs.getSADetails<LoginResponse>()
  var CurrentState=0
  private val viewModel: SalesRegBranchesVM by viewModels<SalesRegBranchesVM>()
  lateinit var recyclerSalesRegBranchesAdapter: RecyclerSalesRegBranchesAdapter

  private val REQUEST_CODE_SALES_ORDER_MONTHLY_ACTIVITY: Int = 715

  private var fromDate= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
  private var toDate= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
  var mFromDate= Date()
  var mToDate= Date()
  var screenName="Branch"
  var branch:String="0"
  var flag_screen_name:String=""
  var flag_branchID: Int=0

  private lateinit var salesregbranchesmodel: SalesRegBranchesModel

  override fun onInitialized(): Unit {
    viewModel.navArguments = intent.extras?.getBundle("bundle")

    SalesorPurchase = intent.getStringExtra(IntentParams.REGISTERSCREEN)
    Log.e("CheckSalesPurchase ", SalesorPurchase.toString())
    if (SalesorPurchase==ReportTypes.SALESREGISTER){ viewModel.salesRegBranchesModel.value?.txtOrder="Sales Register" }
    else{ viewModel.salesRegBranchesModel.value?.txtOrder="Purchase Register" }

    if (SalesorPurchase==ReportTypes.SALESREGISTER) viewModel.salesRegBranchesModel.value?.txtNetSales = "Net Sales"
    else if (SalesorPurchase==ReportTypes.PURCHASEREGISTER) viewModel.salesRegBranchesModel.value?.txtNetSales = "Net Purchase"

    salesregbranchesmodel=viewModel.salesRegBranchesModel.value!!
    fromDate=intent.getStringExtra(IntentParams.FROM_DATE)?:fromDate
    toDate=intent.getStringExtra(IntentParams.To_DATE)?:toDate
    salesregbranchesmodel.txtFromdate=fromDate
    salesregbranchesmodel.txtTodate=toDate

    val branchList= prefs.getBranchlist<ArrayList<BranchListItem>>()!!

    if (!user.branchID.isNullOrEmpty()) {
      flag_branchID = user.branchID!!.toInt()
      specificbranch = branchList.find { it.branchID == user.branchID }
      spinnerBranchList.add(specificbranch!!)
    }
    else {
      spinnerBranchList=branchList
    }
    val branchlistAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerBranchList.map { it.branchName })
    branchlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    binding.spinnersalesregbranch.adapter = branchlistAdapter
    binding.spinnersalesregbranch.onItemSelectedListener = this

    recyclerSalesRegBranchesAdapter = RecyclerSalesRegBranchesAdapter(viewModel.recyclerSalesRegBranchesList.value?:mutableListOf())
    binding.recyclerSalesRegBranches.adapter=recyclerSalesRegBranchesAdapter

    recyclerSalesRegBranchesAdapter.setOnItemClickListener(
    object : RecyclerSalesRegBranchesAdapter.OnItemClickListener {
      override fun onItemClick(view:View, position:Int, item: SalesRegBranches1RowModel) {
        onClickRecyclerSalesRegBranches(view, position, item)
      }
    }
    )
    viewModel.recyclerSalesRegBranchesList.observe(this) {
      recyclerSalesRegBranchesAdapter.updateData((it?: arrayListOf()) as ArrayList<SalesRegBranches1RowModel>)
    }
    binding.salesRegBranchesVM = viewModel
    this@SalesRegBranchesActivity.hideKeyboard()
    getSalesRegList()
  }

  override fun setUpClicks(): Unit {
    binding.txtFromdate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesRegDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        fromDate = SalesRegDatetime
        mFromDate = selectedDate

        if (!user.branchID.isNullOrEmpty()){
          flag_branchID = user.branchID!!.toInt()
        }

        viewModel.salesRegBranchesModel.value!!.txtFromdate = SalesRegDatetime
        binding.txtFromdate.text=SalesRegDatetime
        getSalesRegList()
      }
    }
    binding.txtTodate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesRegDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        toDate = SalesRegDatetime
        mToDate = selectedDate

        if (!user.branchID.isNullOrEmpty()){
          flag_branchID = user.branchID!!.toInt()
        }

        viewModel.salesRegBranchesModel.value?.txtTodate = SalesRegDatetime
        binding.txtTodate.text=SalesRegDatetime
        getSalesRegList()
      }
    }

    binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {

        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        recyclerSalesRegBranchesAdapter.filter.filter(newText)
        return false
      }
    })

    binding.backLSRB.setOnClickListener { super.onBackPressed() }
  }

  fun onClickRecyclerSalesRegBranches(view: View, position: Int, item: SalesRegBranches1RowModel): Unit {
    Log.e("Check_branchid ", item.branchid!!.toString())
    val destIntent = SalesRegMonthActivity.getIntent(this, null)
    val request = SalesRegisterRequest(SuperAdminDetails!!.orgID,"Month",fromDate,toDate,item.branchid.toString())
    destIntent.putExtra("Request",request)
    destIntent.putExtra("SalesORPurchase",SalesorPurchase)
    destIntent.putExtra("branch_name",item.txtBrachnameBranch)
    startActivity(destIntent)
  }

  override fun addObservers(): Unit {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@SalesRegBranchesActivity) {
      if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@SalesRegBranchesActivity.showProgressDialog()
      } else {
        progressDialog?.dismiss()
      }
    }
    viewModel.createSalesRegisterLiveData.observe(this@SalesRegBranchesActivity) {
      if(it is SuccessResponse) {
        val response = it.getContentIfNotHandled()
        onSuccessCreateSalesRegister(it)
      } else if(it is ErrorResponse) {
        onErrorCreateSalesRegister(it.data ?:Exception())
      }
    }
  }

  private fun onSuccessCreateSalesRegister(response: SuccessResponse<SalesRegisterResponse>): Unit {
    Snackbar.make(binding.root, MyApp.getInstance().getString(R.string.lbl_success), Snackbar.LENGTH_LONG).show()
    if (SalesorPurchase==ReportTypes.SALESREGISTER) {
      if (!response.data.salesList?.outputDataListObject!!.isNullOrEmpty()) {
        viewModel.bindCreateSalesRegisterResponse(response.data.salesList.outputDataListObject, true)
      }
      else{
        recyclerSalesRegBranchesAdapter.updateData(arrayListOf())
        binding.txtTotaldebitBranch.text=""
        binding.totalvouchercount.text=""
        binding.totalvalue.text=""
      }
    }
    else if (SalesorPurchase==ReportTypes.PURCHASEREGISTER) {
      if (!response.data.purchaseList?.outputDataListObject!!.isNullOrEmpty()) {
        viewModel.bindCreateSalesRegisterResponse(response.data.purchaseList?.outputDataListObject, false)
      }
      else{
        recyclerSalesRegBranchesAdapter.updateData(arrayListOf())
        binding.txtTotaldebitBranch.text=""
        binding.totalvouchercount.text=""
        binding.totalvalue.text=""
      }
    }
  }

  private fun onErrorCreateSalesRegister(exception: Exception): Unit {
    recyclerSalesRegBranchesAdapter.updateData(arrayListOf())
    binding.txtTotaldebitBranch.text=""
    binding.totalvouchercount.text=""
    binding.totalvalue.text=""
    when(exception) {
      is NoInternetConnection -> {
        Snackbar.make(binding.root, exception.message?:"", Snackbar.LENGTH_LONG).show()
      }
      is HttpException -> {
        val errorBody = exception.response()?.errorBody()?.string()
        val errorObject = if (errorBody != null  && errorBody.isJSONObject()) JSONObject(errorBody)
        else JSONObject()
        Snackbar.make(binding.root, "No data available",
        Snackbar.LENGTH_LONG).show()
      }
    }
  }

  companion object {
    const val TAG: String = "SALES_REG_BRANCHES_ACTIVITY"

    fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, SalesRegBranchesActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }

  override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    if(parent!!.id==binding.spinnersalesregbranch.id)
    {
      val branchList= prefs.getBranchlist<ArrayList<BranchListItem>>()!!
      branch= branchList[position].branchID!!
      flag_branchID = branch.toInt()

      if (user.branchID.isNullOrEmpty()) {
        getSalesRegList()
      }
    }
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    TODO("Not yet implemented")
  }

  private fun getSalesRegList() {
    salesregbranchesmodel.txtFromdate=fromDate
    salesregbranchesmodel.txtTodate=toDate
    binding.txtFromdate.text=fromDate
    binding.txtTodate.text=toDate
    Log.e("fromdate_SRB ", fromDate)
    Log.e("todate_SRB ", toDate)
    Log.e("Flag_screen_name_SRB ", screenName)
    Log.e("Flag_branchID_SRB ", flag_branchID.toString())
    if (flag_screen_name.equals("Month") || flag_screen_name.equals("Voucher")) {
      screenName = flag_screen_name
    }
    var request = SalesRegisterRequest(SuperAdminDetails?.orgID, screenName, fromDate, toDate, flag_branchID.toString())
    if(mFromDate>mToDate) {
      Toast.makeText(this, "invalid dates", Toast.LENGTH_SHORT).show()
      recyclerSalesRegBranchesAdapter.updateData(arrayListOf())
      binding.txtTotaldebitBranch.text=""
      binding.totalvouchercount.text=""
      binding.totalvalue.text=""
    }
    else
      viewModel.getSalesRegister(request,SalesorPurchase)
  }
}
