package com.wms.superadmin.modules.stocksummary.sritem.data.model

import com.google.gson.annotations.SerializedName
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class StockReportRowModel(
val BranchID: String?="",
val BranchName: String?="",
val ClosingQty: Double?=0.0,
val ClosingQtyRate: Double?=0.0,
val ClosingQtyValue: Int?=0,
val CreatedDate: String?,
val CustID:  String?="",
val GroupID: Int?=0,
val GroupName: String?,
val InvoiceNumber: String?="",
val InwardQTY: Double?=0.0,
val InwardQTYRate: Double?=0.0,
val InwardQTYValue: Int?=0,
val ItemCode: String?="",
val ItemName: String?="",
val MonthID: Int?,
val MonthName: String?="",
val OpeningQTY: Double?=0.0,
val OpeningRate: Double?=0.0,
val OpeningValue: Int?=0,
val OrderDate: String?="",
val OrgID: String?="",
val OutwardQTY: Double?=0.0,
val OutwardQTYRate: Double?=0.0,
val OutwardQTYValue: Int?=0,
val PartyName: String?="",
val SoPoNumber: String?="",
val StrClosingQTY: String?="",
val StrInwardQty:  String?="",
val StrOpeningQty:  String?="",
val StrOutwardQty:  String?="",
val SubCategoryId:  Int?=0,
val SubCategoryName: String?="",
val VoucherNo: String?,
val VoucherType: String?,
val WarehouseID: Int?=0,
val WarehouseName:  String?="",
val Year: Int?=0
)


