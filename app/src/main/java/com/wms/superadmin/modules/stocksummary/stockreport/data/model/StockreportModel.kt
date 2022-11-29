package com.wms.superadmin.modules.stocksummary.stockreport.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class StockreportModel(
  /**
   * TODO Replace with dynamic value
   */
  var txtStockReport: String? = MyApp.getInstance().resources.getString(R.string.lbl_stock_report)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtInQty: String? = MyApp.getInstance().resources.getString(R.string.lbl_in_qty)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txt: String? = MyApp.getInstance().resources.getString(R.string.lbl)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txt20: String? = MyApp.getInstance().resources.getString(R.string.lbl_20)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtInValue: String? = MyApp.getInstance().resources.getString(R.string.lbl_in_value)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txt1: String? = MyApp.getInstance().resources.getString(R.string.lbl)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtTotal: String? = MyApp.getInstance().resources.getString(R.string.lbl_total)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtZipcode: String? = MyApp.getInstance().resources.getString(R.string.lbl_100000)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtFrom: String? = MyApp.getInstance().resources.getString(R.string.lbl_from)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtTo: String? = MyApp.getInstance().resources.getString(R.string.lbl_to)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtFromDate: String? = MyApp.getInstance().resources.getString(R.string.lbl_fromdate)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtToDate: String? = MyApp.getInstance().resources.getString(R.string.lbl_todate)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtFrom1: String? = MyApp.getInstance().resources.getString(R.string.lbl_from)
  ,
  /**
   * TODO Replace with dynamic value
   */
  var txtTo1: String? = MyApp.getInstance().resources.getString(R.string.lbl_to)

)
