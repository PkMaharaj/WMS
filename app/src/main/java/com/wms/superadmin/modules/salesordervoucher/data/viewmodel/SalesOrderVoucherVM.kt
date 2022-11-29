package com.wms.superadmin.modules.salesordervoucher.data.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.extensions.ReportTypes
import com.wms.superadmin.modules.salesorderbranch.data.model.SalesOrderRowModel
import com.wms.superadmin.modules.salesordervoucher.data.model.SalesOrderVoucherModel
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

public class SalesOrderVoucherVM : ViewModel(), KoinComponent {
  public val salesOrderVoucherModel: MutableLiveData<SalesOrderVoucherModel> =
    MutableLiveData(SalesOrderVoucherModel())

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

  public fun getSalesOrderVoucher(request: SalesOrderReportRequest, SalesorPurchase: String): Unit {
    viewModelScope.launch {
      progressLiveData.postValue(true)

      if (SalesorPurchase==ReportTypes.SALESORDER) {
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
    val salesOrderVoucherModelValue = salesOrderVoucherModel.value ?: SalesOrderVoucherModel()
    val recyclerSalesOrderVoucher = response?.map {
      SalesOrderRowModel(
        txtDate = if(issalesorpurchase) it?.salesDateInDateFormat.toString() else it?.purchaseDate.toString(),
        txtparticulars = it?.customerName.toString(),
        txtvouchernumber = if (issalesorpurchase) it?.salesOrderNumber.toString() else it?.purchaseOrderNumber.toString(),
        txtvalue = BigDecimal(it?.totalvalue.toString()).setScale(2, RoundingMode.HALF_EVEN).toString(),
        txtmerged_details = it?.mergedDetails.toString(),
        ordernumber = if (issalesorpurchase) it?.salesOrderNumber?:"" else it?.purchaseOrderNumber?:"",
        Vouchertype = it?.voucherTypeName?:""
      )
    }?.toMutableList()
    recyclerGroup177List.value = recyclerSalesOrderVoucher

    val decimal = recyclerGroup177List.value?.sumOf { it?.txtvalue?.toDoubleOrNull()?:0.0 }.toString()
    salesOrderVoucherModel.value?.totalvaluevoucher = BigDecimal(decimal).setScale(2, RoundingMode.HALF_EVEN).toString()

    salesOrderVoucherModel.value = salesOrderVoucherModelValue
  }
}
