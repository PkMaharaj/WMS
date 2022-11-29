package com.wms.superadmin.modules.salesregmonth.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class SalesRegMonth1RowModel(
  var txtMonthSalesregbranch: String? = MyApp.getInstance().resources.getString(R.string.lbl_may),
  var txtDebitSalesregbranch: String = "",
//    MyApp.getInstance().resources.getString(R.string.lbl_100),
  var txtCreditSalesregbranch: String = "",
//    MyApp.getInstance().resources.getString(R.string.lbl_100),
  var txtNetsalesSalesregbranch: String? = MyApp.getInstance().resources.getString(R.string.lbl_10),
  var branchid: String? = "",
  var branchname: String? = "",
  var monthID: Int? = 0
)
