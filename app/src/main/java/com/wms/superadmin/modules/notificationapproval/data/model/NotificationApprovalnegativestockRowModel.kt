package com.wms.superadmin.modules.notificationapproval.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class NotificationApprovalnegativestockRowModel(
  var txtName1: String? = MyApp.getInstance().resources.getString(R.string.lbl_name1),
  var txtMWBDharwad: String? = MyApp.getInstance().resources.getString(R.string.lbl_mwb_dharwad),
  var txt1300: String? = MyApp.getInstance().resources.getString(R.string.lbl_13_00),
  var txt1302: String? = MyApp.getInstance().resources.getString(R.string.lbl_13_00),

  var isFifoSkiped: Boolean? = null,
  var isDiscountIten: Boolean? = null,
  var isNegitiveStock: Boolean? = null,
)
