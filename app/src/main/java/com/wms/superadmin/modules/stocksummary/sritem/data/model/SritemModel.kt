package com.wms.superadmin.modules.stocksummary.sritem.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class SritemModel(
  /**
   * TODO Replace with dynamic value
   */
  var txtHeader: String? = MyApp.getInstance().resources.getString(R.string.lbl_mwb_dharwad)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtMWBDharwad: String? = MyApp.getInstance().resources.getString(R.string.lbl_mwb_dharwad)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtBranch: String? = MyApp.getInstance().resources.getString(R.string.lbl_branch)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtValue: String? = MyApp.getInstance().resources.getString(R.string.lbl_value)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtQuantity: String? = MyApp.getInstance().resources.getString(R.string.lbl_quantity)

)
