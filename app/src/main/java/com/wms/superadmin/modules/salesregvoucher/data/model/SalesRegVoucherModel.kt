package com.wms.superadmin.modules.salesregvoucher.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class SalesRegVoucherModel(
  var txtOrder: String? = "",
  var txtDateParticulars: String? = "Date/Particulars",
  var txtParticulars: String? = MyApp.getInstance().resources.getString(R.string.lbl_particulars),
  var txtDebit: String? = MyApp.getInstance().resources.getString(R.string.lbl_debit),
  var txtCredit: String? = MyApp.getInstance().resources.getString(R.string.lbl_credit),
  var txtNetSales: String? = MyApp.getInstance().resources.getString(R.string.lbl_net_sales),
  var txtFromdate: String? = MyApp.getInstance().resources.getString(R.string.lbl_from_date),
  var txtTodate: String? = MyApp.getInstance().resources.getString(R.string.lbl_to_date),
  var txtTo: String? = MyApp.getInstance().resources.getString(R.string.lbl_to),
  var txtBranch: String? = MyApp.getInstance().resources.getString(R.string.lbl_branch),
  var txtLanguage: String? = MyApp.getInstance().resources.getString(R.string.lbl_total),
  var txtsalesregvoucherdebit: String? = "",
  var txtsalesregvouchercredit: String? = "",
  var txtsalesregvouchernetsales: String? = "",
  var txtFrom: String? = MyApp.getInstance().resources.getString(R.string.lbl_from)
)
