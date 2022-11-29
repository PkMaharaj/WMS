package com.wms.superadmin.modules.salesregvoucher.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.appcomponents.views.DatePickerFragment
import com.wms.superadmin.databinding.ActivitySalesRegVoucherBinding
import com.wms.superadmin.extensions.*
import com.wms.superadmin.modules.salesregvoucher.data.model.SalesRegVoucher1RowModel
import com.wms.superadmin.modules.salesregvoucher.data.model.SalesRegVoucherModel
import com.wms.superadmin.modules.salesregvoucher.data.viewmodel.SalesRegVoucherVM
import com.wms.superadmin.modules.transaction.ui.BranchTXNAdapter
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

class SalesRegVoucherActivity :
    BaseActivity<ActivitySalesRegVoucherBinding>(R.layout.activity_sales_reg_voucher),
  AdapterView.OnItemSelectedListener {

  private val viewModel: SalesRegVoucherVM by viewModels<SalesRegVoucherVM>()
  private lateinit var request: SalesRegisterRequest

  private var branchname: String=""
  private val prefs: PreferenceHelper by inject()
  var user=prefs.getSADetails<LoginResponse>()!!
  var SuperAdminDetails=prefs.getSADetails<LoginResponse>()
  lateinit var salesRegVoucherModel: SalesRegVoucherModel
  private var specificbranch: BranchListItem?=null
  var spinnerBranchList = arrayListOf<BranchListItem>()
  lateinit var recyclersaleregvoucherAdapter: RecyclerSalesRegVoucherAdapter
  private var SalesorPurchase: String = ""
  private var OrderNumber: String? = ""
  private var VoucherType: String? = ""
  private var fromDate= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
  private var toDate= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
  var mFromDate= Date()
  var mToDate= Date()

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
    salesRegVoucherModel=viewModel.salesRegVoucherModel.value!!

    SalesorPurchase = intent.getStringExtra("SalesORPurchase")!!
    Log.e("CheckSalesPurchase ", SalesorPurchase.toString())
    if (SalesorPurchase== ReportTypes.SALESREGISTER){ viewModel.salesRegVoucherModel.value?.txtOrder="Sales Register" }
    else{ viewModel.salesRegVoucherModel.value?.txtOrder="Purchase Register" }


    request= intent.getSerializableExtra("RequestVoucher") as SalesRegisterRequest
    branchname=intent.getStringExtra("branch_name")!!
    screenName = request.screenName!!
    branchID = request.branchID?.toIntOrNull()?:0
    Log.e("check_request_passed ", request.toString())
    Log.e("check_branchname_passed ",branchname )
    Log.e("CheckBranchID ", request.branchID!!)

    fromDate = request.fromDate!!
    toDate = request.toDate!!

    salesRegVoucherModel.txtFromdate = fromDate
    salesRegVoucherModel.txtTodate = toDate

    val branchList = prefs.getBranchlist<ArrayList<BranchListItem>>()!!

    if (!user.branchID.isNullOrEmpty()){
      specificbranch = branchList.find { it.branchID == user.branchID }
      spinnerBranchList.add(specificbranch!!)
      val branchlistAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerBranchList.map { it.branchName })
      branchlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      binding.spinnersalesregvoucher.adapter=branchlistAdapter
      binding.spinnersalesregvoucher.onItemSelectedListener=this
    }
    else{
      val branchlistAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, branchList.map { it.branchName })
      branchlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      binding.spinnersalesregvoucher.adapter=branchlistAdapter
      var branch=branchList.find { it.branchID==branchID.toString()}
      var position = branchList.indexOf(branch)
      binding.spinnersalesregvoucher.setSelection(position)
      binding.spinnersalesregvoucher.onItemSelectedListener=this
    }

    recyclersaleregvoucherAdapter = RecyclerSalesRegVoucherAdapter(viewModel.recyclerSalesRegVoucherList.value?: mutableListOf())
    binding.recyclerSalesRegVoucher.adapter = recyclersaleregvoucherAdapter

    recyclersaleregvoucherAdapter.setOnItemClickListener(
      object : RecyclerSalesRegVoucherAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int, item: SalesRegVoucher1RowModel) {
          onClickRecyclerSalesRegVoucher(view, position, item)
        }
      }
    )
    viewModel.recyclerSalesRegVoucherList.observe(this) {
      recyclersaleregvoucherAdapter.updateData(it as ArrayList<SalesRegVoucher1RowModel>)
    }
    binding.salesRegVoucherVM = viewModel
    this@SalesRegVoucherActivity.hideKeyboard()
    getSalesRegVoucherList()
  }

  override fun setUpClicks(): Unit {
    binding.txtFromdate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesRegDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        fromDate = SalesRegDatetime
        mFromDate = selectedDate

        if (!user.branchID.isNullOrEmpty()) {
          branchID = request.branchID!!.toInt()
        }

        viewModel.salesRegVoucherModel.value!!.txtFromdate = SalesRegDatetime
        binding.txtFromdate.text=SalesRegDatetime
        getSalesRegVoucherList()
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

        viewModel.salesRegVoucherModel.value?.txtTodate = SalesOrderDatetime
        binding.txtTodate.text=SalesOrderDatetime
        getSalesRegVoucherList()
      }
    }

    binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {

        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        recyclersaleregvoucherAdapter.filter.filter(newText)
        return false
      }
    })

    binding.backLSRV.setOnClickListener { super.onBackPressed() }
  }

  fun onClickRecyclerSalesRegVoucher(view: View, position: Int, item: SalesRegVoucher1RowModel): Unit {
    val builder = AlertDialog.Builder(this)
    val itemdetail_heading = "<b>" + "Details" + "</b>"
    builder.setTitle(Html.fromHtml(itemdetail_heading))
    builder.setMessage("Voucher ID: " + item.voucherid +"\n\n"+ "Voucher type: " + "" + item.Vouchertype + "\n\n" + "Items" + "\n" + item.mergeddetails)
    builder.setPositiveButton(android.R.string.yes) { dialog, which -> }
    builder.show()
  }

  override fun addObservers() {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@SalesRegVoucherActivity) {
      if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@SalesRegVoucherActivity.showProgressDialog()
      } else  {
        progressDialog?.dismiss()
      }
    }
    viewModel.createSalesRegisterLiveData.observe(this@SalesRegVoucherActivity) {
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
    if (SalesorPurchase==ReportTypes.SALESREGISTER){
      if (!response.data.salesList?.outputDataListObject!!.isNullOrEmpty()){
        viewModel.bindCreateSalesRegisterResponse(response.data.salesList?.outputDataListObject,true)
      }
      else{
        recyclersaleregvoucherAdapter.updateData(arrayListOf())
        binding.txt45.text=""
        binding.txt46.text=""
      }
    }
    else if (SalesorPurchase==ReportTypes.PURCHASEREGISTER){
      if (!response.data.purchaseList?.outputDataListObject!!.isNullOrEmpty()){
        viewModel.bindCreateSalesRegisterResponse(response.data.purchaseList?.outputDataListObject,false)
      }
      else{
        recyclersaleregvoucherAdapter.updateData(arrayListOf())
        binding.txt45.text=""
        binding.txt46.text=""
      }
    }
  }

  private fun onErrorCreateSalesRegister(exception: Exception) {
    recyclersaleregvoucherAdapter.updateData(arrayListOf())
    binding.txt45.text=""
    binding.txt46.text=""
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
    const val TAG: String = "SALES_REG_VOUCHER_ACTIVITY"

    fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, SalesRegVoucherActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }

  override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    if(parent!!.id==binding.spinnersalesregvoucher.id)
    {
      val branchList= prefs.getBranchlist<ArrayList<BranchListItem>>()!!
      branchID= branchList[position].branchID!!.toIntOrNull()

      if (user.branchID.isNullOrEmpty()) {
        getSalesRegVoucherList()
      }
    }
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    TODO("Not yet implemented")
  }

  private fun getSalesRegVoucherList() {
    salesRegVoucherModel.txtFromdate=fromDate
    salesRegVoucherModel.txtTodate=toDate
    binding.txtFromdate.text=fromDate
    binding.txtTodate.text=toDate
    Log.e("fromdate_SRV ", fromDate)
    Log.e("todate_SRV ", toDate)
    Log.e("Flag_screen_name_SRV ", screenName)
    Log.e("Flag_branchID_SRV ", branchID.toString())
    if (flag_screen_name.equals("Month") || flag_screen_name.equals("Voucher") || flag_screen_name.equals("Daywise")) {
      screenName = flag_screen_name
    }
    var request = SalesRegisterRequest(user.orgID, screenName, fromDate, toDate, branchID.toString())
    if(mFromDate>mToDate) {
      Toast.makeText(this, "invalid dates", Toast.LENGTH_SHORT).show()
      recyclersaleregvoucherAdapter.updateData(arrayListOf())
      binding.txt45.text=""
      binding.txt46.text=""
    }
    else
      viewModel.SalesRegVoucher(request,SalesorPurchase)
  }
}
