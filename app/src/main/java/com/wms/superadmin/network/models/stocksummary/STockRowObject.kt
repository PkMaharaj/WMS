package com.wms.superadmin.network.models.stocksummary


import com.google.gson.annotations.SerializedName
import com.wms.superadmin.modules.stocksummary.sritem.data.model.StockReportRowModel
import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.math.roundToInt

data class STockRowObject(
    @SerializedName("alternateUnit")
    val alternateUnit: Any?,
    @SerializedName("BasicUnit")
    val basicUnit: Any?,
    @SerializedName("BranchID")
    val branchID: String?,
    @SerializedName("BranchName")
    val branchName: String?,
    @SerializedName("ClosingQty")
    val closingQty: Double?,
    @SerializedName("ClosingQtyRate")
    val closingQtyRate: Double?,
    @SerializedName("ClosingQtyValue")
    val closingQtyValue: Double?,
    @SerializedName("CommonID")
    val commonID: String?,
    @SerializedName("CreatedDate")
    val createdDate: String?,
    @SerializedName("CustID")
    val custID: String?,
    @SerializedName("DataState")
    val dataState: Int?,
    @SerializedName("DisplayMessage")
    val displayMessage: String?,
    @SerializedName("DisplayValue")
    val displayValue: String?,
    @SerializedName("FromDate")
    val fromDate: String?,
    @SerializedName("GetErrorSource")
    val getErrorSource: String?,
    @SerializedName("GetErrorStackTrace")
    val getErrorStackTrace: String?,
    @SerializedName("GetErrormessages")
    val getErrormessages: String?,
    @SerializedName("GroupID")
    val groupID: Int?,
    @SerializedName("GroupName")
    val groupName: String?,
    @SerializedName("InvoiceNumber")
    val invoiceNumber: String?,
    @SerializedName("InwardQTY")
    val inwardQTY: Double?,
    @SerializedName("InwardQTYRate")
    val inwardQTYRate: Double?,
    @SerializedName("InwardQTYValue")
    val inwardQTYValue: Double?,
    @SerializedName("ItemCode")
    val itemCode: String?,
    @SerializedName("ItemName")
    val itemName: String?,
    @SerializedName("MonthID")
    val monthID: Int?,
    @SerializedName("MonthName")
    val monthName: String?,
    @SerializedName("OpeningQTY")
    val openingQTY: Double?,
    @SerializedName("OpeningRate")
    val openingRate: Double?,
    @SerializedName("OpeningValue")
    val openingValue: Double?,
    @SerializedName("OrderDate")
    val orderDate: String?,
    @SerializedName("OrgID")
    val orgID: String?,
    @SerializedName("OutwardQTY")
    val outwardQTY: Double?,
    @SerializedName("OutwardQTYRate")
    val outwardQTYRate: Double?,
    @SerializedName("OutwardQTYValue")
    val outwardQTYValue: Double?,
    @SerializedName("PartyName")
    val partyName: String?,
    @SerializedName("ScreenName")
    val screenName: Int?,
    @SerializedName("SoPoNumber")
    val soPoNumber: String?,
    @SerializedName("strClosingQTY")
    val strClosingQTY: Any?,
    @SerializedName("strInwardQty")
    val strInwardQty: Any?,
    @SerializedName("strOpeningQty")
    val strOpeningQty: Any?,
    @SerializedName("strOutwardQty")
    val strOutwardQty: Any?,
    @SerializedName("SubCategoryId")
    val subCategoryId:Int?,
    @SerializedName("SubCategoryName")
    val subCategoryName: String?,
    @SerializedName("ToDate")
    val toDate: String?,
    @SerializedName("VoucherNo")
    val voucherNo: String?,
    @SerializedName("VoucherType")
    val voucherType: String?,
    @SerializedName("WarehouseID")
    val warehouseID: Int?,
    @SerializedName("WarehouseName")
    val warehouseName: String?,
    @SerializedName("Year")
    val year: Int?
)

public fun STockRowObject.torowMdel():StockReportRowModel{
    val Format = DecimalFormat("##.00")

    return StockReportRowModel(BranchID=this.branchID?:"0",
        BranchName = this.branchName?:"",
        ClosingQty = this.closingQty?:0.0,
        ClosingQtyRate = this.closingQtyRate?:0.0,
        ClosingQtyValue = this.closingQtyValue?.roundToInt()?:0,//Format.format(BigDecimal(this.closingQtyValue?:0.0)).toDoubleOrNull()?:0.0,
        CreatedDate = this.createdDate?:"",
        CustID = this.custID?:"",
        GroupID = this.groupID?:0,
        GroupName = this.groupName?:"",
        InvoiceNumber = this.invoiceNumber?:"",
        InwardQTY = this.inwardQTY?:0.0,
        InwardQTYRate = this.inwardQTYRate?:0.0,
        InwardQTYValue = this.inwardQTYValue?.roundToInt()?:0,//Format.format(BigDecimal(this.inwardQTYValue?:0.0)).toDoubleOrNull()?:0.0,
        ItemCode =this.itemCode?:"",
        ItemName = this.itemName?:"",
        MonthID =this.monthID?:0,
        MonthName =this.monthName?:"",
        OpeningQTY = this.openingQTY?:0.0,
        OpeningRate = this.openingRate?:0.0,
        OpeningValue = this.openingValue?.roundToInt()?:0,//Format.format(BigDecimal(this.openingValue?:0.0)).toDoubleOrNull()?:0.0,
        OrderDate = this.orderDate?:"",
        OrgID = this.orgID?:"",
        OutwardQTY = this.outwardQTY?:0.0,
        OutwardQTYRate = this.outwardQTYRate?:0.0,
        OutwardQTYValue = this.outwardQTYValue?.roundToInt()?:0,//Format.format(BigDecimal(this.outwardQTYValue?:0.0)).toDoubleOrNull()?:0.0,
        PartyName = this.partyName?:"",SoPoNumber = this.soPoNumber?:"",
       SubCategoryId = this.subCategoryId?:0,
        SubCategoryName = this.subCategoryName?:"",
        VoucherNo = this.commonID?:"",
        VoucherType = if(this.voucherType!=null)" ${this.voucherType} : " else " VoucherType : ",
        WarehouseID = this.warehouseID?:0,
        WarehouseName = this.warehouseName?:"",Year = this.year?:0)
}