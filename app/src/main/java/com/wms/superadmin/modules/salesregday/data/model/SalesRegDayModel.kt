package com.wms.superadmin.modules.salesregday.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class SalesRegDayModel(
  var txtOrder: String? = "",
  var txtDate: String? = MyApp.getInstance().resources.getString(R.string.lbl_date),
  var txtDebit: String? = MyApp.getInstance().resources.getString(R.string.lbl_debit),
  var txtCredit: String? = MyApp.getInstance().resources.getString(R.string.lbl_credit),
  var txtNetSales: String? = "",
  var txtFromdate: String? = MyApp.getInstance().resources.getString(R.string.lbl_from_date),
  var txtTodate: String? = MyApp.getInstance().resources.getString(R.string.lbl_to_date),
  var txtTo: String? = MyApp.getInstance().resources.getString(R.string.lbl_to),
  var txtLanguage: String? = MyApp.getInstance().resources.getString(R.string.lbl_total),
  var txtTotaldebitSalesregday: String? = "",
  var txtTotalnetsalesSalesregday: String? = "",
  var txtTotalcreditSalesregday: String? = "",
  var txtFrom: String? = MyApp.getInstance().resources.getString(R.string.lbl_from)
)
