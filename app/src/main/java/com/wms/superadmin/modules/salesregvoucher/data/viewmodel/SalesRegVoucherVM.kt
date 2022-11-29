package com.wms.superadmin.modules.salesregvoucher.data.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.extensions.ReportTypes
import com.wms.superadmin.modules.salesregvoucher.data.model.SalesRegVoucher1RowModel
import com.wms.superadmin.modules.salesregvoucher.data.model.SalesRegVoucherModel
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

class SalesRegVoucherVM : ViewModel(), KoinComponent {
  val salesRegVoucherModel: MutableLiveData<SalesRegVoucherModel> =
    MutableLiveData(SalesRegVoucherModel())

  var navArguments: Bundle? = null

  val recyclerSalesRegVoucherList: MutableLiveData<MutableList<SalesRegVoucher1RowModel>> =
    MutableLiveData(mutableListOf())

  val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

  private val networkRepository: NetworkRepository by inject()
  private val prefs: PreferenceHelper by inject()

  val createSalesRegisterLiveData: MutableLiveData<Response<SalesRegisterResponse>> =
    MutableLiveData<Response<SalesRegisterResponse>>()

  fun SalesRegVoucher(request: SalesRegisterRequest, SalesorPurchase: String) {
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

  fun bindCreateSalesRegisterResponse(response: ArrayList<ResponseSalesListOutputDataListObjectItem?>?, issalesorpurchase: Boolean) {
    val salesRegVoucherModelValue = salesRegVoucherModel.value ?: SalesRegVoucherModel()
    val recyclerSalesRegVoucher = response?.map {
      SalesRegVoucher1RowModel(
        txtDate = it?.creationDate.toString(),
        txtParticulars = it?.ledgerName.toString(),
        txtdebitsalesregvoucher = it?.debit?.roundToInt().toString(),
        txtcreditsalesregvoucher = it?.credit?.roundToInt().toString(),
        mergeddetails = it?.mergedDetails?:"",
        voucherid = it?.voucherid?:"",
        Vouchertype = it?.voucherType?:""
      )
    }?.toMutableList()
    recyclerSalesRegVoucherList.value = recyclerSalesRegVoucher

    val decimal1 = recyclerSalesRegVoucherList.value?.sumOf { it.txtdebitsalesregvoucher?.toIntOrNull()?:0}.toString()
    salesRegVoucherModelValue.txtsalesregvoucherdebit = BigDecimal(decimal1).setScale(2, RoundingMode.HALF_EVEN).toString()

    val decimal2 = recyclerSalesRegVoucherList.value?.sumOf { it.txtcreditsalesregvoucher?.toIntOrNull()?:0 }.toString()
    salesRegVoucherModelValue.txtsalesregvouchercredit = BigDecimal(decimal2).setScale(2, RoundingMode.HALF_EVEN).toString()

/*    val decimal3 = recyclerSalesRegVoucherList.value?.sumOf { (it.txtnetsalesregvoucher?.split(" ")?.get(0)?.toDoubleOrNull()?:0.0)}.toString()
    salesRegVoucherModelValue.txtsalesregvouchernetsales = BigDecimal(decimal3).setScale(2, RoundingMode.HALF_EVEN).toString()*/

    salesRegVoucherModel.value = salesRegVoucherModelValue
  }
}
