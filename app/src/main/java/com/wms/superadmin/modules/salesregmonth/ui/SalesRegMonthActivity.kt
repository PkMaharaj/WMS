package com.wms.superadmin.modules.salesregmonth.ui

import android.annotation.SuppressLint
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
import com.wms.superadmin.databinding.ActivitySalesRegMonthBinding
import com.wms.superadmin.extensions.*
import com.wms.superadmin.modules.salesregbranches.ui.RecyclerSalesRegBranchesAdapter
import com.wms.superadmin.modules.salesregday.ui.SalesRegDayActivity
import com.wms.superadmin.modules.salesregmonth.data.model.SalesRegMonth1RowModel
import com.wms.superadmin.modules.salesregmonth.data.model.SalesRegMonthModel
import com.wms.superadmin.modules.salesregmonth.data.viewmodel.SalesRegMonthVM
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.Salesregister.SalesRegisterRequest
import com.wms.superadmin.network.models.Salesregister.SalesRegisterResponse
import com.wms.superadmin.network.models.pojos.BranchListItem
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import org.json.JSONObject
import org.koin.android.ext.android.inject
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SalesRegMonthActivity :
  BaseActivity<ActivitySalesRegMonthBinding>(R.layout.activity_sales_reg_month),
  AdapterView.OnItemSelectedListener {

  private var specificbranch: BranchListItem?=null
  private val prefs: PreferenceHelper by inject()
  var user=prefs.getSADetails<LoginResponse>()!!
  var spinnerBranchList = arrayListOf<BranchListItem>()
  private lateinit var request: SalesRegisterRequest
  private var branchname: String=""
  private var SalesorPurchase: String = ""
  var CurrentState=0
  private val viewModel: SalesRegMonthVM by viewModels<SalesRegMonthVM>()

  private val REQUEST_CODE_SALES_REG_BRANCHES_ACTIVITY: Int = 801

  private var fromDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
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

  private lateinit var salesRegMonthModel: SalesRegMonthModel
  lateinit var recyclersalesorderAdapter: RecyclerSalesRegMonthAdapter

  @SuppressLint("SimpleDateFormat")
  @RequiresApi(Build.VERSION_CODES.O)
  override fun onInitialized(): Unit {
    viewModel.navArguments = intent.extras?.getBundle("bundle")
    salesRegMonthModel=viewModel.salesRegMonthModel.value!!

    SalesorPurchase = intent.getStringExtra("SalesORPurchase")!!
    Log.e("CheckSalesPurchase ", SalesorPurchase.toString())
    if (SalesorPurchase== ReportTypes.SALESREGISTER){ viewModel.salesRegMonthModel.value?.txtOrder="Sales Register" }
    else{ viewModel.salesRegMonthModel.value?.txtOrder="Purchase Register" }

    if (SalesorPurchase==ReportTypes.SALESREGISTER) viewModel.salesRegMonthModel.value?.txtNetSales = "Net Sales"
    else if (SalesorPurchase==ReportTypes.PURCHASEREGISTER) viewModel.salesRegMonthModel.value?.txtNetSales = "Net Purchase"

    viewModel.navArguments = intent.extras?.getBundle("bundle")
    request= intent.getSerializableExtra("Request") as SalesRegisterRequest
    branchname=intent.getStringExtra("branch_name")!!
    Log.e("check_request_passed ", request.toString())
    Log.e("check_branchname_passed ",branchname )
    screenName = request.screenName!!
    branchID = request.branchID!!.toInt()

    fromDate = request.fromDate!!
    toDate = request.toDate!!

    val sdf = SimpleDateFormat("dd-MM-yyyy")
    val date = sdf.parse(fromDate)
    val cal = Calendar.getInstance()
    cal.time = date!!
    month = cal[Calendar.MONTH]
    dd = cal[Calendar.DATE]
    year = cal[Calendar.YEAR]

    Log.e("Checkmonthyearfromdate ", "$dd $month $year")

    fromDate = "01" + "-" +  month.plus(1) + "-" + year
    toDate = "31" + "-" + "03" + "-" + year.plus(1)

    salesRegMonthModel.txtFromdate = fromDate
    salesRegMonthModel.txtTodate = toDate
//    viewModel.salesOrderMonthlyModel.value?.txtBranch = branchname

    val branchList = prefs.getBranchlist<ArrayList<BranchListItem>>()!!

    if (!user.branchID.isNullOrEmpty()){
      specificbranch = branchList.find { it.branchID == user.branchID }
      spinnerBranchList.add(specificbranch!!)
      val branchlistAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerBranchList.map { it.branchName })
      branchlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      binding.spinnersalesregMonth.adapter=branchlistAdapter
      binding.spinnersalesregMonth.onItemSelectedListener=this
    }
    else{
      val branchlistAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, branchList.map { it.branchName })
      branchlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
      binding.spinnersalesregMonth.adapter=branchlistAdapter
      var branch=branchList.find { it.branchID==branchID.toString()}
      var position = branchList.indexOf(branch)
      binding.spinnersalesregMonth.setSelection(position)
      binding.spinnersalesregMonth.onItemSelectedListener=this
    }

    recyclersalesorderAdapter = RecyclerSalesRegMonthAdapter(viewModel.recyclerSalesRegMonthList.value?: mutableListOf())
    binding.recyclerSalesRegMonth.adapter = recyclersalesorderAdapter

    recyclersalesorderAdapter.setOnItemClickListener(
      object : RecyclerSalesRegMonthAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int, item: SalesRegMonth1RowModel) {
          onClickRecyclerSalesRegMonth(view, position, item)
        }
      }
    )
    viewModel.recyclerSalesRegMonthList.observe(this) {
      recyclersalesorderAdapter.updateData(it as ArrayList<SalesRegMonth1RowModel>)
    }
    binding.salesRegMonthVM = viewModel
    this@SalesRegMonthActivity.hideKeyboard()
    getSalesRegMonthList()
  }


  override fun setUpClicks(): Unit {

    binding.txtFromdate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesRegDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        fromDate = SalesRegDatetime
        mFromDate=selectedDate

        if (!user.branchID.isNullOrEmpty()) {
          branchID = request.branchID!!.toInt()
        }

        viewModel.salesRegMonthModel.value!!.txtFromdate = SalesRegDatetime
        binding.txtFromdate.text=SalesRegDatetime
        getSalesRegMonthList()
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

        viewModel.salesRegMonthModel.value?.txtTodate = SalesOrderDatetime
        binding.txtTodate.text=SalesOrderDatetime
        getSalesRegMonthList()
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

    binding.backLSRM.setOnClickListener { super.onBackPressed() }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun onClickRecyclerSalesRegMonth(view: View, position: Int, item: SalesRegMonth1RowModel): Unit {
    val year = item.txtMonthSalesregbranch?.split("-")?.get(1)?.toIntOrNull()?:0
    val month = item.monthID?:0
    fromDate=String.format("%02d-%02d-%d", 1,month,year)

    val day = if (month.equals(4) || month.equals(6) || month.equals(9)  || month.equals(11)){ 30 }
    else if (month.equals(2)){ 29 }
    else{ 31 }
    toDate=String.format("%02d-%02d-%d", day,month,year)

    val destIntent =  SalesRegDayActivity.getIntent(this, null)
    val request_day = SalesRegisterRequest(user.orgID,"Daywise",fromDate, toDate,item.branchid)
    Log.e("Monthscreen_branchid ", item.branchid!!)
    destIntent.putExtra("RequestDay",request_day)
    destIntent.putExtra("SalesORPurchase",SalesorPurchase)
    destIntent.putExtra("branch_name",item.branchname)
    startActivity(destIntent)
  }

  override fun addObservers() {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@SalesRegMonthActivity) {
      if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@SalesRegMonthActivity.showProgressDialog()
      } else  {
        progressDialog?.dismiss()
      }
    }
    viewModel.createSalesRegisterLiveData.observe(this@SalesRegMonthActivity) {
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
      if (!response.data.salesList?.outputDataListObject?.isEmpty()!!) {
        viewModel.bindCreateSalesRegisterResponse(response.data.salesList?.outputDataListObject, true)
      }
      else{
        recyclersalesorderAdapter.updateData(arrayListOf())
        binding.txtTotaldebitSalesregbranch.text=""
        binding.txtTotalnetsalesSalesregbranch.text=""
        binding.txtTotalcreditSalesregbranch.text=""
      }
    }

    else if (SalesorPurchase==ReportTypes.PURCHASEREGISTER) {
      if (!response.data.purchaseList?.outputDataListObject!!.isNullOrEmpty()) {
        viewModel.bindCreateSalesRegisterResponse(response.data.purchaseList?.outputDataListObject, false)
      } else {
        recyclersalesorderAdapter.updateData(arrayListOf())
        binding.txtTotaldebitSalesregbranch.text = ""
        binding.txtTotalnetsalesSalesregbranch.text = ""
        binding.txtTotalcreditSalesregbranch.text = ""
      }
    }
  }

  private fun onErrorCreateSalesRegister(exception: Exception) {
    recyclersalesorderAdapter.updateData(arrayListOf())
    binding.txtTotaldebitSalesregbranch.text=""
    binding.txtTotalnetsalesSalesregbranch.text=""
    binding.txtTotalcreditSalesregbranch.text=""
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
    const val TAG: String = "SALES_REG_MONTH_ACTIVITY"

    fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, SalesRegMonthActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }

  override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    if(parent!!.id==binding.spinnersalesregMonth.id)
    {
      val branchList= prefs.getBranchlist<ArrayList<BranchListItem>>()!!
      branchID= branchList[position].branchID!!.toIntOrNull()
//      flag_branchID = branch.toInt()

      if (user.branchID.isNullOrEmpty()) {
        getSalesRegMonthList()
      }
    }

  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    TODO("Not yet implemented")
  }

  private fun getSalesRegMonthList() {
    salesRegMonthModel.txtFromdate=fromDate
    salesRegMonthModel.txtTodate=toDate
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
      recyclersalesorderAdapter.updateData(arrayListOf())
      binding.txtTotaldebitSalesregbranch.text = ""
      binding.txtTotalnetsalesSalesregbranch.text = ""
      binding.txtTotalcreditSalesregbranch.text = ""
    }
    else
      viewModel.getSalesRegisterMonth(request,SalesorPurchase)
  }
}
