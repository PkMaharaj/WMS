package com.wms.superadmin.modules.salesregbranches.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class SalesRegBranchesModel(
  var txtOrder: String? = "",
  var txtBranch: String? = MyApp.getInstance().resources.getString(R.string.lbl_branch),
  var txtDebit: String? = MyApp.getInstance().resources.getString(R.string.lbl_debit),
  var txtCredit: String? = MyApp.getInstance().resources.getString(R.string.lbl_credit),
  var txtNetSales: String? = "",
  var txtFromdate: String? = MyApp.getInstance().resources.getString(R.string.lbl_from_date),
  var txtTodate: String? = MyApp.getInstance().resources.getString(R.string.lbl_to_date),
  var txtTo: String? = MyApp.getInstance().resources.getString(R.string.lbl_to),
  var txtLanguage: String? = MyApp.getInstance().resources.getString(R.string.lbl_total),
  var txtTotaldebitBranch: String? = "",
  var txtTotalnetsalesBranch: String? = "0",
//    MyApp.getInstance().resources.getString(R.string.lbl_45),
  var txtTotalcreditBranch: String? = "",
  var txtFrom: String? = "From"
)
