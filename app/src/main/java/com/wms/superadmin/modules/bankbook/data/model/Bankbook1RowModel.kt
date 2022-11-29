package com.wms.superadmin.modules.bankbook.`data`.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class Bankbook1RowModel(
  /**
   * TODO Replace with dynamic value
   */
  public var BranchName: String? ="",
  public var BranchId: String? ="",

  /**
   * TODO Replace with dynamic value
   */
  public var OpeningBalance: String? =""
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var Debit: String? = ""
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var Credit: String? = ""
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var ClosingBal: String? = "",
  public var LedgerName: String? = "",
  public var LedgerId: String? = "",
  public var groupname: String? = "",
  public var groupid:  String? = ""
)

