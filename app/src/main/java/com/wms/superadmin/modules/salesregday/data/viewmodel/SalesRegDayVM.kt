package com.wms.superadmin.modules.salesregday.data.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.extensions.ReportTypes
import com.wms.superadmin.modules.salesregday.data.model.SalesRegDay1RowModel
import com.wms.superadmin.modules.salesregday.data.model.SalesRegDayModel
import com.wms.superadmin.modules.salesregday.data.model.SpinnerBtnprimaryModel
import com.wms.superadmin.network.models.Salesregister.SalesRegisterRequest
import com.wms.superadmin.network.models.Salesregister.SalesRegisterResponse
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToInt

class SalesRegDayVM : ViewModel(), KoinComponent {
  val salesRegDayModel: MutableLiveData<SalesRegDayModel> = MutableLiveData(SalesRegDayModel())

  var navArguments: Bundle? = null

  val spinnerBtnprimaryList: MutableLiveData<MutableList<SpinnerBtnprimaryModel>> =
    MutableLiveData()

  val recyclerSalesRegDayList: MutableLiveData<MutableList<SalesRegDay1RowModel>> =
    MutableLiveData(mutableListOf())

  val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

  private val networkRepository: NetworkRepository by inject()
  private val prefs: PreferenceHelper by inject()

  val createSalesRegisterLiveData: MutableLiveData<Response<SalesRegisterResponse>> =
    MutableLiveData<Response<SalesRegisterResponse>>()

  fun SalesRegDay(request: SalesRegisterRequest, SalesorPurchase: String?) {
    viewModelScope.launch {
      progressLiveData.postValue(true)

      if (SalesorPurchase==ReportTypes.SALESREGISTER) {
        createSalesRegisterLiveData.postValue(
          networkRepository.createSalesRegister(
            authorization = "bearer ${prefs.getAccess_token()}",
            salesRegisterRequest = request
          )
        )
        progressLiveData.postValue(false)
      }
      else if (SalesorPurchase==ReportTypes.PURCHASEREGISTER){
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

  fun bindCreateSalesRegisterResponse(response: SalesRegisterResponse, issalesorpurchase: Boolean) {
    val salesRegDayModelValue = salesRegDayModel.value ?: SalesRegDayModel()
    if (issalesorpurchase) {
//      val recyclerSalesRegDay = response.salesList?.outputDataListObject?.groupBy { it!!.creationDate }!!.map {
//          SalesRegDay1RowModel(
//            txtDateSalesregday = it.value[0]?.creationDate ?:"",
//            txtDebitSalesregday = BigDecimal(it.value.sumOf { it!!.debit!! }).setScale(2, RoundingMode.HALF_EVEN).toDouble(),
//            txtCreditSalesregday = BigDecimal(it.value.sumOf { it!!.credit!! }).setScale(2, RoundingMode.HALF_EVEN).toDouble(),
//            txtNetsalesSalesregday = BigDecimal(it.value[0]?.closingBalance ?: "").setScale(2, RoundingMode.HALF_EVEN).toString(),
//            branchname = it.value[0]?.branchName ?: "",
//            branchid = it.value[0]?.branchID ?: "",
//            creationdate = it.value[0]?.creationDate ?:""
//          )
//        }.toMutableList()
//      recyclerSalesRegDayList.value = recyclerSalesRegDay
//    }

          val recyclerSalesRegDay = response.salesList?.outputDataListObject?.map {
          SalesRegDay1RowModel(
            txtDateSalesregday = it?.creationDate ?:"",
            txtDebitSalesregday = it?.debit?.roundToInt().toString(),
            txtCreditSalesregday = it?.credit?.roundToInt().toString(),
            txtNetsalesSalesregday = it?.closingBalance ?:"",
            branchname = it?.branchName ?: "",
            branchid = it?.branchID ?: "",
            creationdate = it?.creationDate ?:""
          )
        }?.toMutableList()
      recyclerSalesRegDayList.value = recyclerSalesRegDay
    }
    else if (!issalesorpurchase){
      val recyclerSalesRegDay = response.purchaseList?.outputDataListObject?.map {
          SalesRegDay1RowModel(
            txtDateSalesregday = it?.creationDate ?: "",
            txtDebitSalesregday = it?.debit?.roundToInt().toString(),
            txtCreditSalesregday = it?.credit?.roundToInt().toString(),
            txtNetsalesSalesregday = it?.closingBalance?:"",
            branchname = it?.branchName ?: "",
            branchid = it?.branchID ?: "",
            creationdate = it?.creationDate ?:""
          )
        }?.toMutableList()
      recyclerSalesRegDayList.value = recyclerSalesRegDay
    }

    val decimal1 = recyclerSalesRegDayList.value?.sumOf { it.txtCreditSalesregday?.toIntOrNull()!! }.toString()
    salesRegDayModelValue.txtTotalcreditSalesregday = BigDecimal(decimal1).setScale(2, RoundingMode.HALF_EVEN).toString()

    val decimal2 = recyclerSalesRegDayList.value?.sumOf { it.txtDebitSalesregday?.toIntOrNull()!! }.toString()
    salesRegDayModelValue.txtTotaldebitSalesregday = BigDecimal(decimal2).setScale(2, RoundingMode.HALF_EVEN).toString()

//    val decimal3 = recyclerSalesRegDayList.value?.sumOf { (it.txtNetsalesSalesregday?.split(" ")?.get(0)?.toDoubleOrNull()?:0.0)}.toString()
    salesRegDayModelValue.txtTotalnetsalesSalesregday= BigDecimal(decimal2).setScale(2, RoundingMode.HALF_EVEN).toString()

    salesRegDayModel.value = salesRegDayModelValue
  }
}
