package com.wms.superadmin.modules.salesregvoucher.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class SalesRegVoucher1RowModel(
  var txtDate: String? = MyApp.getInstance().resources.getString(R.string.lbl_16_05_2022),
  var txtdebitsalesregvoucher: String? = "",
  var txtcreditsalesregvoucher: String? = "",
  var txtParticulars: String? = MyApp.getInstance().resources.getString(R.string.lbl_particulars),
  var mergeddetails: String? = "",
  var voucherid: String? = "",
  var Vouchertype: String = ""
)
