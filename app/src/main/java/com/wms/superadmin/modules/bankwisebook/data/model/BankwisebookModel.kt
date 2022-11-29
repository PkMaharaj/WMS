package com.wms.superadmin.modules.bankwisebook.`data`.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class BankwisebookModel(
  /**
   * TODO Replace with dynamic value
   */
  public var txtBankBook: String? = MyApp.getInstance().resources.getString(R.string.lbl_bank_book)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtTXtBranch: String? = MyApp.getInstance().resources.getString(R.string.lbl_branch)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtBranchname: String? = MyApp.getInstance().resources.getString(R.string.lbl_mwb_darwad)
  ,
  public var txtBranch1: String? = "",
  /**
   * TODO Replace with dynamic value
   */
  public var txtCategory9: String? =
      MyApp.getInstance().resources.getString(R.string.lbl_cash_in_bank)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtCashInBank: String? =""
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtFrom: String? = MyApp.getInstance().resources.getString(R.string.lbl_from)
  ,
  /**
   * TODO Replace with dynamic value
   */
  /**
   * TODO Replace with dynamic value
   */
  public var txtTo: String? = MyApp.getInstance().resources.getString(R.string.lbl_to)
  ,
  /**
   * TODO Replace with dynamic value
   */

  /**
   * TODO Replace with dynamic value
   */
  public var txtOpeningbalance: String? =
      MyApp.getInstance().resources.getString(R.string.lbl_opening_balance)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtClosingbalance: String? =
      MyApp.getInstance().resources.getString(R.string.lbl_closing_balance)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtBank: String? = MyApp.getInstance().resources.getString(R.string.lbl_bank)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtDebit: String? = MyApp.getInstance().resources.getString(R.string.lbl_debit)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtCredit: String? = MyApp.getInstance().resources.getString(R.string.lbl_credit)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtTotal: String? = MyApp.getInstance().resources.getString(R.string.lbl_total)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtTotalValue: String? = MyApp.getInstance().resources.getString(R.string.lbl_450000),

          public var TotalDebitBalance: String? =
    MyApp.getInstance().resources.getString(R.string.lbl_450000)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtTcb: String? = MyApp.getInstance().resources.getString(R.string.msg_total_credit_ba)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var TotalCreditBalance: String? =
    MyApp.getInstance().resources.getString(R.string.lbl_450000)
  ,
  public var TotalBalance: String? =
    MyApp.getInstance().resources.getString(R.string.lbl_450000)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtOp: String? = MyApp.getInstance().resources.getString(R.string.msg_opening_balance)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var OpeningBalance: String? =
    MyApp.getInstance().resources.getString(R.string.lbl_450000)
)
