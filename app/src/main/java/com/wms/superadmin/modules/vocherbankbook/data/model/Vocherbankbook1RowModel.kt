package com.wms.superadmin.modules.vocherbankbook.`data`.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class Vocherbankbook1RowModel(
  /**
   * TODO Replace with dynamic value
   */
  public var LedgerDate: String? = ""
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var Party: String? = ""
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var VocherType: String? = ""
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var VocherTypeNo: String? = ""
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var Credit: String? = ""
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var Debit: String? ="",
  public var Description: String? ="",

  public var BranchName: String? ="",
  public var BranchId: String? ="",
  public var LedgerId: String? ="",
  public var ClosingBal: String? = ""

  )
