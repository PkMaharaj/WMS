package com.wms.superadmin.modules.dashboardsuperadmin.`data`.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class DashboardSuperadmin2RowModel(
  /**
   * TODO Replace with dynamic value
   */
  public var txtLanguage: String? = MyApp.getInstance().resources.getString(R.string.lbl_cash)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtLanguage1: String? = MyApp.getInstance().resources.getString(R.string.lbl_1000)

)
