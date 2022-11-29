package com.wms.superadmin.modules.bankwisebook.`data`.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.modules.bankwisebook.`data`.model.Bankwisebook1RowModel
import com.wms.superadmin.modules.bankwisebook.`data`.model.BankwisebookModel
import com.wms.superadmin.modules.vocherbankbook.data.model.Vocherbankbook1RowModel
import com.wms.superadmin.network.models.bankbook.BankBookRequest
import com.wms.superadmin.network.models.bankbook.BankBookResponse
import com.wms.superadmin.network.models.bankbook.GroupItem
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlin.Boolean
import kotlin.Unit
import kotlin.collections.MutableList
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.math.roundToInt

public class BankwisebookVM : ViewModel(), KoinComponent {
    public val bankwisebookModel: MutableLiveData<BankwisebookModel> =
        MutableLiveData(BankwisebookModel())

    public var navArguments: Bundle? = null
    private val prefs: PreferenceHelper by inject()
    var reportType = prefs.getBookType()

    public val recyclerBankwisebooklistList: MutableLiveData<MutableList<Bankwisebook1RowModel>> =
        MutableLiveData(mutableListOf())

    public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    private val networkRepository: NetworkRepository by inject()


    val Format = DecimalFormat("##.00")

    public val createBankAccountLiveData: MutableLiveData<Response<BankBookResponse>> =
        MutableLiveData<Response<BankBookResponse>>()

    public fun onClickOnCreate(request: BankBookRequest): Unit {
        viewModelScope.launch {
            // BankBookRequest(orgID = "1",screenName ="Ledger",fromDate = "2021/10/21",toDate = "2021/11/19",branchID = "0",reportType = "BankBook")
            progressLiveData.postValue(true)
            createBankAccountLiveData.postValue(
                networkRepository.GetBankBookList(
                    authorization = "bearer " + prefs.getAccess_token(),
                    createBankAccountRequest = request
                )
            )
            progressLiveData.postValue(false)
        }
    }

    public fun bindBankBookResponse(response: BankBookResponse): Unit {
        val bankwisebookModelValue = bankwisebookModel.value ?: BankwisebookModel()
        var dr ="Dr"
        var cr ="Cr"
        val recyclerBankwisebooklist = response.outputDataListObject?.map {
            Bankwisebook1RowModel(
                Debit = if (it?.debit!! > 0) Format.format(BigDecimal(it?.debit!!))
                    .toString() else "0",
                Credit = if (it?.credit!! > 0) Format.format(BigDecimal(it?.credit!!))
                    .toString() else "0",
                ClosingBal = it?.closingBalance.toString(),
                OpeningBalance = it?.openingBalance,
                BankName = it?.ledgerName!!.split("C A/C")[0],
                LedgerId = it?.ledgerID,
                BranchName = it?.branchName,
                BranchId = it?.branchID,
            )
        }?.toMutableList()
        recyclerBankwisebooklistList.value = recyclerBankwisebooklist
        var totalCreditbal = 0
        var totalDebitbal = 0

        for (item in recyclerBankwisebooklist as ArrayList<Bankwisebook1RowModel>) {
            totalCreditbal += item.Credit!!.toDouble().toInt()
            totalDebitbal += item.Debit!!.toDouble().toInt()
        }

        bankwisebookModelValue.txtBranch1 = response.branchName.toString()
        bankwisebookModelValue.TotalBalance = response.totalClosingBalance.toString()
        bankwisebookModelValue.OpeningBalance = response.openingBalance
        bankwisebookModelValue.TotalCreditBalance = "$totalCreditbal $cr"
        bankwisebookModelValue.TotalDebitBalance = "$totalDebitbal $dr"

//        var totaldebit = bankwisebookModelValue.TotalDebitBalance
//        var totalcredit = bankwisebookModelValue.TotalCreditBalance

        /*Sum of credit and debit is equal to opening balance*/
        /* if(reportType.equals("BankBook")) {
          if (totalcredit!! < totaldebit!!) {
              val closingbal = totalDebitbal - totalCreditbal
              bankwisebookModelValue.TotalBalance = closingbal.toString()
          } else {
              val closingbal = totalCreditbal - totalDebitbal
              bankwisebookModelValue.TotalBalance = closingbal.toString()
          }
      }else if (reportType.equals("CashBook")) {*/
        /* if(totalcredit!! < totaldebit!!) {
              val closingbal = totalCreditbal-totalDebitbal
              bankwisebookModelValue.TotalBalance = closingbal.toString()
          } else {
              val closingbal = totalCreditbal - totalDebitbal
              bankwisebookModelValue.TotalBalance = closingbal.toString()
          }*/
        bankwisebookModelValue.OpeningBalance = if(response.totalOpeningBalance!=null) response.totalOpeningBalance.toString() else 0.0.toString()
        bankwisebookModelValue.TotalBalance = if(response.totalClosingBalance!=null) response.totalClosingBalance.toString() else 0.0.toString()
      bankwisebookModelValue.txtCashInBank =if(response.totalClosingBalance!=null && response.totalClosingBalance> 0.toString())Format.format(BigDecimal(response.totalClosingBalance)).toString() else "0"
      bankwisebookModel.value = bankwisebookModelValue
    }
  }

