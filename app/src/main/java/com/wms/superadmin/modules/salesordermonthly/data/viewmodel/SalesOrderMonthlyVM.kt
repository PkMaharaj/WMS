package com.wms.superadmin.modules.salesordermonthly.data.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.extensions.ReportTypes
import com.wms.superadmin.modules.salesorderbranch.data.model.SalesOrderRowModel
import com.wms.superadmin.modules.salesordermonthly.data.model.SalesOrderMonthlyModel
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.createsalesorderreport.SalesOrderReportRequest
import com.wms.superadmin.network.models.createsalesorderreport.CreateSalesOrderReportResponse
import com.wms.superadmin.network.models.createsalesorderreport.SalesOrderReportResponselistItem
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlin.Boolean
import kotlin.Unit
import kotlin.collections.MutableList
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.math.BigDecimal
import java.math.RoundingMode

public class SalesOrderMonthlyVM : ViewModel(), KoinComponent {
  public val salesOrderMonthlyModel: MutableLiveData<SalesOrderMonthlyModel> =
    MutableLiveData(SalesOrderMonthlyModel())

  public var navArguments: Bundle? = null

  public val recyclerGroup177List: MutableLiveData<MutableList<SalesOrderRowModel>> =
    MutableLiveData(mutableListOf())

  public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

  private val networkRepository: NetworkRepository by inject()
  private val prefs: PreferenceHelper by inject()
  var SuperAdminDetails=prefs.getSADetails<LoginResponse>()

  public val createSalesOrderReportLiveData:
          MutableLiveData<Response<CreateSalesOrderReportResponse>> =
    MutableLiveData<Response<CreateSalesOrderReportResponse>>()

  public fun getSalesOrderMonthly(request: SalesOrderReportRequest, SalesorPurchase: String): Unit {
    viewModelScope.launch {
      progressLiveData.postValue(true)

      if (SalesorPurchase== ReportTypes.SALESORDER) {
        createSalesOrderReportLiveData.postValue(
          networkRepository.createSalesOrderReport(
            authorization = "bearer ${prefs.getAccess_token()}",
            salesOrderReportRequest = request
          )
        )
        progressLiveData.postValue(false)
      }
      else if (SalesorPurchase==ReportTypes.PURCHASEORDER){
        createSalesOrderReportLiveData.postValue(
          networkRepository.createPurchaseOrderReport(
            authorization = "bearer ${prefs.getAccess_token()}",
            salesOrderReportRequest = request
          )
        )
        progressLiveData.postValue(false)
      }
    }
  }

  public fun bindCreateSalesOrderReportResponse(response: ArrayList<SalesOrderReportResponselistItem?>?, issalesorpurchase: Boolean): Unit {
    val salesOrderMonthlyModelValue = salesOrderMonthlyModel.value ?: SalesOrderMonthlyModel()
    val recyclerGroup177 = response?.map {
      SalesOrderRowModel(
        txtMonth = it?.monthName.toString(),
        monthID = it?.monthID?:0,
        txtvouchers = it?.totalVouchersCount.toString(),
        totalvalue = BigDecimal(it?.totalvalue?:0.0).setScale(2, RoundingMode.HALF_EVEN).toString(),
        branchID = if(issalesorpurchase) it?.SO_branchID?.toIntOrNull()?:0 else  it?.PO_branchID?.toIntOrNull()?:0
      )
    }?.toMutableList()
    recyclerGroup177List.value = recyclerGroup177
    salesOrderMonthlyModelValue.txtvouchers = recyclerGroup177List.value?.sumOf { it?.txtvouchers?.toIntOrNull()?:0}.toString()

    val decimal = recyclerGroup177List.value?.sumOf { it?.totalvalue?.toDoubleOrNull()?:0.0 }.toString()
    salesOrderMonthlyModelValue.totalvalue = BigDecimal(decimal).setScale(2, RoundingMode.HALF_EVEN).toString()

    salesOrderMonthlyModel.value = salesOrderMonthlyModelValue
  }
}
