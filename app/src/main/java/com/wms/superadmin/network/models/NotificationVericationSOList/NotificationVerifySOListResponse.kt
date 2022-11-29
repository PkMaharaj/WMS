package com.wms.superadmin.network.models.NotificationVericationSOList

import com.google.gson.annotations.SerializedName
import com.wms.superadmin.modules.notificationapproval.data.model.NotificationApprovalModel

data class NotificationVerifySOListResponse(

	@field:SerializedName("ItemDetails")
	val itemDetails: List<NotificationResponseItemDetailsItem?>? = null,

	@field:SerializedName("SalesOrderDetails")
	val salesOrderDetails: NotificationResponseSalesOrderDetails? = null
)

data class NotificationResponseSalesOrderDetails(

	@field:SerializedName("IsBillsExceeded")
	val isBillsExceeded: Boolean? = null,

	@field:SerializedName("CustID")
	val custID: String? = null,

	@field:SerializedName("CrLimit")
	val crLimit: String? = null,

	@field:SerializedName("IsCreditLimitExceeded")
	val isCreditLimitExceeded: Boolean? = null,

	@field:SerializedName("NewCrdLimit")
	val newCrdLimit: String? = null,

	@field:SerializedName("NewCrdBills")
	val newCrdBills: String? = null,

	@field:SerializedName("NewCrdDays")
	val newCrdDays: String? = null,

	@field:SerializedName("NoofOutstandingBill")
	val noofOutstandingBill: Int? = null,

	@field:SerializedName("IsDirectSO")
	val isDirectSO: Boolean? = null,

	@field:SerializedName("CrDays")
	val crDays: String? = null,

	@field:SerializedName("IsCreditDaysExceeded")
	val isCreditDaysExceeded: Boolean? = null
)

data class NotificationResponseItemDetailsItem(

	@field:SerializedName("ApproveNegativeStock")
	val approveNegativeStock: Boolean? = null,

	@field:SerializedName("ItemCode")
	val itemCode: String? = null,

	@field:SerializedName("BatchID")
	val batchID: Int? = null,

	@field:SerializedName("OrderDiscountValue")
	val orderDiscountValue: Int? = null,

	@field:SerializedName("BagQty")
	val bagQty: String? = null,

	@field:SerializedName("Value")
	val value: String? = null,

	@field:SerializedName("ItemDiscountValue")
	val itemDiscountValue: String? = null,

	@field:SerializedName("WarehouseID")
	val warehouseID: Int? = null,

	@field:SerializedName("WarehouseName")
	val warehouseName: String? = null,

	@field:SerializedName("ItemDiscounts")
	val itemDiscounts: Int? = null,

	@field:SerializedName("ChangeItemDiscounts")
	val changeItemDiscounts: Int? = null,

	@field:SerializedName("OrgID")
	val orgID: Int = 0,

	@field:SerializedName("IsItemDiscountExceeded")
	val isItemDiscountExceeded: Boolean? = null,

	@field:SerializedName("ItemName")
	val itemName: String? = null,

	@field:SerializedName("SalesOrderWithItemID")
	val salesOrderWithItemID: String? = null,

	@field:SerializedName("IsNegativeStock")
	val isNegativeStock: Boolean? = null,

	@field:SerializedName("ApproveitemDis")
	val approveitemDis: Boolean? = null,

	@field:SerializedName("OrderDiscountAmt")
	val orderDiscountAmt: Double? = null,

	@field:SerializedName("Rate")
	val rate: Double? = null,

	@field:SerializedName("TotalLinItemQuantity")
	val totalLinItemQuantity: Double? = null,

	@field:SerializedName("ChangeQty")
	val changeQty: Int? = null,

	@field:SerializedName("IsOrderDiscountRangeExceeded")
	val isOrderDiscountRangeExceeded: Boolean? = false,

	@field:SerializedName("TotalItemNegativeStock")
	val totalItemNegativeStock: Int? = null,

	@field:SerializedName("SalesOrderNumber")
	val salesOrderNumber: String? = null,

	@field:SerializedName("ApproveOrderDiscount")
	val approveOrderDiscount: Boolean? = null,

	@field:SerializedName("IsFIFOSkipped")
	val isFIFOSkipped: Boolean? = null,

	@field:SerializedName("QuantityOrdered")
	val quantityOrdered: Int? = null
)
