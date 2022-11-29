package com.wms.superadmin.modules.notificationWMS.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class NotificationWMSRowModel(
  public var txtLanguage: String? = MyApp.getInstance().resources.getString(R.string.lbl_receipts),
  public var txt1122021: String? = MyApp.getInstance().resources.getString(R.string.lbl_11_2_2021),
  public var txt200: String? = MyApp.getInstance().resources.getString(R.string.lbl_200),
  public var txtSOnum: String? = "",
  public var date: String? = ""
)
