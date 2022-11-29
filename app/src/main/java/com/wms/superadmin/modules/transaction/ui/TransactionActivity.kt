package com.wms.superadmin.modules.transaction.ui

import android.app.Dialog
import android.app.assist.AssistContent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.appcomponents.views.DatePickerFragment
import com.wms.superadmin.databinding.ActivityTransactionBinding
import com.wms.superadmin.extensions.*
import com.wms.superadmin.modules.transaction.data.model.TransactionRowModel
import com.wms.superadmin.modules.transaction.data.model.HorizontallinesRowModel
import com.wms.superadmin.modules.transaction.data.viewmodel.TransactionVM
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.createallbranchreceivables.AgingRequestItem
import com.wms.superadmin.network.models.createallbranchreceivables.CreateAllBranchReceivablesResponse
import com.wms.superadmin.network.models.createallbranchreceivables.TransactionRequest
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import org.json.JSONObject
import org.koin.android.ext.android.inject
import retrofit2.HttpException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.ArrayList

class TransactionActivity : BaseActivity<ActivityTransactionBinding>(R.layout.activity_transaction)
        {
    private val viewModel: TransactionVM by viewModels<TransactionVM>()

            private var mFromDateVal= Date()
            private var mToDateval= Date()
            private val prefs: PreferenceHelper by inject()
            private var mUser=prefs.getSADetails<LoginResponse>()
            var mFromDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(mFromDateVal)
            var mToDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(mToDateval)
            var mBranchId="0"
            var mOrgId=mUser?.orgID?.toIntOrNull()?:0
            var mReportType:String?=""
            var mScreenName=""
            var mAgingBy=""
            var mAgingRequestItem: AgingRequestItem?= AgingRequestItem()
            var mIsAgingAdded=false
            private lateinit var branchrecyclerAdapter:TransactionAdapter

    override fun onInitialized() {
        viewModel.navArguments = intent.extras?.getBundle("bundle")
        mReportType=viewModel.navArguments?.getString(IntentParams.REPORT_TYPE,null)
        mFromDate=viewModel.navArguments?.getString(IntentParams.FROM_DATE,mFromDate)!!
        mToDate=viewModel.navArguments?.getString(IntentParams.To_DATE,mToDate)!!
        binding.txtfromdate.text=mFromDate
        binding.txtTodate.text=mToDate
        mReportType=viewModel.navArguments?.getString(IntentParams.REPORT_TYPE,null)
        mScreenName=ScreenNames.TXN_ALL_BRANCH
        binding.txtTransactionHeader.text=if(mReportType==ReportTypes.RECEIVABLES)"Receivables" else "Payables"
         branchrecyclerAdapter = TransactionAdapter(viewModel.transactionList.value?:mutableListOf())
        binding.recyclerBranchrecycler.adapter = branchrecyclerAdapter
        branchrecyclerAdapter.setOnItemClickListener(
        object : TransactionAdapter.OnItemClickListener {
            override fun onItemClick(view:View, position:Int, item : TransactionRowModel) {
                onClickRecyclerBranchrecycler(view, position, item)
            }
        })
        viewModel.transactionList.observe(this) {
            branchrecyclerAdapter.updateData(it as ArrayList<TransactionRowModel>)
        }

        binding.transactionVM = viewModel
        setUpSearchViewSearchBArListener()
        getTransactionList()

    }

    override fun setUpClicks() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.txtfromdate.setOnClickListener {
            val destinationInstance = DatePickerFragment.getInstance()
            destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
                mFromDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
                mFromDateVal = selectedDate
                binding.txtfromdate.text=mFromDate
                if(mFromDateVal<=mToDateval)
                    getTransactionList()
                else
                    Toast.makeText(this,"Invalid dates",Toast.LENGTH_SHORT).show()

            }
        }
        binding.txtTodate.setOnClickListener {
            val destinationInstance = DatePickerFragment.getInstance()
            destinationInstance.show(this.supportFragmentManager, DatePickerFragment.TAG) { selectedDate ->
                mToDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate)
                mToDateval = selectedDate
                binding.txtTodate.text=mToDate
                if(mFromDateVal<=mToDateval)
                    getTransactionList()
                else
                    Toast.makeText(this,"Invalid dates",Toast.LENGTH_SHORT).show()

            }
        }
        binding.chxAgeing.setOnClickListener {
            it as CheckBox
            mIsAgingAdded=it.isChecked
            if(mIsAgingAdded)
                openagingDialog()
            else
            {
                mAgingRequestItem= AgingRequestItem()
                mAgingBy=""
                mScreenName=ScreenNames.TXN_ALL_BRANCH
                getTransactionList()
            }

        }
        binding.txtageingvalue.setOnClickListener {
            if(mIsAgingAdded)
                openagingDialog()
        }
    }

    fun onClickRecyclerBranchrecycler(view: View, position: Int, item: TransactionRowModel) {
        when(view.id){
            R.id.main_layout->{
              val mScreenName=if( mAgingRequestItem?.isAgewiseSelected!!)ScreenNames.TXN_VOUCHER_AGING else ScreenNames.TXN_VOUCHER
                val request=TransactionRequest(orgID = mOrgId,agewiseProperties = mAgingRequestItem,
                    screenName = mScreenName,fromDate = mFromDate,toDate = mToDate,branchID = item.BranchId,reportType = mReportType)
                val bundle=Bundle()
                bundle.putSerializable(IntentParams.TRANSACTION_REQUEST,request)
                val intent=BranchTXNActivity.getIntent(this,bundle)
                startActivity(intent)
            }
        }
    }

   fun getTransactionList(){
     var request=TransactionRequest(orgID = mOrgId,agewiseProperties = mAgingRequestItem,
         screenName = mScreenName,fromDate = mFromDate,toDate = mToDate,branchID = mBranchId,reportType = mReportType)
       viewModel.onClickOnCreate(request)
   }

            private fun openagingDialog(){
                val  mDialogAging = Dialog(this)
                mDialogAging.requestWindowFeature(Window.FEATURE_NO_TITLE)
                mDialogAging.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val window: Window? = mDialogAging!!.window
                val wlp = window!!.attributes
                wlp.gravity = Gravity.CENTER
                mDialogAging.setContentView(R.layout.dialog_aging)
                val  aging1= mDialogAging.findViewById(R.id.Aging1) as EditText
                val   aging2= mDialogAging.findViewById(R.id.Aging2) as EditText
                val  cancel= mDialogAging.findViewById(R.id.cancel) as ImageView
                val submit= mDialogAging.findViewById(R.id.submit_aging) as Button
                val radioGroup=mDialogAging.findViewById(R.id.radio_group) as RadioGroup
                val by_dueRadio=mDialogAging.findViewById(R.id.by_due) as RadioButton
                val by_billRadio=mDialogAging.findViewById(R.id.by_bill) as RadioButton
                var option1=false
                var option2=false
                if(mAgingRequestItem?.toDays1!=0 && mAgingRequestItem?.toDays2!=0 )
                {
                    aging1.setText(mAgingRequestItem?.toDays1.toString())
                    aging2.setText(mAgingRequestItem?.toDays2.toString())
                    by_billRadio.isChecked=mAgingRequestItem?.billByDate?:false
                    by_dueRadio.isChecked=mAgingRequestItem?.billByDueDate?:false
                }
                submit.setOnClickListener {
                    when(radioGroup!!.checkedRadioButtonId){
                        R.id.by_bill->{
                            mAgingRequestItem?.billByDate=true
                            mAgingRequestItem?.billByDueDate=false
                            option1=true
                            option2=false
                            mAgingBy=MyApp.getInstance().resources.getString(R.string.lbl_by_bill)
                        }
                        R.id.by_due->{
                            mAgingRequestItem?.billByDate=false
                            mAgingRequestItem?.billByDueDate=true
                            option1=false
                            option2=true
                            mAgingBy=MyApp.getInstance().resources.getString(R.string.lbl_by_due)
                        }
                    }
                    var day1=aging1.text.trim().toString().toIntOrNull()?:0
                    var day2=aging2.text.trim().toString().toIntOrNull()?:0
                    if(day2>day1){
                        if(option1||option2) {
                            mAgingRequestItem?.toDays1 = day1
                            mAgingRequestItem?.fromDays2 = day1
                            mScreenName = ScreenNames.TXN_ALL_BRANCH_AGING
                            mAgingRequestItem?.toDays2 = day2
                            mIsAgingAdded = true
                            mAgingRequestItem?.isAgewiseSelected = true
                            getTransactionList()
                            mDialogAging.dismiss()
                        }else
                            Toast.makeText(this,"Please select one of the Option for aging",Toast.LENGTH_SHORT).show()

                    }else{
                        mIsAgingAdded=false
                        mScreenName=ScreenNames.TXN_ALL_BRANCH
                        Toast.makeText(this,"Please enter valid days",Toast.LENGTH_SHORT).show()
                    }
                }
                cancel.setOnClickListener {
                    binding.chxAgeing.isChecked=mAgingRequestItem?.isAgewiseSelected?:false
                    mDialogAging.dismiss()
                }

                mDialogAging.setCancelable(false)
                mDialogAging.show()
            }

    private fun setUpSearchViewSearchBArListener() {
        binding.searchViewSearchBAr.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0 : String) : Boolean {
                // Performs search when user hit
                // the search button on the keyboard
                return false
            }
            override fun onQueryTextChange(p0 : String) : Boolean {
                branchrecyclerAdapter.filter.filter(p0)
                return false
            }
            })
        }


            override fun addObservers() {
                var progressDialog : AlertDialog? = null
                viewModel.progressLiveData.observe(this@TransactionActivity) {
                    if(it) {
                        progressDialog?.dismiss()
                        progressDialog = null
                        progressDialog = this@TransactionActivity.showProgressDialog()
                    } else  {
                        progressDialog?.dismiss()
                    }
                }
                viewModel.createAllBranchReceivablesLiveData.observe(this@TransactionActivity) {
                    if(it is SuccessResponse) {
                        val response = it.getContentIfNotHandled()
                        if(it.data.outputDataListObject?.isNotEmpty()!!){
                            var TransType=if(mReportType==ReportTypes.RECEIVABLES)"Dr" else "Cr"
                            viewModel.createAllBranchReceivables(it.data,mAgingRequestItem?:AgingRequestItem(),TransType)
                            branchrecyclerAdapter.showAgingDetails(mIsAgingAdded)
                            binding.txtageingvalue.text="$mAgingBy${mAgingRequestItem?.fromDays1} - ${mAgingRequestItem?.toDays1} To ${mAgingRequestItem?.fromDays2} - ${mAgingRequestItem?.toDays2}"
                        }

                    } else if(it is ErrorResponse)  {
                        onError(it.data ?: Exception())
                    }
                }
            }
            private fun onError(exception: Exception) {
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
                const val TAG: String = "TRANSACTION_ACTIVITY"
                fun getIntent(context: Context, bundle: Bundle?): Intent {
                    val destIntent = Intent(context, TransactionActivity::class.java)
                    destIntent.putExtra("bundle", bundle)
                    return destIntent
                }
            }
    }
