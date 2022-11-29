package com.wms.superadmin.modules.bankbook.`data`.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.extensions.ReportTypes
import com.wms.superadmin.modules.bankbook.data.model.Bankbook1RowModel
import com.wms.superadmin.modules.bankbook.data.model.BankbookModel
import com.wms.superadmin.modules.bankbook.`data`.model.SpinnerAllBranchesModel
import com.wms.superadmin.modules.bankbook.`data`.model.SpinnerGroup206Model
import com.wms.superadmin.modules.bankwisebook.data.model.Bankwisebook1RowModel
import com.wms.superadmin.network.models.bankbook.BankBookRequest
import com.wms.superadmin.network.models.bankbook.BankBookResponse
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.math.BigDecimal
import java.text.DecimalFormat

public class BankbookVM : ViewModel(), KoinComponent {
  public val bankbookModel: MutableLiveData<BankbookModel> = MutableLiveData(BankbookModel())

  public var navArguments: Bundle? = null

  public val recyclerBankbooklistList: MutableLiveData<MutableList<Bankbook1RowModel>> =
      MutableLiveData(mutableListOf())

  public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

  private val networkRepository: NetworkRepository by inject()

  private val prefs: PreferenceHelper by inject()
  var reportType=prefs.getBookType()

  public val createBankAccountLiveData: MutableLiveData<Response<BankBookResponse>> =
      MutableLiveData<Response<BankBookResponse>>()
  val Format = DecimalFormat("##.00")

  public fun onClickOnCreate(request:BankBookRequest): Unit {
    viewModelScope.launch {
      progressLiveData.postValue(true)
      createBankAccountLiveData.postValue(networkRepository.GetBankBookList(authorization = "bearer "+prefs.getAccess_token(),
          createBankAccountRequest = request))
      progressLiveData.postValue(false)
    }
  }
  public fun bindBankBookResponse(response: BankBookResponse): Unit {
    val bankbookModelValue = bankbookModel.value ?:BankbookModel()
    var dr ="Dr"
    var cr ="Cr"

    val recyclerBankbooklist = response.outputDataListObject?.map {
      Bankbook1RowModel(
        Debit = if(it?.debit!! > 0)Format.format(BigDecimal(it?.debit!!)).toString() else "0",
        Credit =if(it?.credit!! > 0)Format.format(BigDecimal(it?.credit!!)).toString() else "0",
        ClosingBal = it?.closingBalance,
        OpeningBalance = it?.openingBalance,
        BranchId = it?.branchID,
        BranchName = if(prefs.getBookType()== ReportTypes.BANKBOOK)"${it?.branchName} ( ${it?.groupName} )" else it?.branchName,
        LedgerName = it?.ledgerName,
        LedgerId = it?.ledgerID,
        groupname = it?.groupName,
        groupid = it?.groupID
      )
    }?.toMutableList()
      recyclerBankbooklistList.value = recyclerBankbooklist
    var totalCreditbal = 0.0
    var totalDebitbal = 0.0
    var totalOpenbal = 0.0

    for (item in recyclerBankbooklist as ArrayList<Bankbook1RowModel>) {
      totalCreditbal += item.Credit!!.toDouble().toInt()
      totalDebitbal += item.Debit!!.toDouble().toInt()
    }
    bankbookModelValue.txtBranch1 = response.branchName.toString()
    bankbookModelValue.OpeningBalance = response.openingBalance
    bankbookModelValue.ClosingBalance = response.totalClosingBalance.toString()
    bankbookModelValue.TotalCreditBalance = "$totalCreditbal $cr"
    bankbookModelValue.TotalDebitBalance = "$totalDebitbal $dr"

    var totaldebit = bankbookModelValue.TotalDebitBalance
    var totalcredit = bankbookModelValue.TotalCreditBalance
    var totalbalance = bankbookModelValue.ClosingBalance
    var openingbalance = bankbookModelValue.OpeningBalance

    /*Sum of credit and debit is equal to opening balance*/
  /*  if(reportType.equals("CashBook")) {
      if (totalcredit!! < totaldebit!!) {
        var Total = totalDebitbal - totalCreditbal
        bankbookModelValue.ClosingBalance = "$Total $dr"
      } else if (totaldebit!! < totalcredit!!) {
        var Total = totalCreditbal - totalDebitbal
        bankbookModelValue.ClosingBalance = "$Total $cr"
      }*/
/*
    val opbal = bankbookModelValue.OpeningBalance.toString().toDouble()
    if(totalDebitbal >opbal){
      val Total =  totalDebitbal - opbal
      bankbookModelValue.ClosingBalance = "$Total $dr"
    }else
      if(totalCreditbal > opbal) {
        val Total = totalCreditbal - opbal
        bankbookModelValue.ClosingBalance ="$Total $cr"

      }*/
 /*   }else if
        (reportType.equals("BankBook")) {
          if(totalcredit!! < totaldebit!!) {
            val closingbal = totalDebitbal - totalCreditbal
            bankbookModelValue.ClosingBalance = closingbal.toString()+""+ dr
          }
        } else {
          val closingbal = totalCreditbal - totalDebitbal
          bankbookModelValue.ClosingBalance = closingbal.toString()+""+cr
        }*/
   /* val opbal2 = bankbookModelValue.OpeningBalance.toString().toDouble()
    if(totalDebitbal > opbal2){
      val Total =  totalDebitbal - opbal2
      bankbookModelValue.ClosingBalance = Total.toString() +""+ dr
    }else
      if(totalCreditbal > opbal2){
        val Total =  totalCreditbal - opbal2
        bankbookModelValue.ClosingBalance = Total.toString() +""+ cr
      }*/
      bankbookModelValue.txtCashInBank =if(response.totalClosingBalance!=null && response.totalClosingBalance> 0.toString())Format.format(BigDecimal(response.totalClosingBalance)).toString() else "0"
      bankbookModelValue.OpeningBalance = if(response.totalOpeningBalance!=null) response.totalOpeningBalance.toString() else 0.0.toString()
      bankbookModelValue.ClosingBalance = if(response.totalClosingBalance!=null) response.totalClosingBalance.toString() else 0.0.toString()
      bankbookModel.value = bankbookModelValue
    }
  }
