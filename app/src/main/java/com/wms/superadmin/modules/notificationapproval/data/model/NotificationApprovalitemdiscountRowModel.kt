package com.wms.superadmin.modules.notificationapproval.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class NotificationApprovalitemdiscountRowModel(
  var txtName1: String? = MyApp.getInstance().resources.getString(R.string.lbl_name1),
  var txtMWBDharwad: String? = MyApp.getInstance().resources.getString(R.string.lbl_mwb_dharwad),
  var txt1300: String? = MyApp.getInstance().resources.getString(R.string.lbl_13_00),
  var txt10: String? = MyApp.getInstance().resources.getString(R.string.lbl_10)
)
