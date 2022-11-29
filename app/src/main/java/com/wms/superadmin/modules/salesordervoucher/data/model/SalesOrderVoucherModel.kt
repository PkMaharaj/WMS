package com.wms.superadmin.modules.salesordervoucher.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class SalesOrderVoucherModel(
  public var txtOrder: String? ="",
  public var txtFromdate: String? = MyApp.getInstance().resources.getString(R.string.lbl_from_date),
  public var txtFrom: String? = MyApp.getInstance().resources.getString(R.string.lbl_from),
  public var txtTodate: String? = MyApp.getInstance().resources.getString(R.string.lbl_to_date),
  public var txtTo: String? = MyApp.getInstance().resources.getString(R.string.lbl_to),
  public var txtBranch: String? = MyApp.getInstance().resources.getString(R.string.lbl_branch),
  public var txtMonthwise: String? = MyApp.getInstance().resources.getString(R.string.lbl_monthwise),
  public var txtDetailed: String? = MyApp.getInstance().resources.getString(R.string.lbl_detailed),
  public var txtDate: String? = MyApp.getInstance().resources.getString(R.string.lbl_date),
  public var txtParticulars: String? = MyApp.getInstance().resources.getString(R.string.lbl_particulars),
  public var txtLanguage: String? = MyApp.getInstance().resources.getString(R.string.lbl_vochertype),
  public var txtVocherNo: String? = MyApp.getInstance().resources.getString(R.string.lbl_vocherno),
  public var txtSalesType: String? = MyApp.getInstance().resources.getString(R.string.lbl_salestype),
  public var txtValue: String? = MyApp.getInstance().resources.getString(R.string.lbl_value2),
  public var txtTotalVoucherc: String? = MyApp.getInstance().resources.getString(R.string.msg_total_voucher_c),
  public var txt45: String? = MyApp.getInstance().resources.getString(R.string.lbl_45),
  public var txtValue1: String? = MyApp.getInstance().resources.getString(R.string.lbl_value),
  public var txt46: String? = MyApp.getInstance().resources.getString(R.string.lbl_45),
  public var txtvouchers: String? = "",
  public var totalvaluevoucher:String? = ""
)
