package com.wms.superadmin.modules.salesregday.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class SalesRegDay1RowModel(
  var txtDateSalesregday: String? = "",
  var txtDebitSalesregday: String? = "",
  var txtCreditSalesregday: String? = "",
  var txtNetsalesSalesregday: String? = MyApp.getInstance().resources.getString(R.string.lbl_10),
  var branchid: String? = "",
  var branchname: String? = "",
  var creationdate: String? = ""
)
