package com.wms.superadmin.modules.bankwisebook.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.appcomponents.views.DatePickerFragment
import com.wms.superadmin.databinding.ActivityBankwisebookBinding
import com.wms.superadmin.extensions.NoInternetConnection
import com.wms.superadmin.extensions.hideKeyboard
import com.wms.superadmin.extensions.isJSONObject
import com.wms.superadmin.extensions.showProgressDialog
import com.wms.superadmin.modules.bankwisebook.`data`.model.Bankwisebook1RowModel
import com.wms.superadmin.modules.bankwisebook.`data`.viewmodel.BankwisebookVM
import com.wms.superadmin.modules.vocherbankbook.ui.VocherbankbookActivity
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.bankbook.*
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

public class BankwisebookActivity : BaseActivity<ActivityBankwisebookBinding>(R.layout.activity_bankwisebook) {
  private val viewModel: BankwisebookVM by viewModels<BankwisebookVM>()
  private val prefs: PreferenceHelper by inject()
  var superAdminDetails=prefs.getSADetails<LoginResponse>()
  var from_date_Bank : String = "From_date"
  var to_date_Bank : String = "To_date"
  var screenName="Ledger"
  var reportType=prefs.getBookType()
  var branch:String="0"
  var branchname=""
   private var mGrouplist:List<GroupItem>?= listOf(GroupItem("0"))

  public override fun onInitialized(): Unit {
    var request=intent.getSerializableExtra("Request")as BankBookRequest
      mGrouplist=request.listGroupdata
    branchname=intent.getStringExtra("branch_name")!!

    binding.txtFromdate.text=request.fromDate
    from_date_Bank=request.fromDate!!

    to_date_Bank=request.toDate!!
    binding.txtTodate.text=request.toDate

    if(reportType.equals("CashBook")){
      binding.txttotalCash.text="Cash In Hand :"
      binding.txtBankBook.text="Cash Book"
      binding.txtBranchName!!.text = "Cash"
    }else{
            (reportType.equals("BankBook"))
             binding.txttotalCash.text="Cash In Bank :"
             binding.txtBankBook.text="Bank Book"
    }
      branch=request.branchID!!
      viewModel.bankwisebookModel.value!!.txtBranchname=branchname
      viewModel.onClickOnCreate(request)

    val recyclerBankwisebooklistAdapter = RecyclerBankwisebooklistAdapter(viewModel.recyclerBankwisebooklistList.value?:mutableListOf())
    binding.recyclerBankwisebooklist?.adapter = recyclerBankwisebooklistAdapter
    recyclerBankwisebooklistAdapter.setOnItemClickListener(
    object : RecyclerBankwisebooklistAdapter.OnItemClickListener {
      override fun onItemClick(view:View, position:Int, item : Bankwisebook1RowModel) {
        onClickRecyclerBankwisebooklist(view, position, item)
      }
    }
    )
    viewModel.recyclerBankwisebooklistList.observe(this) {
        if(it!=null)
      recyclerBankwisebooklistAdapter.updateData(it)
        else{
            Toast.makeText(this,"not Available",Toast.LENGTH_SHORT).show()
            var emptylist= arrayListOf<Bankwisebook1RowModel>()
            recyclerBankwisebooklistAdapter.updateData(emptylist)
        }
    }
    binding.bankwisebookVM = viewModel
    this@BankwisebookActivity.hideKeyboard()
  }
  public override fun setUpClicks(): Unit {
    binding.txtFromdate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        from_date_Bank = SalesDatetime
        binding.txtFromdate.text=SalesDatetime
        getBankBookList()
      }
    }

    binding.txtTodate.setOnClickListener {
      val destinationInstance = DatePickerFragment.getInstance()
      destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
        var SalesDatetime = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
        to_date_Bank = SalesDatetime
        binding.txtTodate.text=SalesDatetime
        getBankBookList()
      }
    }
          binding.imageBackImg.setOnClickListener { super.onBackPressed() }
  }

  public fun onClickRecyclerBankwisebooklist(view: View, position: Int, item: Bankwisebook1RowModel): Unit
  {
   var destIntent=VocherbankbookActivity.getIntent(this,null)
    var request = VoucherRequest(superAdminDetails!!.orgID,"Voucher",from_date_Bank,to_date_Bank,item.BranchId,reportType,
      listOf(LedgerItem(item.LedgerId)))
    destIntent.putExtra("Request",request)
    destIntent.putExtra("branch_name",branchname)
    destIntent.putExtra("from_date",from_date_Bank)
    Log.e("fromdate...",from_date_Bank)
    destIntent.putExtra("to_date",to_date_Bank)
    Log.e("todate...",from_date_Bank)
    startActivity(destIntent)
}
  public override fun addObservers(): Unit {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@BankwisebookActivity) {
      if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@BankwisebookActivity.showProgressDialog()
      } else  {
        progressDialog?.dismiss()
      }
    }
    viewModel.createBankAccountLiveData.observe(this@BankwisebookActivity) {
      if(it is SuccessResponse) {
        val response = it.getContentIfNotHandled()
        onSuccessCreateBankAccount(it)
      } else if(it is ErrorResponse)
      {
        onErrorCreateBankAccount(it.data ?:Exception())
      }
    }
  }

  private fun onSuccessCreateBankAccount(response: SuccessResponse<BankBookResponse>): Unit {
    if (response.data.status)
        viewModel.bindBankBookResponse(response.data)
       else
        Toast.makeText(this, "Bills Not available", Toast.LENGTH_SHORT).show()
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
        Toast.makeText(this@BankwisebookActivity,"error",Toast.LENGTH_LONG).show()
      }
    }
  }
  private fun getBankBookList() {
      if(from_date_Bank>to_date_Bank)
          Toast.makeText(this,"invalid dates",Toast.LENGTH_SHORT).show()
      else {
          val request = BankBookRequest(superAdminDetails!!.orgID, screenName, from_date_Bank, to_date_Bank, branch, reportType, mGrouplist)
          viewModel.onClickOnCreate(request)
      }
  }

  public companion object {
    public const val TAG: String = "BANKWISEBOOK_ACTIVITY"
    public fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, BankwisebookActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }
}
