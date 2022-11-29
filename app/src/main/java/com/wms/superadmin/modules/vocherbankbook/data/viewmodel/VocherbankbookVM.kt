package com.wms.superadmin.modules.vocherbankbook.`data`.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.modules.vocherbankbook.`data`.model.Vocherbankbook1RowModel
import com.wms.superadmin.modules.vocherbankbook.`data`.model.VocherbankbookModel
import com.wms.superadmin.network.models.bankbook.BankBookResponse
import com.wms.superadmin.network.models.bankbook.LedgerItem
import com.wms.superadmin.network.models.bankbook.VoucherRequest
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlin.Boolean
import kotlin.Unit
import kotlin.collections.MutableList
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.math.roundToInt

public class VocherbankbookVM : ViewModel(), KoinComponent {
  public val vocherbankbookModel: MutableLiveData<VocherbankbookModel> =
      MutableLiveData(VocherbankbookModel())

  public var navArguments: Bundle? = null

  public val recyclerVocherwisebooklistList: MutableLiveData<MutableList<Vocherbankbook1RowModel>> =
      MutableLiveData(mutableListOf())

  public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

  private val networkRepository: NetworkRepository by inject()

  private val prefs: PreferenceHelper by inject()
  var reportType=prefs.getBookType()

  public val createBankAccountVoucherLiveData: MutableLiveData<Response<BankBookResponse>> = MutableLiveData<Response<BankBookResponse>>()

  public fun onClickOnCreate(voucherRequest: VoucherRequest): Unit {
    viewModelScope.launch {
      progressLiveData.postValue(true)
      createBankAccountVoucherLiveData.postValue(networkRepository.getvocherWiseBankBook(authorization = "bearer "+prefs.getAccess_token(),
          createBankAccountVoucherRequest = voucherRequest))
      progressLiveData.postValue(false)
    }
  }
  public fun bindVoucherResponse(response: BankBookResponse): Unit {
    val vocherbankbookModelValue = vocherbankbookModel.value ?:VocherbankbookModel()
    var dr ="Dr"
    var cr ="Cr"
    val recyclerBankwisebooklist = response.outputDataListObject?.map {
      Vocherbankbook1RowModel(
        Debit = if(it?.debit!! > 0)it.debit.toString() else "0",
        Credit =if(it?.credit!! > 0)it.credit.toDouble().roundToInt().toString()else "0",
        VocherType = it?.voucherType,
        VocherTypeNo = it?.voucherID,
        ClosingBal = it?.closingBalance.toString(),
        Party = it?.ledgerName,
        Description = it?.narration,
        LedgerDate = it.creationDate,
        BranchName = it?.branchName,
        BranchId = it?.branchID,)
    }?.toMutableList()
    recyclerVocherwisebooklistList.value = recyclerBankwisebooklist
    var totalCreditbal=0
    var totalDebitbal=0
    for(item in recyclerBankwisebooklist as ArrayList<Vocherbankbook1RowModel>)
    {
      totalCreditbal += item.Credit!!.toDouble().toInt()
      totalDebitbal += item.Debit!!.toDouble().toInt()
    }
    vocherbankbookModelValue.txtBranch1 = response.branchName.toString()
    vocherbankbookModelValue.TotalBalance = response.totalClosingBalance.toString()
    vocherbankbookModelValue.OpeningBalance =  response.openingBalance
    vocherbankbookModelValue.TotalCreditBalance = totalCreditbal.toDouble().toString() + " " +cr
    vocherbankbookModelValue.TotalDebitBalance = totalDebitbal.toDouble().toString() + " " +dr

/*    var totaldebit = vocherbankbookModelValue.TotalDebitBalance
    var totalcredit = vocherbankbookModelValue.TotalCreditBalance

    if(reportType.equals("BankBook")) {
      if (totalcredit!! < totaldebit!!) {
        val closingbal = totalDebitbal - totalCreditbal
        vocherbankbookModelValue.TotalBalance = closingbal.toString()
      } else {
        val closingbal = totalCreditbal - totalDebitbal
        vocherbankbookModelValue.TotalBalance = closingbal.toString()
      }
    }else if (reportType.equals("CashBook")) {
      if(totalcredit!! < totaldebit!!) {
        val closingbal = totalCreditbal-totalDebitbal
        vocherbankbookModelValue.TotalBalance = closingbal.toString()
      } else {
        val closingbal = totalCreditbal - totalDebitbal
        vocherbankbookModelValue.TotalBalance = closingbal.toString()
      }
    }*/
    vocherbankbookModelValue.OpeningBalance = if(response.totalOpeningBalance!=null) response.totalOpeningBalance.toString() else 0.0.toString()
    vocherbankbookModelValue.TotalBalance = if(response.totalClosingBalance!=null) response.totalClosingBalance.toString() else 0.0.toString()
    vocherbankbookModel.value = vocherbankbookModelValue
  }
}
