package com.wms.superadmin.modules.transaction.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class TransactionModel(
  /**
   * TODO Replace with dynamic value
   */
  var txtPayables: String? = MyApp.getInstance().resources.getString(R.string.lbl_payables)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtTotalValue: String? = MyApp.getInstance().resources.getString(R.string.lbl_total_value)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtTxtvalue: String? = MyApp.getInstance().resources.getString(R.string.lbl_180000)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtTxtfrom: String? = MyApp.getInstance().resources.getString(R.string.lbl_from)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtTxtTo: String? = MyApp.getInstance().resources.getString(R.string.lbl_to)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtTxtdatefield: String? = MyApp.getInstance().resources.getString(R.string.lbl_fromdate)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtTxtfieldd: String? = MyApp.getInstance().resources.getString(R.string.lbl_todate)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtItemName: String? = MyApp.getInstance().resources.getString(R.string.lbl_item_name)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtBranch: String? = MyApp.getInstance().resources.getString(R.string.lbl_branch)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtBillAmount: String? = MyApp.getInstance().resources.getString(R.string.lbl_bill_amount)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtTotalReceivabl: String? =
      MyApp.getInstance().resources.getString(R.string.msg_total_receivabl),

  var txtFromdate: String? = "",
var TotalBalance:String?="",
var TotalBillAmount:String?=""

)
