package com.wms.superadmin.network.models.notificationApprove

import com.google.gson.annotations.SerializedName

data class NotificationApproveObject(

    @field:SerializedName( "IsCustomerCrdExceeded")
    val isCustomerCrdExceeded: Boolean? = false,

    @field:SerializedName("CustID")
    val custID: String="",

    @field:SerializedName("CrLimit")
    val crLimit: String="",

    @field:SerializedName("CrDays")
    val crDays: String="",

    @field:SerializedName("NoofOutstandingBill")
    val noofOutstandingBill: String="",

    @field:SerializedName("ModifiedByID")
    val modifiedByID: String="",

    @field:SerializedName("SalesorderNumber")
    val salesorderNumber: String="",

    @field:SerializedName("OrderDiscountApprove")
    val orderDiscountApprove: Boolean? = false,

    @field:SerializedName("IsOrderDiscountExceeded")
    val isOrderDiscountExceeded: Boolean?=false,

    @field:SerializedName("OrderDiscountPer")
    val orderDiscountPer: String="",

    @field:SerializedName("OrderDiscountAmt")
    val orderDiscountAmt: String="",

    @field:SerializedName("IsNegativeStockTaken")
    val isNegativeStockTaken: Boolean? = false,

    @field:SerializedName("IsFifoskipped")
    val isFifoskipped: Boolean? = false,

    @field:SerializedName("IsItemDiscountExceeded")
    val isItemDiscountExceeded: Boolean? = false,

//    NewCrdBills,NewCrdDays,NewCrdLimit
    @field:SerializedName("NewCrdLimit")
    val newCrdLimit: String? = "",

    @field:SerializedName("NewCrdBills")
    val newCrdBills: String? = "",

    @field:SerializedName("NewCrdDays")
    val newCrdDays: String? = "",

//    val dLSalesOrderWithItemCreations: List<DLSalesOrderWithItemCreation>,

    @field:SerializedName("dLSalesOrderItemWarehouseMapForFiFO")
    val dLSalesOrderItemWarehouseMapForFiFO: List<DLSalesOrderItemWarehouseMapForFiFO>? = null,

    @field:SerializedName("dLSalesOrderItemWarehouseMapForNegativeStock")
    val dLSalesOrderItemWarehouseMapForNegativeStock: List<DLSalesOrderItemWarehouseMapForNegativeStock>? = null,

    @field:SerializedName("dLSalesOrderWithItemCreationsDiscounts")
    val dLSalesOrderWithItemCreationsDiscounts: List<DLSalesOrderWithItemCreationsDiscount>
)

data class DLSalesOrderItemWarehouseMapForFiFO (
    @field:SerializedName("SalesOrderWithItemID")
    val salesOrderWithItemID: String="",

    @field:SerializedName("ItemCode")
    val itemCode: String="",

    @field:SerializedName("ApproveFiFo")
    val approveFiFo: Boolean? = false,

    @field:SerializedName("WarehouseID")
    val warehouseID: String="",

    @field:SerializedName("QuantityOrdered")
    val quantityOrdered: String="",

    @field:SerializedName("BatchID")
    val batchID: String="",

    @field:SerializedName("OrgID")
    val orgID: Int? = 0
)

data class DLSalesOrderItemWarehouseMapForNegativeStock(
    @field:SerializedName("SalesOrderWithItemID")
    val salesOrderWithItemID: String="",

    @field:SerializedName("WarehouseID")
    val warehouseID: String="",

    @field:SerializedName("OrgID")
    val orgID: String? = "",

    @field:SerializedName("IsNegativeStock")
    val isNegativeStock: String="",

    @field:SerializedName("ItemCode")
    val itemCode: String="",

    @field:SerializedName("ApproveNegativeStock")
    val approveNegativeStock: Boolean? = false,

    @field:SerializedName("QuantityOrdered")
    val quantityOrdered: String="",

    @field:SerializedName("ChangeQty")
    val changeQty: String="",

    @field:SerializedName("TotalLinItemQuantity")
    val totalLinItemQuantity: String="",

    @field:SerializedName("ChngedNegativeQTy")
    val chngedNegativeQTy: String="",

    @field:SerializedName("Value")
    val value: String? = "",

    @field:SerializedName("ItemDiscountValue")
    val itemDiscountValue: String? = ""
)

data class DLSalesOrderWithItemCreationsDiscount(
    @field:SerializedName("SalesOrderWithItemID")
    val salesOrderWithItemID:String ="",

    @field:SerializedName("ItemCode")
    val itemCode: String="",

    @field:SerializedName("ApproveitemDis")
    val approveitemDis: Boolean = false,

    @field:SerializedName("ItemDiscounts")
    val itemDiscounts: String="",

    @field:SerializedName("ChangeItemDiscounts")
    val changeItemDiscounts: String? = "",

    @field:SerializedName("Rate")
    val rate: Double? = 0.0,

    @field:SerializedName("TotalLinItemQuantity")
    val totalLinItemQuantity: String="",

    @field:SerializedName("BagQty")
    val bagQty: String="",

    @field:SerializedName("Value")
    val value: String? = "",

    @field:SerializedName("ItemDiscountValue")
    val itemDiscountValue: String? = ""
)


data class DLSalesOrderWithItemCreation (
    @field:SerializedName("SalesOrderWithItemID")
    val salesOrderWithItemID: String="",

    @field:SerializedName("Approved")
    val approved: String="",

    @field:SerializedName("TotalQTY")
    val totalQTY: String="",

    @field:SerializedName("Value")
    val value: String=""
)
