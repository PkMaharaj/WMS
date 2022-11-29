package com.wms.superadmin.modules.stocksummary.sritem.ui

import StockReportAdapter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseFragment
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.databinding.FragmentSrmonthwiseBinding
import com.wms.superadmin.databinding.FragmentSrwarehouseBinding
import com.wms.superadmin.extensions.*
import com.wms.superadmin.modules.stocksummary.sritem.data.model.StockReportRowModel
import com.wms.superadmin.modules.stocksummary.sritem.data.viewmodel.SritemVM
import com.wms.superadmin.modules.stocksummary.stockreport.ui.StockreportActivity
import com.wms.superadmin.network.models.stocksummary.StockReportRequest
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import org.json.JSONObject
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class SrMonthFragment  : BaseFragment<FragmentSrmonthwiseBinding>(R.layout.fragment_srmonthwise),
    StockreportActivity.OnActivityListener{
    private val viewModel: SritemVM by viewModels<SritemVM>()
    private lateinit var mActivity: StockreportActivity
    private lateinit var mPreviousFragment: Fragment
    private var mStockReportRequest= StockReportRequest()
    private lateinit var mStockReportAdapter:StockReportAdapter
    private var mItemName=""
    override fun onInitialized(): Unit {
        viewModel.navArguments = arguments
        mStockReportRequest=viewModel.navArguments?.getSerializable(IntentParams.STOCKREPORT_REQUEST) as StockReportRequest
        mStockReportAdapter = StockReportAdapter(viewModel.stockReportList.value?:mutableListOf(), ScreenNames.SR_MONTHWISE)
        mItemName="Item: "+viewModel.navArguments?.getString(IntentParams.ITEM_NAME,"")
        binding.itemName.text=mItemName
        binding.srRecyclerview.adapter = mStockReportAdapter
        mStockReportAdapter.setOnItemClickListener(
            object : StockReportAdapter.OnItemClickListener {
                override fun onItemClick(view: View, position:Int, item : StockReportRowModel) {
                    onClickRecyclerListinward(view, position, item)
                }
            })
        viewModel.stockReportList.observe(requireActivity()) {
            mStockReportAdapter.updateData(it as ArrayList<StockReportRowModel>)
        }
        getStockReport()

        binding.sritemVM = viewModel
        mActivity.setScreenAction(false,false,false,true,false)
    }
    override fun setUpClicks(): Unit {

    }

    fun onClickRecyclerListinward(view: View, position: Int, item: StockReportRowModel): Unit {
        val year = item.MonthName?.split("-")?.get(1)?.toIntOrNull()?:0
        val month = item.MonthID?:0
        val fromdate=String.format("%d-%02d-%02d", year,month,1)
        var convertedDate = LocalDate.parse(fromdate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        convertedDate= convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));
        mStockReportRequest.fromDate=fromdate
        mActivity.mInwardRate=item.InwardQTYRate?.toString()?:"0"
        mStockReportRequest.toDate=convertedDate.toString()
        mStockReportRequest.inwardrate=item.InwardQTYRate?.toString()?:"0"
        mActivity.binding.txtfromdate.text=fromdate
        mActivity.binding.txtTodate.text=convertedDate.toString()

        var bundle= Bundle()
        bundle.putSerializable(IntentParams.STOCKREPORT_REQUEST,mStockReportRequest)
        bundle.putString(IntentParams.ITEM_NAME,mItemName)
        val destFragment = SrVoucherFragment.getInstance(bundle,mActivity,this)
        requireActivity().loadFragment(
            R.id.fragmentContainer,
            destFragment,
            bundle = null,
            tag = SrVoucherFragment.TAG,
            addToBackStack = true,
            add = true,
            enter = null,
            exit = null,
        )
    }
    fun getStockReport(){
        mStockReportRequest.screenName= ScreenNames.SR_MONTHWISE
        viewModel.onClickOnCreate(mStockReportRequest)
    }

    override fun onDetach() {
        super.onDetach()
        mActivity.setScreenAction(false,false,false,false,false)
         mActivity.onAttachFragment(mPreviousFragment)
    }

    override fun addObservers() {
        var progressDialog : AlertDialog? = null
        viewModel.progressLiveData.observe(requireActivity()) {
            if(it) {
                progressDialog?.dismiss()
                progressDialog = null
                progressDialog = requireActivity().showProgressDialog()
            } else  {
                progressDialog?.dismiss()
            }
        }
        viewModel.stockReportListLiveData.observe(requireActivity()) {
            if(it is SuccessResponse) {
                val response = it.getContentIfNotHandled()
                if(it.data.StockReportitemList?.isNotEmpty()!!){
                    mStockReportAdapter.updateMonthwise(mStockReportRequest.ismonthcheckbox!!)
                    viewModel.bindStockReportList(it.data.StockReportitemList?: arrayListOf())
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
        const val TAG: String = "SR_MONTH_FRAGMENT"
        fun getInstance(bundle: Bundle?, activity: StockreportActivity,prious:Fragment): SrMonthFragment {
            val fragment = SrMonthFragment()
            fragment.mActivity=activity
            fragment.mPreviousFragment=prious
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun <T> onActionChange(`object`: T) {
        if(`object` is Boolean){
            if(`object`)
                Log.e("ActionChange","MonthWise:")
            mActivity.onBackPressed()
        }

        else if( `object` is StockReportRequest) {
            mStockReportRequest = `object` as StockReportRequest
            getStockReport()
        }
    }

    override fun <T> onSearchTextChange(searchtext: String) {
        if(this :: mStockReportAdapter.isInitialized )
            mStockReportAdapter.filter.filter(searchtext)
    }
}