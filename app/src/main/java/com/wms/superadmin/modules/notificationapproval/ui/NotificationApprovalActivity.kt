package com.wms.superadmin.modules.notificationapproval.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.wms.superadmin.network.models.NotificationVericationSOList.NotificationVerifySOListResponse
import com.wms.superadmin.modules.notificationapproval.data.viewmodel.NotificationApprovalVM
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.databinding.ActivityNotificationApprovalBinding
import com.wms.superadmin.extensions.NoInternetConnection
import com.wms.superadmin.extensions.hideKeyboard
import com.wms.superadmin.extensions.isJSONObject
import com.wms.superadmin.extensions.showProgressDialog
import com.wms.superadmin.modules.notificationapproval.data.model.*
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.notificationApprove.DLSalesOrderItemWarehouseMapForFiFO
import com.wms.superadmin.network.models.notificationApprove.DLSalesOrderItemWarehouseMapForNegativeStock
import com.wms.superadmin.network.models.notificationApprove.DLSalesOrderWithItemCreationsDiscount
import com.wms.superadmin.network.models.notificationApprove.NotificationApproveObject
//import com.wms.superadmin.modules.notificationapproval.data.model.NotificationApproval1RowModel
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import org.json.JSONObject
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import retrofit2.HttpException
import java.lang.Exception
import kotlin.Int
import kotlin.String
import kotlin.Unit

class NotificationApprovalActivity : BaseActivity<ActivityNotificationApprovalBinding>(R.layout.activity_notification_approval) {

  private var qty: String? = ""
  private var newoutstandingbbill: String?=""
  private var newcreditlimit: String?=""
  private var newcreditdays: String? = ""
  private var orderdiscountamt: String? = null
//  private var Orderchangeamt: String? = null
  private var so_number: String? = null
  private val viewModel: NotificationApprovalVM by viewModels<NotificationApprovalVM>()
  var saveresponse_isDirectSO: Boolean = false
  private lateinit var mnotificationapprovevalue: NotificationApprovalModel
  private val prefs: PreferenceHelper by inject()
  var user = prefs.getSADetails<LoginResponse>()!!

  private var MainFiFOlist = arrayListOf<DLSalesOrderItemWarehouseMapForFiFO>()
  private var MainNegativeStocklist = arrayListOf<DLSalesOrderItemWarehouseMapForNegativeStock>()
  private var MainItemdiscount = arrayListOf<DLSalesOrderWithItemCreationsDiscount>()


  override fun onInitialized(): Unit {
    viewModel.navArguments = intent.extras?.getBundle("bundle")
    so_number=intent.getStringExtra("SONUMBER")!!
    Log.e("SONUMBERCheck ", so_number.toString())
    viewModel.notificationApprovalModel.value?.txtLanguage= "Order number:  ${so_number.toString()}"

    val recyclerHorizontalLines1Adapter = NotificationFIFOSkippedAdapter(viewModel.recyclerFifoList.value?:mutableListOf())
    binding.recyclerHorizontalLines1.adapter = recyclerHorizontalLines1Adapter
    recyclerHorizontalLines1Adapter.setOnItemClickListener(object : NotificationFIFOSkippedAdapter.OnItemClickListener {
      override fun onItemClick(view:View, position:Int, item : NotificationApprovalRowModel) {
        onClickRecyclerHorizontalLines(view, position, item)
      }
    }
    )
    viewModel.recyclerFifoList.observe(this) {
      recyclerHorizontalLines1Adapter.updateData(it)
    }


    val recyclerNegitiveStockAdapter = NotificationNegativestockAdapter(viewModel.recyclernegativestockList.value?:mutableListOf())
    binding.recyclerHorizontalLines.adapter = recyclerNegitiveStockAdapter
    recyclerNegitiveStockAdapter.setOnItemClickListener(object : NotificationNegativestockAdapter.OnItemClickListener {
      override fun onItemClick(view: View, position: Int, item: NotificationApprovalRowModel) {
        onClickRecyclerHorizontalLines1(view, position, item)
      }
    }
    )
    viewModel.recyclernegativestockList.observe(this) {
      recyclerNegitiveStockAdapter.updateData(it)
    }

    val recyclerHorizontalLines2Adapter = NotificationItemdiscountAdapter(viewModel.recyclerdiscountitemList.value?:mutableListOf())
    binding.recyclerHorizontalLines2.adapter = recyclerHorizontalLines2Adapter
    recyclerHorizontalLines2Adapter.setOnItemClickListener(object : NotificationItemdiscountAdapter.OnItemClickListener {
      override fun onItemClick(view:View, position:Int, item : NotificationApprovalRowModel) {
        onClickRecyclerHorizontalLines2(view, position, item)
      }
    }
    )
    viewModel.recyclerdiscountitemList.observe(this) {
      recyclerHorizontalLines2Adapter.updateData(it)
    }

    binding.notificationApprovalVM = viewModel
    this@NotificationApprovalActivity.hideKeyboard()
    viewModel.notification(so_number!!)

    mnotificationapprovevalue = viewModel.notificationApprovalModel.value!!
  }

