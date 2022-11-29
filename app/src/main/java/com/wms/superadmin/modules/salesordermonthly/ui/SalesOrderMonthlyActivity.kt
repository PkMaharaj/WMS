package com.wms.superadmin.modules.salesordermonthly.ui

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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.appcomponents.views.DatePickerFragment
import com.wms.superadmin.databinding.ActivitySalesOrderMonthlyBinding
import com.wms.superadmin.extensions.*
import com.wms.superadmin.modules.salesorderbranch.data.model.SalesOrderRowModel
import com.wms.superadmin.modules.salesorderbranch.ui.RecyclerSalesOrderAdapter
import com.wms.superadmin.modules.salesordermonthly.data.model.SalesOrderMonthlyModel
import com.wms.superadmin.modules.salesordermonthly.data.viewmodel.SalesOrderMonthlyVM
import com.wms.superadmin.modules.salesordervoucher.ui.SalesOrderVoucherActivity
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
@RequiresApi(Build.VERSION_CODES.O)
public class SalesOrderMonthlyActivity :
  BaseActivity<ActivitySalesOrderMonthlyBinding>(R.layout.activity_sales_order_monthly),
  AdapterView.OnItemSelectedListener {

  lateinit var recyclersalesorderAdapter: RecyclerSalesOrderAdapter
  private lateinit var request: SalesOrderReportRequest
  private var branchID: Int=0
  private var screenName: String=""
  private var branchname: String=""
  private val viewModel: SalesOrderMonthlyVM by viewModels<SalesOrderMonthlyVM>()
  private val prefs: PreferenceHelper by inject()
  var user=prefs.getSADetails<LoginResponse>()!!
  var SuperAdminDetails=prefs.getSADetails<LoginResponse>()
  lateinit var salesOrderMonthlyModel: SalesOrderMonthlyModel
  private var specificbranch: BranchListItem?=null
  var spinnerBranchList = arrayListOf<BranchListItem>()
  private var SalesorPurchase: String = ""

  private var fromDate= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
  private var toDate= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
  var mFromDate=Date()
  var mToDate=Date()

  public override fun onInitialized(): Unit {
    viewModel.navArguments = intent.extras?.getBundle("bundle")
    request=intent.getSerializableExtra("Request")as SalesOrderReportRequest
    branchname=intent.getStringExtra("branch_name")!!

    SalesorPurchase = intent.getStringExtra("SalesORPurchase")!!
    Log.e("CheckSalesPurchase ", SalesorPurchase.toString())
    if (SalesorPurchase==ReportTypes.SALESORDER){ viewModel.salesOrderMonthlyModel.value?.txtOrder="Sales Order" }
    else{ viewModel.salesOrderMonthlyModel.value?.txtOrder="Purchase Order" }

    Log.e("check_request_passed ", request.toString())
    Log.e("check_branchname_passed ",branchname )
    Log.e("SalesorPurchase_month ",SalesorPurchase)
    screenName = request.screenName!!
    branchID = request.branchID!!.toInt()

    fromDate = request.fromDate!!
    toDate = request.toDate!!
    viewModel.salesOrderMonthlyModel.value?.txtFromdate = fromDate
    viewModel.salesOrderMonthlyModel.value?.txtTodate = toDate
//    viewModel.salesOrderMonthlyModel.value?.txtBranch = branchname

    val branchList = prefs.getBranchlist<ArrayList<BranchListItem>>()!!

    if (!user.branchID.isNullOrEmpty()){
      specificbranch = branchList.find { it.branchID == user.branchID }
      spinnerBranchList.add(specificbranch!!)
      val branchlistAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerBranchList.map { it.branchName })
      branchlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      binding.spinnerBranch.adapter=branchlistAdapter
      binding.spinnerBranch.onItemSelectedListener=this
    }
    else{
      val branchlistAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, branchList.map { it.branchName })
      branchlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      binding.spinnerBranch.adapter=branchlistAdapter
      var branch=branchList.find { it.branchID==branchID.toString()}
      var position = branchList.indexOf(branch)
      binding.spinnerBranch.setSelection(position)
      binding.spinnerBranch.onItemSelectedListener=this
    }

    recyclersalesorderAdapter = RecyclerSalesOrderAdapter(viewModel.recyclerGroup177List.value?: mutableListOf())
    binding.recyclersalesordermonthly.adapter = recyclersalesorderAdapter

    recyclersalesorderAdapter.setOnItemClickListener(
      object : RecyclerSalesOrderAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int, item: SalesOrderRowModel) {
          onClickRecyclerGroup177(view, position, item)
        }
      }
    )
    viewModel.recyclerGroup177List.observe(this) {
      recyclersalesorderAdapter.updateData(it as ArrayList<SalesOrderRowModel>,1)
    }
    binding.salesOrderMonthlyVM = viewModel
    this@SalesOrderMonthlyActivity.hideKeyboard()
    getSalesOrderMonthlyList()
  }


  public override fun setUpClicks(): Unit {
    binding.txtFromdate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesOrderDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        fromDate = SalesOrderDatetime
        mFromDate=selectedDate

        if (!user.branchID.isNullOrEmpty()) {
          branchID = request.branchID!!.toInt()
        }

        viewModel.salesOrderMonthlyModel.value!!.txtFromdate = SalesOrderDatetime
        binding.txtFromdate.text=SalesOrderDatetime
        getSalesOrderMonthlyList()
      }
    }
    binding.txtTodate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesOrderDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        toDate = SalesOrderDatetime
        mToDate=selectedDate

        if (!user.branchID.isNullOrEmpty()) {
          branchID = request.branchID!!.toInt()
        }

        viewModel.salesOrderMonthlyModel.value?.txtTodate = SalesOrderDatetime
        binding.txtTodate.text=SalesOrderDatetime
        getSalesOrderMonthlyList()
      }
    }

    binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {

        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        recyclersalesorderAdapter.filter.filter(newText)
        return false
      }
    })

    binding.backLSOM.setOnClickListener { super.onBackPressed() }
  }


  public fun onClickRecyclerGroup177(view: View, position: Int, item: SalesOrderRowModel): Unit {
    Log.e("branchIDVoucher-- ",item.branchID.toString())
    if (item.txtvouchers!! == "0"){
      Toast.makeText(this,"Vouchers are not available", Toast.LENGTH_SHORT).show()
    }
    else{

/*      var newStrg= item.txtMonth
      val year = newStrg!!.split("-").toTypedArray()
      Log.e("monthname- ",year[1])

      fromDate = "01" + "-" + item.monthID + "-" + month[1]

      toDate = if (month[0].equals("April") || month[0].equals("June") || month[0].equals("September")  || month[0].equals("November")){
        "30"  + "-" + item.monthID + "-" + month[1]
      } else if (month[0].equals("February")){
        "29"  + "-" + item.monthID + "-" + month[1]
      } else{
        "31"  + "-" + item.monthID + "-" + month[1]
      }*/

      val year = item.txtMonth?.split("-")?.get(1)?.toIntOrNull()?:0
      val month = item.monthID?:0
      fromDate=String.format("%02d-%02d-%d", 1,month,year)

      val day = if (month.equals(4) || month.equals(6) || month.equals(9)  || month.equals(11)){ 30 }
      else if (month.equals(2)){ 29 }
      else{ 31 }
      toDate=String.format("%02d-%02d-%d", day,month,year)

/*      var convertedDate = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
      convertedDate = convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()))*/

      val destIntent =  SalesOrderVoucherActivity.getIntent(this, null)
      val request_voucher = SalesOrderReportRequest(SuperAdminDetails!!.orgID, "Voucher",false,fromDate,toDate,item.branchID.toString())
      destIntent.putExtra("RequestVoucher",request_voucher)
      destIntent.putExtra("branch_name",item.so_branchname)
      destIntent.putExtra("SalesORPurchase",SalesorPurchase)
      destIntent.putExtra("branch_ID",request.branchID)
      startActivity(destIntent)
    }
  }

  public override fun addObservers(): Unit {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@SalesOrderMonthlyActivity) {
      if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@SalesOrderMonthlyActivity.showProgressDialog()
      } else {
        progressDialog?.dismiss()
      }
    }
    viewModel.createSalesOrderReportLiveData.observe(this@SalesOrderMonthlyActivity) {
      if(it is SuccessResponse) {
        val response = it.getContentIfNotHandled()
        onSuccessCreateSalesOrderReport(it)
      } else if(it is ErrorResponse) {
        onErrorCreateSalesOrderReport(it.data ?:Exception())
      }
    }
  }

  private fun onSuccessCreateSalesOrderReport(response: SuccessResponse<CreateSalesOrderReportResponse>): Unit {
    Snackbar.make(binding.root, MyApp.getInstance().getString(R.string.lbl_success), Snackbar.LENGTH_LONG).show()
    if (SalesorPurchase== ReportTypes.SALESORDER) {
      if (response.data.salesorderlist!!.isNotEmpty()) {
        viewModel.bindCreateSalesOrderReportResponse(response.data.salesorderlist?: arrayListOf(), true)
      }
      else{
        recyclersalesorderAdapter.updateData(arrayListOf(),0)
        binding.txtTotalVouchers.text=""
        binding.totalvalue.text=""
      }
    }
    else if (SalesorPurchase== ReportTypes.PURCHASEORDER){
      if (response.data.purchaselist!!.isNotEmpty()) {
        viewModel.bindCreateSalesOrderReportResponse(response.data.purchaselist?: arrayListOf(), false)
      }
      else{
        recyclersalesorderAdapter.updateData(arrayListOf(),0)
        binding.txtTotalVouchers.text=""
        binding.totalvalue.text=""
      }
    }
  }

  private fun onErrorCreateSalesOrderReport(exception: Exception): Unit {
    recyclersalesorderAdapter.updateData(arrayListOf(),0)
    binding.txtTotalVouchers.text=""
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
    public const val TAG: String = "SALES_ORDER_MONTHLY_ACTIVITY"

    public fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, SalesOrderMonthlyActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }


  override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    if(parent!!.id==binding.spinnerBranch.id)
    {
      val branchList= prefs.getBranchlist<ArrayList<BranchListItem>>()!!
      branchID= branchList[position].branchID!!.toInt()
//      flag_branchID = branch.toInt()
      if (user.branchID.isNullOrEmpty()) {
        getSalesOrderMonthlyList()
      }
    }
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    TODO("Not yet implemented")
  }

  private fun getSalesOrderMonthlyList() {
    viewModel.salesOrderMonthlyModel.value?.txtFromdate=fromDate
    viewModel.salesOrderMonthlyModel.value?.txtTodate=toDate
    Log.e("fromdate_M ", fromDate)
    Log.e("todate_M ", toDate)
    Log.e("branchid_M ",branchID.toString())
    Log.e("ScreenName_M ",screenName)
    var request = SalesOrderReportRequest(SuperAdminDetails?.orgID,screenName,false,fromDate,toDate,branchID.toString())

    if(mFromDate>mToDate) {
      Toast.makeText(this, "invalid dates", Toast.LENGTH_SHORT).show()
      recyclersalesorderAdapter.updateData(arrayListOf(), 0)
      binding.txtTotalVouchers.text = ""
      binding.totalvalue.text = ""
    }
    else
    viewModel.getSalesOrderMonthly(request,SalesorPurchase)
  }
}
