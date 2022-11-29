package com.wms.superadmin.modules.salesordervoucher.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
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
import com.wms.superadmin.databinding.ActivitySalesOrderVoucherBinding
import com.wms.superadmin.extensions.*
import com.wms.superadmin.modules.salesorderbranch.data.model.SalesOrderRowModel
import com.wms.superadmin.modules.salesorderbranch.ui.RecyclerSalesOrderAdapter
import com.wms.superadmin.modules.salesordervoucher.data.viewmodel.SalesOrderVoucherVM
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
public class SalesOrderVoucherActivity :
    BaseActivity<ActivitySalesOrderVoucherBinding>(R.layout.activity_sales_order_voucher),
  AdapterView.OnItemSelectedListener {

  lateinit var recyclersalesordervoucherAdapter: RecyclerSalesOrderAdapter
  private lateinit var request_voucher: SalesOrderReportRequest
  private val viewModel: SalesOrderVoucherVM by viewModels<SalesOrderVoucherVM>()
  private var branchID: String?=""
  private var screenName: String=""
  private var branchname: String=""
  private val prefs: PreferenceHelper by inject()
  var SuperAdminDetails=prefs.getSADetails<LoginResponse>()
  var user=prefs.getSADetails<LoginResponse>()!!
  private var specificbranch: BranchListItem?=null
  var spinnerBranchList = arrayListOf<BranchListItem>()
  private var SalesorPurchase: String = ""
  private var fromDate= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
  private var toDate= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

  var mFromDate = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
  var mToDate = LocalDate.parse(toDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"))

  @RequiresApi(Build.VERSION_CODES.O)
  public override fun onInitialized(): Unit {
    viewModel.navArguments = intent.extras?.getBundle("bundle")

    SalesorPurchase = intent.getStringExtra("SalesORPurchase")!!
    Log.e("CheckSalesPurchase ", SalesorPurchase.toString())
    if (SalesorPurchase== ReportTypes.SALESORDER){ viewModel.salesOrderVoucherModel.value?.txtOrder="Sales Order" }
    else{ viewModel.salesOrderVoucherModel.value?.txtOrder="Purchase Order" }

    request_voucher=intent.getSerializableExtra("RequestVoucher")as SalesOrderReportRequest
    branchname=intent.getStringExtra("branch_name")!!
//    branchID=intent.getStringExtra("branch_ID")
    screenName = request_voucher.screenName!!
    branchID = request_voucher.branchID!!.toIntOrNull().toString()
    Log.e("check_request_passed_V ", branchID.toString())
    Log.e("check_branchname_passed_v ",branchname )

    fromDate = request_voucher.fromDate!!
    toDate = request_voucher.toDate!!

    Log.e("check_fromDate ", fromDate)
    Log.e("check_toDate ", toDate)

    viewModel.salesOrderVoucherModel.value?.txtFromdate = fromDate
    mFromDate = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    viewModel.salesOrderVoucherModel.value?.txtTodate = toDate
    mToDate = LocalDate.parse(toDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));

    val branchList= prefs.getBranchlist<ArrayList<BranchListItem>>()!!

    if (!user.branchID.isNullOrEmpty()){
      specificbranch = branchList.find { it.branchID == user.branchID }
      spinnerBranchList.add(specificbranch!!)
      val branchlistAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerBranchList.map { it.branchName })
      branchlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      binding.spinnerbranch.adapter=branchlistAdapter
      binding.spinnerbranch.onItemSelectedListener=this
    }
    else {
      val branchlistAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, branchList.map { it.branchName })
      branchlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      binding.spinnerbranch.adapter = branchlistAdapter
      var branch = branchList.find { it.branchID == branchID.toString() }
      var position = branchList.indexOf(branch)
      binding.spinnerbranch.setSelection(position)
      binding.spinnerbranch.onItemSelectedListener = this
    }

    recyclersalesordervoucherAdapter = RecyclerSalesOrderAdapter(viewModel.recyclerGroup177List.value?:mutableListOf())
    binding.recyclerSalesOrderVoucher.adapter = recyclersalesordervoucherAdapter
    binding.recyclerSalesOrderVoucher.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

    recyclersalesordervoucherAdapter.setOnItemClickListener(
      object : RecyclerSalesOrderAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int, item: SalesOrderRowModel) {
          onClickRecyclerSalesOrderVoucher(view, position, item)
        }
      }
    )
    viewModel.recyclerGroup177List.observe(this) {
      recyclersalesordervoucherAdapter.updateData(it as ArrayList<SalesOrderRowModel>,3)
    }
    binding.salesOrderVoucherVM = viewModel
    this@SalesOrderVoucherActivity.hideKeyboard()
    getSalesOrderVoucherList()
  }

  @RequiresApi(Build.VERSION_CODES.O)
  public override fun setUpClicks(): Unit {
    binding.txtFromdate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesOrderDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        fromDate = SalesOrderDatetime
        mFromDate=LocalDate.parse(SalesOrderDatetime, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        Log.e("Changed_fromdate ", mFromDate.toString())

        if (!user.branchID.isNullOrEmpty()) {
          branchID = intent.getStringExtra("branch_ID")
        }

        viewModel.salesOrderVoucherModel.value!!.txtFromdate = SalesOrderDatetime
        binding.txtFromdate.text=SalesOrderDatetime
        getSalesOrderVoucherList()
      }
    }
    binding.txtTodate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesOrderDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        toDate = SalesOrderDatetime
        mToDate=LocalDate.parse(SalesOrderDatetime, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Log.e("Changed_todate ", toDate.toString())

        if (!user.branchID.isNullOrEmpty()) {
          branchID = intent.getStringExtra("branch_ID")
        }

        viewModel.salesOrderVoucherModel.value?.txtTodate = SalesOrderDatetime
        binding.txtTodate.text=SalesOrderDatetime
        getSalesOrderVoucherList()
      }
    }

    binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {

        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        recyclersalesordervoucherAdapter.filter.filter(newText)
        return false
      }
    })

    binding.backLSOV.setOnClickListener { super.onBackPressed() }
  }

  public fun onClickRecyclerSalesOrderVoucher(view: View, position: Int, item: SalesOrderRowModel): Unit {
    val builder = AlertDialog.Builder(this)
    val itemdetail_heading = "<b>" + "Item Details" + "</b> "
    builder.setTitle(Html.fromHtml(itemdetail_heading))
    if (SalesorPurchase==ReportTypes.SALESORDER)
      builder.setMessage("SO Number: " + item.ordernumber +"\n\n"+ "Voucher type: " + "" + item.Vouchertype + "\n\n" + "Items" + "\n" + item.txtmerged_details)
    else if (SalesorPurchase==ReportTypes.PURCHASEORDER)
      builder.setMessage("PO Number: " + item.ordernumber +"\n\n"+ "Voucher type: " + "" + item.Vouchertype + "\n\n" + "Items" + "\n" + item.txtmerged_details)

    builder.setPositiveButton(android.R.string.yes) { dialog, which -> }
    builder.show()
  }

  public override fun addObservers(): Unit {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@SalesOrderVoucherActivity) {
      if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@SalesOrderVoucherActivity.showProgressDialog()
      } else  {
        progressDialog?.dismiss()
      }
    }
    viewModel.createSalesOrderReportLiveData.observe(this@SalesOrderVoucherActivity) {
      if(it is SuccessResponse) {
        val response = it.getContentIfNotHandled()
        onSuccessCreateSalesOrderReport(it)
      } else if(it is ErrorResponse)  {
        onErrorCreateSalesOrderReport(it.data ?:Exception())
      }
    }
  }

  private fun onSuccessCreateSalesOrderReport(response: SuccessResponse<CreateSalesOrderReportResponse>): Unit {
    Snackbar.make(binding.root, MyApp.getInstance().getString(R.string.lbl_success), Snackbar.LENGTH_LONG).show()
    if (SalesorPurchase == ReportTypes.SALESORDER) {
      if (response.data.salesorderlist!!.isNotEmpty()) {
        viewModel.bindCreateSalesOrderReportResponse(response.data.salesorderlist ?: arrayListOf(), true)
      } else {
        recyclersalesordervoucherAdapter.updateData(arrayListOf(), 0)
        binding.totalvalue.text = ""
      }
    } else if (SalesorPurchase == ReportTypes.PURCHASEORDER) {
      if (response.data.purchaselist!!.isNotEmpty()) {
        viewModel.bindCreateSalesOrderReportResponse(response.data.purchaselist ?: arrayListOf(), false)
      } else {
        recyclersalesordervoucherAdapter.updateData(arrayListOf(), 0)
        binding.totalvalue.text = ""
      }
    }
  }

  private fun onErrorCreateSalesOrderReport(exception: Exception): Unit {
    recyclersalesordervoucherAdapter.updateData(arrayListOf(), 0)
    binding.totalvalue.text = ""
    when(exception) {
      is NoInternetConnection -> {
        Snackbar.make(binding.root, exception.message?:"", Snackbar.LENGTH_LONG).show()
      }
      is HttpException -> {
        val errorBody = exception.response()?.errorBody()?.string()
        val errorObject = if (errorBody != null  && errorBody.isJSONObject()) JSONObject(errorBody)
            else JSONObject()
        Snackbar.make(binding.root, "Vouchers Not available",
            Snackbar.LENGTH_LONG).show()
      }
    }
  }

  public companion object {
    public const val TAG: String = "SALES_ORDER_VOUCHER_ACTIVITY"

    public fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, SalesOrderVoucherActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//    TODO("Not yet implemented")
    if(parent!!.id==binding.spinnerbranch.id)
    {
      val branchList= prefs.getBranchlist<ArrayList<BranchListItem>>()!!
      branchID= branchList[position].branchID!!.toString()
//      flag_branchID = branch.toInt()
      if (user.branchID.isNullOrEmpty()) {
      getSalesOrderVoucherList()
      }
    }
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    TODO("Not yet implemented")
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun getSalesOrderVoucherList() {
    viewModel.salesOrderVoucherModel.value?.txtFromdate=fromDate
    viewModel.salesOrderVoucherModel.value?.txtTodate=toDate
    Log.e("fromdate_V ", fromDate)
    Log.e("todate_V ", toDate)
    Log.e("branchid_V ",branchID.toString())
    Log.e("ScreenName_V ",screenName)
    var request = SalesOrderReportRequest(SuperAdminDetails?.orgID,screenName,false,fromDate,toDate,branchID.toString())

    if(mFromDate>mToDate) {
      Toast.makeText(this, "invalid dates", Toast.LENGTH_SHORT).show()
      recyclersalesordervoucherAdapter.updateData(arrayListOf(), 0)
      binding.totalvalue.text = ""
    }
    else
      viewModel.getSalesOrderVoucher(request,SalesorPurchase)
  }
}