  override fun setUpClicks(): Unit {

    binding.orderchangeamt.addTextChangedListener { orderdiscountamt = binding.orderchangeamt.text.toString() }
    binding.newcreditlmt.addTextChangedListener { newcreditlimit = binding.newcreditlmt.text.toString()}
    binding.newcreditdays.addTextChangedListener { newcreditdays = binding.newcreditdays.text.toString() }
    binding.newoutstandinngbbill.addTextChangedListener { newoutstandingbbill = binding.newoutstandinngbbill.text.toString() }
//    binding.orderchangeamt.addTextChangedListener { Orderchangeamt = binding.orderchangeamt.text.toString() }

    binding.backnotificationL.setOnClickListener { super.onBackPressed() }


    //APPROVE
    binding.approve.setOnClickListener {

      if (saveresponse_isDirectSO){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ALERT")
        builder.setMessage("This order goes as partially approved, So further after changes approval is needed for further process. Do you want to continue? " )

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
          Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
          Toast.makeText(applicationContext,
            android.R.string.no, Toast.LENGTH_SHORT).show()
        }

        builder.show()
      }

//      Checkbox cheched or not handling condition in order discount
//      if (!binding.chxCreditlmt.isChecked || !binding.chxCreditdays.isChecked || !binding.chxOutstandingbills.isChecked){
      if(!validatecustomerdetails()){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ALERT")
        builder.setMessage("If Credit limit is not approved then order goes as rejected. Do you want to continue? " )

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
          Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
          sendNotificationApproval("CompleteReject")
        }
        builder.setNegativeButton(android.R.string.no) { dialog, which ->
          Toast.makeText(applicationContext,
            android.R.string.no, Toast.LENGTH_SHORT).show()
        }
        builder.show()
      }

      else{
        sendNotificationApproval("Approve")
      }
    }

    //REJECT
    binding.reject.setOnClickListener {
      if (saveresponse_isDirectSO){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ALERT")
        builder.setMessage("This order goes as partially approved, So further after changes approval is needed for further process. Do you want to continue? " )

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
          Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
          Toast.makeText(applicationContext,
            android.R.string.no, Toast.LENGTH_SHORT).show()
        }

        builder.show()
      }

      //      Checkbox cheched or not handling condition in order discount
      if (!validatecustomerdetails()){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ALERT")
        builder.setMessage("If Credit limit is not approved then order goes as rejected. Do you want to continue? " )

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
          Toast.makeText(
            applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT
          ).show()

          if (binding.reasonofrejection.text.isNullOrEmpty()) {
            Toast.makeText(this,"Reason of rejection is compusory",Toast.LENGTH_SHORT).show()
          } else {
            sendNotificationApproval("CompleteReject")

          }
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
          Toast.makeText(applicationContext,
            android.R.string.no, Toast.LENGTH_SHORT).show()

          val builder = AlertDialog.Builder(this)
          builder.setTitle("ALERT")
          builder.setMessage("Are you sure to reject these items details from SO?. Do you want to continue? " )

          builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            Toast.makeText(applicationContext,
              android.R.string.yes, Toast.LENGTH_SHORT).show()
            sendNotificationApproval("SOItemReject")
          }

          builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(applicationContext,
              android.R.string.no, Toast.LENGTH_SHORT).show()
          }
        }
        builder.show()
      }
      else{
        if (binding.reasonofrejection.text.isNullOrEmpty()) {
          Toast.makeText(this,"Reason of rejection is compusory",Toast.LENGTH_SHORT).show()
        } else {
          sendNotificationApproval("CompleteReject")
        }
      }
    }



    //DRAFT
    binding.draft.setOnClickListener {

//      isDirectSO conditioin handling
      if (saveresponse_isDirectSO){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ALERT")
        builder.setMessage("This order goes as partially approved, So further after changes approval is needed for further process. Do you want to continue? " )

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
          Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
          Toast.makeText(applicationContext,
            android.R.string.no, Toast.LENGTH_SHORT).show()
        }

        builder.show()
      }


