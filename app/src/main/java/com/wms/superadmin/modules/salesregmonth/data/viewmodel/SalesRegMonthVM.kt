package com.wms.superadmin.modules.salesregmonth.`data`.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.extensions.ReportTypes
import com.wms.superadmin.modules.salesregmonth.data.model.SalesRegMonth1RowModel
import com.wms.superadmin.modules.salesregmonth.`data`.model.SalesRegMonthModel
import com.wms.superadmin.network.models.Salesregister.ResponseSalesListOutputDataListObjectItem
import com.wms.superadmin.network.models.Salesregister.SalesRegisterRequest
import com.wms.superadmin.network.models.Salesregister.SalesRegisterResponse
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlin.Boolean
import kotlin.collections.MutableList
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

class SalesRegMonthVM : ViewModel(), KoinComponent {
  val salesRegMonthModel: MutableLiveData<SalesRegMonthModel> =
    MutableLiveData(SalesRegMonthModel())

  var navArguments: Bundle? = null

  val recyclerSalesRegMonthList: MutableLiveData<MutableList<SalesRegMonth1RowModel>> =
    MutableLiveData(mutableListOf())

  val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

  private val networkRepository: NetworkRepository by inject()

  val createSalesRegisterLiveData: MutableLiveData<Response<SalesRegisterResponse>> =
    MutableLiveData<Response<SalesRegisterResponse>>()

  private val prefs: PreferenceHelper by inject()

  fun getSalesRegisterMonth(request: SalesRegisterRequest, SalesorPurchase: String) {
    viewModelScope.launch {
      progressLiveData.postValue(true)

      if (SalesorPurchase == ReportTypes.SALESREGISTER) {
        createSalesRegisterLiveData.postValue(
          networkRepository.createSalesRegister(
            authorization = "bearer ${prefs.getAccess_token()}",
            salesRegisterRequest = request
          )
        )
        progressLiveData.postValue(false)
      }
      else if (SalesorPurchase == ReportTypes.PURCHASEREGISTER){
        createSalesRegisterLiveData.postValue(
          networkRepository.createPurchaseRegister(
            authorization = "bearer ${prefs.getAccess_token()}",
            salesRegisterRequest = request
          )
        )
        progressLiveData.postValue(false)
      }
    }
  }

  fun bindCreateSalesRegisterResponse(response: ArrayList<ResponseSalesListOutputDataListObjectItem?>?, issalesorpurchase: Boolean) {
    val salesRegMonthModelValue = salesRegMonthModel.value ?:SalesRegMonthModel()
    val recyclerSalesRegMonth = response?.map {
      SalesRegMonth1RowModel(
        txtMonthSalesregbranch = it?.monthName.toString()?:"",
        txtDebitSalesregbranch = it?.debit?.roundToInt().toString()?:"",
        txtCreditSalesregbranch = it?.credit?.roundToInt().toString()?:"",
        txtNetsalesSalesregbranch = it?.closingBalance.toString()?:"",
        monthID = it?.monthID?:0,
        branchid = it?.branchID?:"",
        branchname = it?.branchName?:"",
      )
    }?.toMutableList()
    recyclerSalesRegMonthList.value = recyclerSalesRegMonth

    val decimal1 = recyclerSalesRegMonthList.value?.sumOf { it.txtDebitSalesregbranch?.toIntOrNull()!! }.toString()
    salesRegMonthModelValue.txtTotaldebitSalesregbranch = BigDecimal(decimal1).setScale(2, RoundingMode.HALF_EVEN).toString()

    val decimal2 = recyclerSalesRegMonthList.value?.sumOf { it.txtCreditSalesregbranch?.toIntOrNull()!! }.toString()
    salesRegMonthModelValue.txtTotalcreditSalesregbranch = BigDecimal(decimal2).setScale(2, RoundingMode.HALF_EVEN).toString()

    salesRegMonthModelValue.txtTotalnetsalesSalesregbranch = recyclerSalesRegMonthList.value?.last()!!.txtNetsalesSalesregbranch

    salesRegMonthModel.value = salesRegMonthModelValue
  }
}
