package com.wms.superadmin.modules.salesorderbranch.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class SalesOrderBranchModel(
  public var txtOrder: String? = "",
  public var txtBranch: String? = MyApp.getInstance().resources.getString(R.string.lbl_branch),
  public var txtTotalVouchers: String? = MyApp.getInstance().resources.getString(R.string.lbl_total_vouchers),
  public var txtVoucherwise: String? = MyApp.getInstance().resources.getString(R.string.lbl_voucherwise),
  public var txtMonthwise: String? = MyApp.getInstance().resources.getString(R.string.lbl_monthwise),
  public var txtFromdate: String? = MyApp.getInstance().resources.getString(R.string.lbl_from_date),
  public var txtTodate: String? = MyApp.getInstance().resources.getString(R.string.lbl_to_date),
  public var txtTo: String? = MyApp.getInstance().resources.getString(R.string.lbl_to),
  public var txtFrom: String? = MyApp.getInstance().resources.getString(R.string.lbl_from),
  public var txtTotalVoucherc: String? = MyApp.getInstance().resources.getString(R.string.msg_total_voucher_c),
  public var totalvouchercount: String? = "",
//  = MyApp.getInstance().resources.getString(R.string.lbl_45)
  public var txtValue: String? = MyApp.getInstance().resources.getString(R.string.lbl_value),
  public var txt46: String? = MyApp.getInstance().resources.getString(R.string.lbl_45),
  public var txtheadingvalue: String? = "Value",
  public var totalvalue: String? = ""

)
