package com.wms.superadmin.modules.salesorderbranch.data.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.extensions.ReportTypes
import com.wms.superadmin.modules.salesorderbranch.data.model.SalesOrderBranchModel
import com.wms.superadmin.modules.salesorderbranch.data.model.SalesOrderRowModel
import com.wms.superadmin.modules.salesorderbranch.data.model.SpinnerBtnprimaryModel
import com.wms.superadmin.modules.salesordermonthly.data.model.SalesOrderMonthlyModel
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.createsalesorderreport.CreateSalesOrderReportResponse
import com.wms.superadmin.network.models.createsalesorderreport.SalesOrderReportRequest
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
import java.util.ArrayList

public class SalesOrderBranchVM : ViewModel(), KoinComponent {
  private var salesorder_request_screen: String? = ""
  public val salesOrderBranchModel: MutableLiveData<SalesOrderBranchModel> =
      MutableLiveData(SalesOrderBranchModel())

  public val salesOrderMonthlyModel: MutableLiveData<SalesOrderMonthlyModel> =
    MutableLiveData(SalesOrderMonthlyModel())

  public var navArguments: Bundle? = null

  public val spinnerBtnprimaryList: MutableLiveData<MutableList<SpinnerBtnprimaryModel>> =
      MutableLiveData()

  public val recyclerGroup177List: MutableLiveData<MutableList<SalesOrderRowModel>> =
      MutableLiveData(mutableListOf())

//  public val recyclerMonthlyList: MutableLiveData<MutableList<SalesOrderMonthly1RowModel>> =
//    MutableLiveData(mutableListOf())

  public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

  private val networkRepository: NetworkRepository by inject()
  private val prefs: PreferenceHelper by inject()
  var SuperAdminDetails=prefs.getSADetails<LoginResponse>()

  public val createSalesOrderReportLiveData:
      MutableLiveData<Response<CreateSalesOrderReportResponse>> =
      MutableLiveData<Response<CreateSalesOrderReportResponse>>()

  public fun getSalesOrder(request: SalesOrderReportRequest, SalesorPurchase: String?): Unit {
    viewModelScope.launch {
      progressLiveData.postValue(true)

      if (SalesorPurchase==ReportTypes.SALESORDER){
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

  public fun bindCreateSalesOrderReportResponse(response: ArrayList<SalesOrderReportResponselistItem?>?, issalespurchaseorder: Boolean): Unit {
    val salesOrderBranchModelValue = salesOrderBranchModel.value ?: SalesOrderBranchModel()
    val recyclerGroup177 = response?.map {
      SalesOrderRowModel(
//        branch
        so_branchname = it?.branchName.toString()?:"",
        so_totalvouchercount = it?.totalVouchersCount.toString()?:"",
        branchID = if(issalespurchaseorder) it?.SO_branchID?.toIntOrNull()?:0 else it?.PO_branchID?.toIntOrNull()?:0,
        so_value = BigDecimal(it?.totalvalue?:0.0).setScale(2, RoundingMode.HALF_EVEN).toString(),

//        monthly
        txtMonth = it?.monthName.toString()?:"",
        monthID = it?.monthID?:0,
        txtvouchers = it?.totalVouchersCount.toString()?:"",
        totalvalue = BigDecimal(it?.totalvalue?:0.0).setScale(2, RoundingMode.HALF_EVEN).toString(),

//        voucher
        txtDate = if (issalespurchaseorder) it?.salesDateInDateFormat.toString() else it?.purchaseDateInDateFormat,
        txtvouchernumber = it?.totalVouchersCount.toString(),
        txtsalestype = if (issalespurchaseorder) it?.salesType.toString() else it?.purchasetype,
        txtvalue = BigDecimal(it?.totalvalue?:0.0).setScale(2, RoundingMode.HALF_EVEN).toString(),
        ordernumber = if (issalespurchaseorder) it?.salesOrderNumber?:"" else it?.purchaseOrderNumber?:"",
        Vouchertype = it?.voucherTypeName?:""
      )
    }?.toMutableList()
      recyclerGroup177List.value = recyclerGroup177

    salesOrderBranchModel.value?.totalvouchercount = recyclerGroup177List.value?.sumOf { it.txtvouchernumber?.toIntOrNull()?:0}.toString()!!

    val decimal = recyclerGroup177List.value?.sumOf { it.so_value.toDoubleOrNull()?:0.0 }.toString()
    salesOrderBranchModelValue.totalvalue = BigDecimal(decimal).setScale(2, RoundingMode.HALF_EVEN).toString()

    salesOrderBranchModel.value = salesOrderBranchModelValue
    }
}
