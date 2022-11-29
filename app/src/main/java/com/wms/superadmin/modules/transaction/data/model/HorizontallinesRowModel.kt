package com.wms.superadmin.modules.transaction.data.model


import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class HorizontallinesRowModel(
  /**
   * TODO Replace with dynamic value
   */
  var txtName1: String? = MyApp.getInstance().resources.getString(R.string.lbl_name1)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtMWBDharwad: String? = MyApp.getInstance().resources.getString(R.string.lbl_mwb_dharwad)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txt1300: String? = MyApp.getInstance().resources.getString(R.string.lbl_13_00)

)
