package com.wms.superadmin.modules.stocksummary.sritem.data.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.modules.stocksummary.sritem.data.model.SritemModel
import com.wms.superadmin.modules.stocksummary.sritem.data.model.StockReportRowModel
import com.wms.superadmin.network.models.createallbranchreceivables.CreateAllBranchReceivablesResponse
import com.wms.superadmin.network.models.createallbranchreceivables.TransactionRequest
import com.wms.superadmin.network.models.stocksummary.STockRowObject
import com.wms.superadmin.network.models.stocksummary.StockReportRequest
import com.wms.superadmin.network.models.stocksummary.StockReportResponse
import com.wms.superadmin.network.models.stocksummary.torowMdel
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.text.DecimalFormat

class SritemVM : ViewModel(), KoinComponent {
  val sritemModel: MutableLiveData<SritemModel> = MutableLiveData(SritemModel())

  var navArguments: Bundle? = null
    val  stockReportListLiveData: MutableLiveData<Response<StockReportResponse>> =
        MutableLiveData<Response<StockReportResponse>>()



  val stockReportList: MutableLiveData<MutableList<StockReportRowModel>> =
      MutableLiveData(mutableListOf())
    val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    private val networkRepository: NetworkRepository by inject()
    private val prefs: PreferenceHelper by inject()
    val Format = DecimalFormat("##.00")

    fun onClickOnCreate(request: StockReportRequest) {
        viewModelScope.launch {
            progressLiveData.postValue(true)
            stockReportListLiveData.postValue(
                networkRepository.fecthStockReport(authorization = "bearer ${prefs.getAccess_token()}",request))
            progressLiveData.postValue(false)
        }
    }

    fun bindStockReportList(stockreportlist:ArrayList<STockRowObject>){
        val stockreportrowmodelvalue=sritemModel.value?: SritemModel()
        val stockreportlistval=stockreportlist.map {
            it.torowMdel()
        }.toMutableList()
        stockReportList.value=stockreportlistval
        sritemModel.value=stockreportrowmodelvalue

    }
}
