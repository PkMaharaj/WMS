package com.wms.superadmin.modules.salesordermonthly.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class SalesOrderMonthly1RowModel(
  public var txtMay: String? = MyApp.getInstance().resources.getString(R.string.lbl_hubli),
  public var txt10: String? = MyApp.getInstance().resources.getString(R.string.lbl_10),
  public var totalvalue: String? = ""
)
