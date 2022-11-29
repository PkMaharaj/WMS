package com.wms.superadmin.modules.salesorderbranch.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class SalesOrderRowModel(

  public var so_branchname: String? = "",
//  = MyApp.getInstance().resources.getString(R.string.lbl_hubli)
  public var so_totalvouchercount: String? = "",
//  = MyApp.getInstance().resources.getString(R.string.lbl_10),
  public var branchID: Int = 0,
  public var so_value: String = "",
  public var txtMonth: String? = MyApp.getInstance().resources.getString(R.string.lbl_hubli),
  public var monthID: Int? = 0,
  public var branchidM: Int = 0,
  public var txtvouchers: String? = MyApp.getInstance().resources.getString(R.string.lbl_10),
  public var txtDate: String? = MyApp.getInstance().resources.getString(R.string.lbl_16_05_2022),
  public var txtOne: String? = MyApp.getInstance().resources.getString(R.string.lbl_one),
  public var txtvouchernumber: String? = "",
//    MyApp.getInstance().resources.getString(R.string.lbl_10),
  public var txtsalestype: String? = MyApp.getInstance().resources.getString(R.string.lbl_abcd),
  public var txtvalue: String? = MyApp.getInstance().resources.getString(R.string.lbl_55),
  public var totalvalue: String? = "",
  public var txtparticulars: String? = "",
  public var txtmerged_details: String? = "",
  public var ordernumber: String? = "",
  public var Vouchertype: String? = ""
)
