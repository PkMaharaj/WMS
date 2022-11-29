package com.wms.superadmin.modules.vocherbankbook.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.appcomponents.views.DatePickerFragment
import com.wms.superadmin.databinding.ActivityVocherbankbookBinding
import com.wms.superadmin.extensions.NoInternetConnection
import com.wms.superadmin.extensions.hideKeyboard
import com.wms.superadmin.extensions.isJSONObject
import com.wms.superadmin.extensions.showProgressDialog
import com.wms.superadmin.modules.bankbook.data.model.Bankbook1RowModel
import com.wms.superadmin.modules.salesorderbranch.ui.RecyclerSalesOrderAdapter
import com.wms.superadmin.modules.vocherbankbook.`data`.model.Vocherbankbook1RowModel
import com.wms.superadmin.modules.vocherbankbook.`data`.viewmodel.VocherbankbookVM
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.bankbook.BankBookRequest
import com.wms.superadmin.network.models.bankbook.BankBookResponse
import com.wms.superadmin.network.models.bankbook.LedgerItem
import com.wms.superadmin.network.models.bankbook.VoucherRequest
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

public class VocherbankbookActivity :
    BaseActivity<ActivityVocherbankbookBinding>(R.layout.activity_vocherbankbook) {
  private val viewModel: VocherbankbookVM by viewModels<VocherbankbookVM>()
  private val prefs: PreferenceHelper by inject()
    var fromDate=""
    var toDate=""
  var reportType=prefs.getBookType()
    private lateinit var intentrequest:VoucherRequest
    var superAdminDetails=prefs.getSADetails<LoginResponse>()
    var branch:String="0"
    var branchname=""
    var screenName="Voucher"
    var from_date_Bank : String = "From_date"
    var to_date_Bank : String = "To_date"

  public override fun onInitialized(): Unit {
     intentrequest=intent.getSerializableExtra("Request")as VoucherRequest
      branchname=intent.getStringExtra("branch_name")!!
      binding.branchname.text = branchname
      fromDate=intentrequest.fromDate!!
      toDate=intentrequest.toDate!!
      binding.txtFromdate.text=fromDate
      binding.txtTodate.text=toDate
      binding.txtBankBook.text=reportType
    if(reportType.equals("CashBook"))
        binding.txtBankBook.text="Cash Book"
    else if(reportType.equals("BankBook"))
        binding.txtBankBook.text="Bank Book"

      branch=intentrequest.branchID!!
      viewModel.navArguments = intent.extras
    val recyclerVocherwisebooklistAdapter = RecyclerVocherwisebooklistAdapter(viewModel.recyclerVocherwisebooklistList.value?:mutableListOf())
    binding.vocherwisebooklist.adapter = recyclerVocherwisebooklistAdapter
    recyclerVocherwisebooklistAdapter.setOnItemClickListener(
    object : RecyclerVocherwisebooklistAdapter.OnItemClickListener {
      override fun onItemClick(view:View, position:Int, item : Vocherbankbook1RowModel) {
        onClickRecyclerVocherwisebooklist(view, position, item)
      }
    }
    )
    viewModel.recyclerVocherwisebooklistList.observe(this) {
        if(it!=null)
            recyclerVocherwisebooklistAdapter.updateData(it)
        else{
            var emptylist= arrayListOf<Vocherbankbook1RowModel>()
            recyclerVocherwisebooklistAdapter.updateData(emptylist)
        }
    }
    binding.vocherbankbookVM = viewModel
    this@VocherbankbookActivity.hideKeyboard()
    viewModel.onClickOnCreate(intentrequest)
  }

  public override fun setUpClicks(): Unit {
      binding.txtFromdate.setOnClickListener {
          val destinationInstance = DatePickerFragment.getInstance()
          destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
              var SalesDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
              intentrequest.fromDate=fromDate
              binding.txtFromdate.text=SalesDatetime
              getBankBookList()
          }
      }
      binding.txtTodate.setOnClickListener {
          val destinationInstance = DatePickerFragment.getInstance()
          destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
              var SalesDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
              intentrequest.toDate=toDate
              binding.txtTodate.text=SalesDatetime
              getBankBookList()
          }
      }
      binding.imageBackImg.setOnClickListener { super.onBackPressed() }
  }

  public fun onClickRecyclerVocherwisebooklist(
    view: View,
    position: Int,
    item: Vocherbankbook1RowModel
  ): Unit {
    when(view.id) {
    }
  }

  public override fun addObservers(): Unit {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@VocherbankbookActivity) {
      if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@VocherbankbookActivity.showProgressDialog()
      } else  {
        progressDialog?.dismiss()
      }
    }
    viewModel.createBankAccountVoucherLiveData.observe(this@VocherbankbookActivity) {
      if(it is SuccessResponse) {
        val response = it.getContentIfNotHandled()
        onSuccessCreateBankAccountVoucher(it)
      } else if(it is ErrorResponse)  {
        onErrorCreateBankAccountVoucher(it.data ?:Exception())
      }
    }
  }

  private fun onSuccessCreateBankAccountVoucher(response: SuccessResponse<BankBookResponse>):
      Unit {
      if(response.data.status) {
          viewModel.bindVoucherResponse(response.data)
      }else
     Toast.makeText(this,"Bills Not available", Toast.LENGTH_SHORT).show()
  }

  private fun onErrorCreateBankAccountVoucher(exception: Exception): Unit {
    when(exception) {
      is NoInternetConnection -> {
        Snackbar.make(binding.root, exception.message?:"", Snackbar.LENGTH_LONG).show()
      }
      is HttpException -> {
        val errorBody = exception.response()?.errorBody()?.string()
        val errorObject = if (errorBody != null  && errorBody.isJSONObject()) JSONObject(errorBody)
            else JSONObject()
        Toast.makeText(this@VocherbankbookActivity,"error",Toast.LENGTH_LONG).show()
      }
    }
  }

    private fun getBankBookList() {
        var intentrequest = VoucherRequest(superAdminDetails!!.orgID, screenName, from_date_Bank, to_date_Bank, branch, reportType)
        if(fromDate>toDate)
            Toast.makeText(this,"invalid dates",Toast.LENGTH_SHORT).show()
        else
            viewModel.onClickOnCreate(intentrequest)
    }
  public companion object {
    public const val TAG: String = "VOCHERBANKBOOK_ACTIVITY"
    public fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, VocherbankbookActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }
}
