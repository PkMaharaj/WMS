package com.wms.superadmin.modules.salesregmonth.`data`.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class SalesRegMonthModel(
  var txtOrder: String? = "",
  var txtMonth: String? = MyApp.getInstance().resources.getString(R.string.lbl_month),
  var txtDebit: String? = MyApp.getInstance().resources.getString(R.string.lbl_debit),
  var txtCredit: String? = MyApp.getInstance().resources.getString(R.string.lbl_credit),
  var txtNetSales: String? = "",
  var txtFromdate: String? = MyApp.getInstance().resources.getString(R.string.lbl_from_date),
  var txtFrom: String? = MyApp.getInstance().resources.getString(R.string.lbl_from),
  var txtTodate: String? = MyApp.getInstance().resources.getString(R.string.lbl_to_date),
  var txtTo: String? = MyApp.getInstance().resources.getString(R.string.lbl_to),
  var txtLanguage: String? = MyApp.getInstance().resources.getString(R.string.lbl_total),
  var txtTotaldebitSalesregbranch: String? = "",
  var txtTotalnetsalesSalesregbranch:String? =  "",
  var txtTotalcreditSalesregbranch: String? = ""
)
