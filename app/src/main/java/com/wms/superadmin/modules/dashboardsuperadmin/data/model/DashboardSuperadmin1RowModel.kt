package com.wms.superadmin.modules.dashboardsuperadmin.`data`.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class DashboardSuperadmin1RowModel(
  /**
   * TODO Replace with dynamic value
   */
  public var txtLanguage1: String? = MyApp.getInstance().resources.getString(R.string.lbl_purchase)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtCategory1: String? = MyApp.getInstance().resources.getString(R.string.lbl_sales)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtLanguage3: String? = MyApp.getInstance().resources.getString(R.string.lbl_1000_10)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtLanguage4: String? = MyApp.getInstance().resources.getString(R.string.lbl_1000_10)

)
