package com.wms.superadmin.modules.transaction.`data`.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.modules.transaction.data.model.*
import com.wms.superadmin.network.models.createallbranchreceivables.AgingRequestItem
import com.wms.superadmin.network.models.createallbranchreceivables.CreateAllBranchReceivablesResponse
import com.wms.superadmin.network.models.createallbranchreceivables.TransactionRequest
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlinx.coroutines.launch
import kotlin.collections.MutableList
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.math.BigDecimal
import java.text.DecimalFormat

class TransactionVM : ViewModel(), KoinComponent {
    val transactionModel: MutableLiveData<TransactionModel> = MutableLiveData(TransactionModel())

  var navArguments: Bundle? = null


  val transactionList: MutableLiveData<MutableList<TransactionRowModel>> =
      MutableLiveData(mutableListOf())

    val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    private val networkRepository: NetworkRepository by inject()

    val  createAllBranchReceivablesLiveData: MutableLiveData<Response<CreateAllBranchReceivablesResponse>> =
        MutableLiveData<Response<CreateAllBranchReceivablesResponse>>()

    val recyclerSalesRegMonthList: MutableLiveData<MutableList<TransactionRowModel>> =
        MutableLiveData(mutableListOf())

    private val prefs: PreferenceHelper by inject()
    val Format = DecimalFormat("##.00")
    fun onClickOnCreate(request: TransactionRequest) {
        viewModelScope.launch {
            progressLiveData.postValue(true)
            createAllBranchReceivablesLiveData.postValue(
                networkRepository.createAllBranchReceivables(
                    authorization = "bearer ${prefs.getAccess_token()}",
                    createAllBranchReceivablesRequest = request)
            )
            progressLiveData.postValue(false)
        }
    }
    fun createAllBranchReceivables(response: CreateAllBranchReceivablesResponse,AgingRequest:AgingRequestItem,TransType:String) {
        var aging1="${AgingRequest.fromDays1} - ${AgingRequest.toDays1}"
        var aging2="${AgingRequest.fromDays2} - ${AgingRequest.toDays2}"
        var totalBalance=0.0
        var totalBillamount=0.0
        val salesRegMonthModelValue = transactionModel.value ?: TransactionModel()

        val BranchrecyclerRowModel = response.outputDataListObject?.map {
            TransactionRowModel(
                BranchName = it?.branchName?:"NA",
                BranchId=it?.branchID?:"0",
                BillAmount = it?.billAmount?:"0",
                TotalBalance = it?.outstandingBalance?:"0",
                AgingBalance1 = "$aging1 : ${it?.outstandingBalance1?:"0"}",
                AgingBalance2 = "$aging2 : ${it?.outstandingBalance2?:"0"}")
        }?.toMutableList()

        for(item in BranchrecyclerRowModel as ArrayList){
            var balance=item.TotalBalance
            var billAmount=item.BillAmount
            if(balance!!.contains("Dr",true))
                balance=balance.split("Dr")[0].trim()
            if(balance!!.contains("Cr",true))
                balance=balance.split("Cr")[0].trim()
            else
                balance=balance.trim()

            if(billAmount!!.contains("Dr",true))
                billAmount=billAmount.split("Dr")[0].trim()
            if(billAmount!!.contains("Cr",true))
                billAmount=billAmount.split("Cr")[0].trim()
            else
                billAmount=billAmount.trim()

            totalBalance+=balance.toDoubleOrNull()?:0.0
            totalBillamount+=billAmount.toDoubleOrNull()?:0.0
        }
        salesRegMonthModelValue.TotalBalance="Total Balance: ${Format.format(BigDecimal(totalBalance))} $TransType"
        salesRegMonthModelValue.TotalBillAmount="Total Bill Amount : ${Format.format(BigDecimal(totalBillamount))} $TransType"
        transactionList.value =BranchrecyclerRowModel
        transactionModel.value = salesRegMonthModelValue
    }


}




