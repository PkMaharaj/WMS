package com.wms.superadmin.modules.notificationapproval.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class NotificationApprovalModel(
  var custID: String? = "",
  var txtSalesOrderApp: String? = MyApp.getInstance().resources.getString(R.string.msg_sales_order_app),
  var txtNegativeStock: String? = MyApp.getInstance().resources.getString(R.string.msg_negative_stock),
  var txtCustomercredit: String? = MyApp.getInstance().resources.getString(R.string.msg_customer_credit),
  var txtOrderdiscount: String? = MyApp.getInstance().resources.getString(R.string.msg_order_discount),
  var txtItemName: String? = MyApp.getInstance().resources.getString(R.string.lbl_item_name),
  var txtWarehousename: String? = MyApp.getInstance().resources.getString(R.string.lbl_warehouse_name),
  var txtQuantityordere: String? = MyApp.getInstance().resources.getString(R.string.msg_quantity_ordere),
  var txtNegativeQuanti: String? = MyApp.getInstance().resources.getString(R.string.msg_negative_quant),
  var txtLanguage: String? = MyApp.getInstance().resources.getString(R.string.msg_order_number),
  var txtApprove: String? = MyApp.getInstance().resources.getString(R.string.lbl_approve),
  var txtItemName1: String? = MyApp.getInstance().resources.getString(R.string.lbl_item_name),
  var txtWarehousename1: String? = MyApp.getInstance().resources.getString(R.string.lbl_warehouse_name),
  var txtQuantityordere1: String? = MyApp.getInstance().resources.getString(R.string.msg_quantity_ordere),
  var txtItemName2: String? = MyApp.getInstance().resources.getString(R.string.lbl_item_name),
  var txtWarehousename2: String? = MyApp.getInstance().resources.getString(R.string.lbl_warehouse_name),
  var txtTotalamount: String? = MyApp.getInstance().resources.getString(R.string.lbl_total_amount),
  var txtDiscount: String? = MyApp.getInstance().resources.getString(R.string.lbl_discount),
  var txtChangeamount: String? = MyApp.getInstance().resources.getString(R.string.lbl_change_amount),
  var txtFIFOSKIPPED: String? = MyApp.getInstance().resources.getString(R.string.lbl_fifo_skipped),
  var txtItemdiscount: String? = MyApp.getInstance().resources.getString(R.string.msg_item_discount_d),
  var txtreasonofrejection: String? = "",
  var oldcreditlmt: String? = "",
  var newcreditlmt: String? = "",
  var totalqtyValue: String? = null,
  var orderdiscount: String? = null,
  var orderchangeamt: String? = null,
  var txtApprpove: String? = "Approve",
  var oldcreditdays: String? = null,
  var newcreditdays: String? = null,
  var oldoutstandingbill: String? = null,
  var newoutstandingbill: String? = null,
  var isCreditLimitExceeded: Boolean? = false,
  var salesOrderNumber: String? = null,
  var IsOrderDiscountRangeExceeded: Boolean? = false,
  var IsItemDiscountExceeded: Boolean? = false,
  var IsNegitiveStockTaken: Boolean? = false,
  var IsFifoSkipped: Boolean? = false,
  var OrderDiscountApprove: Boolean? = false,
  var isCreditDaysExceeded: Boolean? = false,
  var isBillsExceeded: Boolean? = false
)
