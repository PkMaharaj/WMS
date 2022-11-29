package com.wms.superadmin.modules.vocherbankbook.`data`.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class VocherbankbookModel(
  /**
   * TODO Replace with dynamic value
   */
  public var txtVocherBankBook: String? =
      MyApp.getInstance().resources.getString(R.string.lbl_vocherbankbook)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtBranch: String? = MyApp.getInstance().resources.getString(R.string.lbl_branch),
  public var CashinBank: String? = ""
  ,
  public var txtBranchname: String? = MyApp.getInstance().resources.getString(R.string.lbl_mwb_darwad)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtBranch1: String? = ""
  ,
  /**
   * TODO Replace with dynamic value
   */
  /**
   * TODO Replace with dynamic value
   */
  public var txtTotalCB: String? = MyApp.getInstance().resources.getString(R.string.lbl_1000_00)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtVoucherNum: String? =
      MyApp.getInstance().resources.getString(R.string.lbl_voucher_num)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtVouchertype: String? =
      MyApp.getInstance().resources.getString(R.string.lbl_voucher_type)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtCredit: String? = MyApp.getInstance().resources.getString(R.string.lbl_credit)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtDate: String? = MyApp.getInstance().resources.getString(R.string.lbl_date)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtParty: String? = MyApp.getInstance().resources.getString(R.string.lbl_party)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtDebit: String? = MyApp.getInstance().resources.getString(R.string.lbl_debit)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtTdb: String? = MyApp.getInstance().resources.getString(R.string.msg_total_debit_bal)
  ,
  /**
   * TODO Replace with dynamic value
   */
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
