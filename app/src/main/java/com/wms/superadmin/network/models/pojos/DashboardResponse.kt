package com.wms.superadmin.network.models.pojos

import com.google.gson.annotations.SerializedName

data class DashboardResponse(

    @field:SerializedName("TodaySales")
    val sales: String? = null,

    @field:SerializedName("TodayPurchase")
    val purchase: String? = null,

    @field:SerializedName("TillDatePayables")
    val payable: String? = null,

    @field:SerializedName("TillDateReceivables")
    val receivable: String? = null,
    @field:SerializedName("CashBalnace")
    val cashBook: String? = null,
    @field:SerializedName("BankBalance")
    val bankBook: String? = null,
    @field:SerializedName("StockBalance")
    val stock: String? = null,

    )
