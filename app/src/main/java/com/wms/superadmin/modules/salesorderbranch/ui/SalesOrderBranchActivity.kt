package com.wms.superadmin.modules.salesorderbranch.ui

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
import com.wms.superadmin.databinding.ActivitySalesOrderBranchBinding
import com.wms.superadmin.extensions.*
import com.wms.superadmin.modules.dashboardsuperadmin.ui.DashboardSuperadminActivity
import com.wms.superadmin.modules.salesorderbranch.data.model.SalesOrderBranchModel
import com.wms.superadmin.modules.salesorderbranch.data.model.SalesOrderRowModel
import com.wms.superadmin.modules.salesorderbranch.data.viewmodel.SalesOrderBranchVM
import com.wms.superadmin.modules.salesordermonthly.ui.SalesOrderMonthlyActivity
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.createsalesorderreport.CreateSalesOrderReportResponse
import com.wms.superadmin.network.models.createsalesorderreport.SalesOrderReportRequest
import com.wms.superadmin.network.models.pojos.BranchListItem
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import org.json.JSONObject
import org.koin.android.ext.android.inject
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

public class SalesOrderBranchActivity :
  BaseActivity<ActivitySalesOrderBranchBinding>(R.layout.activity_sales_order_branch),
  AdapterView.OnItemSelectedListener {

  private var specificbranch: BranchListItem?=null
  private lateinit var recyclerSalesOrderAdapter: RecyclerSalesOrderAdapter
  private val viewModel: SalesOrderBranchVM by viewModels<SalesOrderBranchVM>()
  private val prefs: PreferenceHelper by inject()
  var user=prefs.getSADetails<LoginResponse>()!!
  var spinnerBranchList = arrayListOf<BranchListItem>()
  var SuperAdminDetails=prefs.getSADetails<LoginResponse>()
  var CurrentState=0
  private var SalesorPurchase: String? = ""

  private val REQUEST_CODE_SALES_ORDER_MONTHLY_ACTIVITY: Int = 715

  private var fromDate= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
  private var toDate= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
  var mFromDate=Date()
  var mToDate=Date()
  var screenName="Branch"
  var branch:String="0"
  var flag_screen_name:String=""
  var flag_branchID: Int=0

  private lateinit var salesorderModel: SalesOrderBranchModel
  private var salesOrderBranchModel=SalesOrderBranchModel()

  @RequiresApi(Build.VERSION_CODES.O)
  public override fun onInitialized(): Unit {
    viewModel.navArguments = intent.extras?.getBundle("bundle")
    salesorderModel=viewModel.salesOrderBranchModel.value!!

    SalesorPurchase = intent.getStringExtra(IntentParams.ORDERSCREEN)
    fromDate=intent.getStringExtra(IntentParams.FROM_DATE)?:fromDate
    toDate=intent.getStringExtra(IntentParams.To_DATE)?:toDate
    Log.e("CheckSalesPurchase ", SalesorPurchase.toString())
    if (SalesorPurchase==ReportTypes.SALESORDER){ viewModel.salesOrderBranchModel.value?.txtOrder="Sales Order" }
    else{ viewModel.salesOrderBranchModel.value?.txtOrder="Purchase Order" }

    salesorderModel.txtFromdate=fromDate
    salesorderModel.txtTodate=toDate

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
      binding.spinnerBtnprimary.adapter = branchlistAdapter
      binding.spinnerBtnprimary.onItemSelectedListener = this

    Log.e("date1 ", fromDate)
    Log.e("date2 ", toDate)

    recyclerSalesOrderAdapter = RecyclerSalesOrderAdapter(viewModel.recyclerGroup177List.value?:mutableListOf())
    binding.recyclersalesorderbranch.adapter = recyclerSalesOrderAdapter

    recyclerSalesOrderAdapter.setOnItemClickListener(object : RecyclerSalesOrderAdapter.OnItemClickListener {
        override fun onItemClick(view:View, position:Int, item: SalesOrderRowModel) {
          onClickRecyclerSalesOrder(view, position, item)
        }
      }
    )
    viewModel.recyclerGroup177List.observe(this) {
      recyclerSalesOrderAdapter.updateData((it?: arrayListOf()) as ArrayList<SalesOrderRowModel>,CurrentState)
    }


    binding.salesOrderBranchVM = viewModel
    this@SalesOrderBranchActivity.hideKeyboard()
    getSalesOrderList(SalesorPurchase)
  }

  public override fun setUpClicks(): Unit {

    binding.txtFromdate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesOrderDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        fromDate = SalesOrderDatetime
        mFromDate = selectedDate

        if (!user.branchID.isNullOrEmpty()){
          flag_branchID = user.branchID!!.toInt()
        }

        viewModel.salesOrderBranchModel.value!!.txtFromdate = SalesOrderDatetime
        binding.txtFromdate.text=SalesOrderDatetime
        getSalesOrderList(SalesorPurchase)
      }
    }
    binding.txtTodate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesOrderDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        toDate = SalesOrderDatetime
        mToDate = selectedDate

        if (!user.branchID.isNullOrEmpty()){
          flag_branchID = user.branchID!!.toInt()
        }

        viewModel.salesOrderBranchModel.value?.txtTodate = SalesOrderDatetime
        binding.txtTodate.text=SalesOrderDatetime
        getSalesOrderList(SalesorPurchase)
      }
    }

    binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {

        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        recyclerSalesOrderAdapter.filter.filter(newText)
        return false
      }
    })

    binding.backLSOB.setOnClickListener { super.onBackPressed() }
  }


  public fun onClickRecyclerSalesOrder(view: View, position: Int, item: SalesOrderRowModel): Unit {
    Log.e("Check_branchid ",item.branchID.toString())
        val destIntent = SalesOrderMonthlyActivity.getIntent(this, null)
        val request = SalesOrderReportRequest(SuperAdminDetails!!.orgID, "Month",false,fromDate,toDate,item.branchID.toString())
        destIntent.putExtra("Request",request)
        destIntent.putExtra("SalesORPurchase",SalesorPurchase)
        destIntent.putExtra("branch_name",item.so_branchname)
        startActivity(destIntent)
    }

  public override fun addObservers(): Unit {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@SalesOrderBranchActivity) {
      if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@SalesOrderBranchActivity.showProgressDialog()
      } else  {
        progressDialog?.dismiss()
      }
    }
    viewModel.createSalesOrderReportLiveData.observe(this@SalesOrderBranchActivity) {
      if(it is SuccessResponse) {
        val response = it.getContentIfNotHandled()
        onSuccessCreateSalesOrderReport(it)
      } else if(it is ErrorResponse)  {
        onErrorCreateSalesOrderReport(it.data ?:Exception())
      }
    }
  }

  private
  fun onSuccessCreateSalesOrderReport(response: SuccessResponse<CreateSalesOrderReportResponse>): Unit {
    Snackbar.make(binding.root, MyApp.getInstance().getString(R.string.lbl_success), Snackbar.LENGTH_LONG).show()
    if (SalesorPurchase==ReportTypes.SALESORDER) {
      if (response.data.salesorderlist!!.isNotEmpty()) {
        viewModel.bindCreateSalesOrderReportResponse(response.data.salesorderlist?: arrayListOf(), true)
      }
      else{
        recyclerSalesOrderAdapter.updateData(arrayListOf(),CurrentState)
        binding.totalvouchercount.text=""
        binding.totalvalue.text=""
      }
    }
    else if (SalesorPurchase==ReportTypes.PURCHASEORDER){
        if (response.data.purchaselist!!.isNotEmpty()) {
        viewModel.bindCreateSalesOrderReportResponse(response.data.purchaselist?: arrayListOf(), false)
      }
        else{
          recyclerSalesOrderAdapter.updateData(arrayListOf(),CurrentState)
          binding.totalvouchercount.text=""
          binding.totalvalue.text=""
        }
      }
  }

  private fun onErrorCreateSalesOrderReport(exception: Exception): Unit {
    recyclerSalesOrderAdapter.updateData(arrayListOf(),CurrentState)
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
        Snackbar.make(binding.root, "Not available",
          Snackbar.LENGTH_LONG).show()
      }
    }
  }

  public companion object {
    public const val TAG: String = "SALES_ORDER_BRANCH_ACTIVITY"

    public fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, SalesOrderBranchActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }

  override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    if(parent!!.id==binding.spinnerBtnprimary.id)
    {
      val branchList= prefs.getBranchlist<ArrayList<BranchListItem>>()!!
      branch= branchList[position].branchID!!
      flag_branchID = branch.toInt()

      if (user.branchID.isNullOrEmpty()) {
        getSalesOrderList(SalesorPurchase)
      }

    }
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    TODO("Not yet implemented")
  }

  private fun getSalesOrderList(SalesorPurchase: String?) {
    salesorderModel.txtFromdate=fromDate
    salesorderModel.txtTodate=toDate
    binding.txtFromdate.text=fromDate
    binding.txtTodate.text=toDate
    Log.e("fromdate ", fromDate)
    Log.e("todate ", toDate)
    Log.e("Flag_screen_name ", screenName)
    Log.e("Flag_branchID ", flag_branchID.toString())
    if (flag_screen_name.equals("Month") || flag_screen_name.equals("Voucher")) {
      screenName = flag_screen_name
    }
    var request = SalesOrderReportRequest(SuperAdminDetails?.orgID, screenName, false, fromDate, toDate, flag_branchID.toString())
    if(mFromDate>mToDate) {
      Toast.makeText(this, "invalid dates", Toast.LENGTH_SHORT).show()
      recyclerSalesOrderAdapter.updateData(arrayListOf(),CurrentState)
      binding.totalvouchercount.text=""
      binding.totalvalue.text=""
    }
    else
      viewModel.getSalesOrder(request, SalesorPurchase)
  }
}
