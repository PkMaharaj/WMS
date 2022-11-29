package com.wms.superadmin.modules.salesregbranches.data.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.extensions.ReportTypes
import com.wms.superadmin.modules.salesregbranches.data.model.SalesRegBranches1RowModel
import com.wms.superadmin.modules.salesregbranches.data.model.SalesRegBranchesModel
import com.wms.superadmin.modules.salesregbranches.data.model.SpinnerBtnprimaryModel
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

class SalesRegBranchesVM : ViewModel(), KoinComponent {
  val salesRegBranchesModel: MutableLiveData<SalesRegBranchesModel> =
    MutableLiveData(SalesRegBranchesModel())

  var navArguments: Bundle? = null

  val spinnerBtnprimaryList: MutableLiveData<MutableList<SpinnerBtnprimaryModel>> =
    MutableLiveData()

  val recyclerSalesRegBranchesList: MutableLiveData<MutableList<SalesRegBranches1RowModel>> =
    MutableLiveData(mutableListOf())

  val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

  private val networkRepository: NetworkRepository by inject()
  private val prefs: PreferenceHelper by inject()

  val createSalesRegisterLiveData: MutableLiveData<Response<SalesRegisterResponse>> =
    MutableLiveData<Response<SalesRegisterResponse>>()

  fun getSalesRegister(request: SalesRegisterRequest, SalesorPurchase: String?) {
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

  fun bindCreateSalesRegisterResponse(response: ArrayList<ResponseSalesListOutputDataListObjectItem?>?, issalesregister: Boolean) {
    val salesRegBranchesModelValue = salesRegBranchesModel.value ?: SalesRegBranchesModel()
    val recyclerSalesRegBranches = response?.map {
      SalesRegBranches1RowModel(
        txtBrachnameBranch = it?.branchName?:"",
        txtDebitBranch = it?.debit?.roundToInt().toString(),
        txtCreditBranch = it?.credit?.roundToInt().toString(),
        txtNetsalesBranch = it?.closingBalance?:"",
        branchid = it?.branchID?.toInt()?:0,
        branchname = it?.branchName?:""
      )
    }?.toMutableList()
    recyclerSalesRegBranchesList.value = recyclerSalesRegBranches

    val decimal1 = recyclerSalesRegBranchesList.value?.sumOf { it.txtCreditBranch!!.toDouble() }?:0.0
    Log.e("Decimal ", decimal1.toString())
    salesRegBranchesModelValue.txtTotalcreditBranch = BigDecimal(decimal1).setScale(2, RoundingMode.HALF_EVEN).toString()

    val decimal2 = recyclerSalesRegBranchesList.value?.sumOf { it.txtDebitBranch!!.toDouble() }?:0.0
    salesRegBranchesModelValue.txtTotaldebitBranch = BigDecimal(decimal2).setScale(2, RoundingMode.HALF_EVEN).toString()

    val decimal3 = recyclerSalesRegBranchesList.value?.sumOf { (it.txtNetsalesBranch?.split(" ")?.get(0)?.toDoubleOrNull()!!)}.toString()
    salesRegBranchesModelValue.txtTotalnetsalesBranch=BigDecimal(decimal3).setScale(2, RoundingMode.HALF_EVEN).toString()

    salesRegBranchesModel.value = salesRegBranchesModelValue
  }

}