//      Checkbox cheched or not handling condition in order discount
      if (!validatecustomerdetails()){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ALERT")
        builder.setMessage("If Credit limit is not approved then order goes as rejected. Do you want to continue? " )

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
          Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
          sendNotificationApproval("Draft")
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
          Toast.makeText(applicationContext,
            android.R.string.no, Toast.LENGTH_SHORT).show()
        }
        builder.show()
      }
      else{
        sendNotificationApproval("Draft")
      }
    }
  }


  private fun sendNotificationApproval(status:String){

    val MainFiFOlist = arrayListOf<DLSalesOrderItemWarehouseMapForFiFO>()
    val MainNegativeStocklist = arrayListOf<DLSalesOrderItemWarehouseMapForNegativeStock>()
    val MainItemdiscount = arrayListOf<DLSalesOrderWithItemCreationsDiscount>()

    viewModel.recyclerFifoList.value?.forEach {
      if(it.isFifoApproved!!)
        MainFiFOlist.add(it.toDLSalesOrderItemWarehouseMapForFiFO())
    }

    viewModel.recyclernegativestockList.value?.forEach {
      if (it.isNegitiveApproved!!) {
        MainNegativeStocklist.add(it.toDLSalesOrderItemWarehouseMapForNegativeStock())
      }
    }

    viewModel.recyclerdiscountitemList.value?.forEach {
      if (it.isDiscountApproved!!)
        MainItemdiscount.add(it.toDLSalesOrderWithItemCreationsDiscount())
    }

    val sendobjectApproval = NotificationApproveObject(
      isCustomerCrdExceeded = mnotificationapprovevalue.isCreditLimitExceeded,
      custID = mnotificationapprovevalue.custID!!,
      crLimit = (mnotificationapprovevalue.oldcreditlmt?:0).toString(),
      crDays = (mnotificationapprovevalue.oldcreditdays?:0).toString(),
      noofOutstandingBill = (mnotificationapprovevalue.oldoutstandingbill?:0).toString(),
      modifiedByID = user.userID.toString(),
      salesorderNumber = so_number!! ,
      orderDiscountApprove = mnotificationapprovevalue.IsOrderDiscountRangeExceeded,
      newCrdLimit = newcreditlimit,
      newCrdDays = newcreditdays,
      newCrdBills = newoutstandingbbill,
      isItemDiscountExceeded = mnotificationapprovevalue.IsItemDiscountExceeded,
      isOrderDiscountExceeded = mnotificationapprovevalue.IsOrderDiscountRangeExceeded,
      orderDiscountAmt = orderdiscountamt.toString(),
      isNegativeStockTaken = mnotificationapprovevalue.IsNegitiveStockTaken!!,
      isFifoskipped = mnotificationapprovevalue.IsFifoSkipped!!,

      dLSalesOrderItemWarehouseMapForFiFO = MainFiFOlist,
      dLSalesOrderItemWarehouseMapForNegativeStock = MainNegativeStocklist,
      dLSalesOrderWithItemCreationsDiscounts = MainItemdiscount
    )

    var gson = Gson()
    Log.e("SendobjectApproval... ", gson.toJson(sendobjectApproval))

    viewModel.notificationapprove(sendobjectApproval,status)

  }

  private fun validatecustomerdetails(): Boolean {
    var credit=binding.chxCreditlmt.isChecked == viewModel.notificationApprovalModel.value?.isCreditLimitExceeded!!
    var days=binding.chxCreditdays.isChecked == viewModel.notificationApprovalModel.value?.isCreditDaysExceeded!!
    var outstanding=binding.chxOutstandingbills.isChecked == viewModel.notificationApprovalModel.value?.isBillsExceeded!!
    return credit && days && outstanding
  }

  fun onClickRecyclerHorizontalLines(view: View, position: Int, item: NotificationApprovalRowModel): Unit {
  }

  fun onClickRecyclerHorizontalLines1(view: View, position: Int, item: NotificationApprovalRowModel): Unit {
    var changeqty = view as EditText
    qty = changeqty.text.trim().toString()
    if (qty!!.isNotEmpty()) {
      if (qty!!.toIntOrNull()!! > item.txtqtyordered?.toIntOrNull()!!) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ALERT")
        builder.setMessage("Qty can not be more than negative qty ")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
          Toast.makeText(applicationContext, android.R.string.yes, Toast.LENGTH_SHORT).show()
        }
        builder.show()

        changeqty.setText("0")
      }else
        item.changeQty=qty
    }
  }

  fun onClickRecyclerHorizontalLines2(view: View, position: Int, item: NotificationApprovalRowModel): Unit {
  }

  override fun addObservers(): Unit {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@NotificationApprovalActivity) {
      if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@NotificationApprovalActivity.showProgressDialog()
      } else {
        progressDialog?.dismiss()
      }
    }
    viewModel.fetch0522SMU010513LiveData.observe(this@NotificationApprovalActivity) {
      if(it is SuccessResponse) {
        val response = it.getContentIfNotHandled()
        onSuccessFetch0522SMU010513(it)
      } else if(it is ErrorResponse) {
        onErrorFetch0522SMU010513(it.data ?: Exception())
      }
    }
    viewModel.notificationapprovalLiveData.observe(this@NotificationApprovalActivity){
      if(it is SuccessResponse) {
        val response = it.getContentIfNotHandled()
        Toast.makeText(this,"Saved Response",Toast.LENGTH_SHORT).show()
        super.onBackPressed()
      } else if(it is ErrorResponse) {
        onErrorFetch0522SMU010513(it.data ?: Exception())
      }
    }
  }

  private fun onSuccessFetch0522SMU010513(response: SuccessResponse<NotificationVerifySOListResponse>): Unit {
    Snackbar.make(binding.root, MyApp.getInstance().getString(R.string.lbl_success), Snackbar.LENGTH_LONG).show()
    viewModel.bindFetch0522SMU010513Response(response.data)

    saveresponse_isDirectSO = response.data.salesOrderDetails?.isDirectSO!!

    binding.newcreditlmt.isEnabled = viewModel.notificationApprovalModel.value?.isCreditLimitExceeded!!
    binding.newcreditdays.isEnabled = viewModel.notificationApprovalModel.value?.isCreditDaysExceeded!!
    binding.newoutstandinngbbill.isEnabled = viewModel.notificationApprovalModel.value?.isBillsExceeded!!

    binding.chxCreditlmt.isChecked = viewModel.notificationApprovalModel.value?.isCreditLimitExceeded!!
    binding.chxCreditdays.isChecked = viewModel.notificationApprovalModel.value?.isCreditDaysExceeded!!
    binding.chxOutstandingbills.isChecked = viewModel.notificationApprovalModel.value?.isBillsExceeded!!

    ConfigureLayouts()
  }

  private fun ConfigureLayouts() {
    if (!viewModel.notificationApprovalModel.value?.IsFifoSkipped!!){
      binding.txtFIFOSKIPPEDLayout.visibility = View.GONE
      binding.constraintHeadline.visibility = View.GONE
      binding.recyclerHorizontalLines1.visibility = View.GONE
    }
    if (!viewModel.notificationApprovalModel.value?.IsNegitiveStockTaken!!){
      binding.txtNegativeStock.visibility = View.GONE
      binding.constraintGroup11.visibility = View.GONE
      binding.recyclerHorizontalLines.visibility = View.GONE
    }
    if (!viewModel.notificationApprovalModel.value?.IsItemDiscountExceeded!!){
      binding.txtItemdiscount.visibility = View.GONE
      binding.constraintGroup6.visibility = View.GONE
      binding.recyclerHorizontalLines2.visibility = View.GONE
    }
    if (!viewModel.notificationApprovalModel.value?.IsOrderDiscountRangeExceeded!!){
      binding.txtOrderdiscount.visibility = View.GONE
      binding.orderdiscountlayout.visibility = View.GONE
    }
    if (!viewModel.notificationApprovalModel.value?.isCreditLimitExceeded!! && !viewModel.notificationApprovalModel.value?.isBillsExceeded!! && !viewModel.notificationApprovalModel.value?.isCreditDaysExceeded!!) {
      binding.LCreditLimitAmount.visibility = View.GONE
      binding.LCreditDays.visibility = View.GONE
      binding.LNumberOfOutstandingBills.visibility = View.GONE
      binding.txtCustomercredit.visibility = View.GONE
    }
  }

  private fun onErrorFetch0522SMU010513(exception: Exception): Unit {
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
    const val TAG: String = "NOTIFICATION_APPROVAL_ACTIVITY"

    fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, NotificationApprovalActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }
}
