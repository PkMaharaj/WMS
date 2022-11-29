package com.wms.superadmin.modules.notificationapproval.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.network.models.notificationApprove.DLSalesOrderItemWarehouseMapForFiFO
import com.wms.superadmin.network.models.notificationApprove.DLSalesOrderItemWarehouseMapForNegativeStock
import com.wms.superadmin.network.models.notificationApprove.DLSalesOrderWithItemCreationsDiscount
import kotlin.String

data class NotificationApprovalRowModel(
  var txtItemName: String? = MyApp.getInstance().resources.getString(R.string.lbl_name1),
  var txtWarehousename: String? = MyApp.getInstance().resources.getString(R.string.lbl_mwb_dharwad),
  var txtqtyordered: String? = MyApp.getInstance().resources.getString(R.string.lbl_13_00),
  var txtnegativeqty: String? = MyApp.getInstance().resources.getString(R.string.lbl_13_00),
//  var txtitemdiscountTotalamt: String? = "",
  var txtdiscountpercent: Int? = 0,
  var txtchangeamt: Int? = 0,
  var sowithitemid: String? = "",
  var totalqty: Double? = 0.0,
  var itemdiscounts: Int? = 0,
  var approvenegativestock: Boolean? = false,
  var orderDiscountApprove: Boolean? = false,
  var orderDiscountPer: String? = "",
  var orderdiscountamt: String? = "",
  var salesordernum: String? = "",
  var itemcode: String? = "",
  var warehouseID: String? = "",
  var batchID: String? = "",
  var orgID: Int? = 0,
  var totalLinItemQuantity: String? = "",
  var approveitemDis: Boolean? = false,
  var changeItemDiscounts: String? = "",
  var rate: Double? = 0.0,
  var bagqty: String? = "",
  var changeQty: String? = "",
  var value: String? = "",
  var itemDiscountValue: String? = "",
  var changeqtyordered: String? = "",

  var isFifoSkiped: Boolean? = false,
  var isDiscountItem: Boolean? = false,
  var isNegitiveStock: Boolean? = false,
  var isOrderDiscountRangeExceeded: Boolean? = false,
  var isCreditLimitExceeded: Boolean? = false,
  var isFifoApproved: Boolean? = false,
  var isNegitiveApproved: Boolean? = false,
  var isDiscountApproved: Boolean? = false,
  )

fun NotificationApprovalRowModel.toDLSalesOrderItemWarehouseMapForFiFO(): DLSalesOrderItemWarehouseMapForFiFO{
  return DLSalesOrderItemWarehouseMapForFiFO(
    salesOrderWithItemID = this.sowithitemid!!,
    itemCode = this.itemcode!!,
    approveFiFo = this.isFifoSkiped,
    warehouseID = this.warehouseID!!,
    quantityOrdered = this.txtqtyordered.toString(),
    batchID = this.batchID.toString(),
    orgID = this.orgID!!,
  )
}

fun NotificationApprovalRowModel.toDLSalesOrderItemWarehouseMapForNegativeStock(): DLSalesOrderItemWarehouseMapForNegativeStock {
  return DLSalesOrderItemWarehouseMapForNegativeStock(
    salesOrderWithItemID = this.sowithitemid!!,
    warehouseID = this.warehouseID!!,
    orgID = this.orgID!!.toString(),
    isNegativeStock = this.isNegitiveStock!!.toString(),
    itemCode = this.itemcode!!,
    approveNegativeStock = this.isNegitiveStock!!,
    quantityOrdered = this.txtqtyordered.toString(),
    totalLinItemQuantity = this.totalLinItemQuantity.toString(),
    changeQty = this.changeQty?:"0",
    value = this.value!!,
    itemDiscountValue = this.itemDiscountValue!!,chngedNegativeQTy = this.changeQty?:"0"
  )
}

fun NotificationApprovalRowModel.toDLSalesOrderWithItemCreationsDiscount(): DLSalesOrderWithItemCreationsDiscount {
  return DLSalesOrderWithItemCreationsDiscount(
    salesOrderWithItemID = this.sowithitemid!!,
    itemCode = this.itemcode!!,
    approveitemDis = this.approveitemDis!!,
    itemDiscounts = this.itemdiscounts!!.toString(),
    changeItemDiscounts = this.changeItemDiscounts,
    rate = this.rate,
    totalLinItemQuantity = this.totalLinItemQuantity!!,
    bagQty = this.bagqty!!,
    value = this.value!!,
    itemDiscountValue = this.itemDiscountValue!!,
  )
}
