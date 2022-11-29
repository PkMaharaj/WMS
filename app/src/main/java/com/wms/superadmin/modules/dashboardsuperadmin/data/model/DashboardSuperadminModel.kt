package com.wms.superadmin.modules.dashboardsuperadmin.`data`.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class DashboardSuperadminModel(
  /**
   * TODO Replace with dynamic value
   */
  public var txtWMS: String? = MyApp.getInstance().resources.getString(R.string.lbl_w_m_s)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var etGroup2Value: String? = null,
  public var Sales: String? = "00",
  public var Purchase: String? = "00",
  public var Payables: String? = "00",
  public var Receivables: String? = "00",
  public var CashBook: String? = "00",
  public var BankBook: String? = "00",
  public var Stock: String? = "00",
)
