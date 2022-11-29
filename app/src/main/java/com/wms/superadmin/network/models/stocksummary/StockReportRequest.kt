package com.wms.superadmin.network.models.stocksummary


import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class StockReportRequest(
    @SerializedName("FromDate")
    var fromDate: String?= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()),
    @SerializedName("ToDate")
    var toDate: String?=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()),
    @SerializedName("OrgID")
    var orgID: String?="0",
    @SerializedName("BranchID")
    var branchID: String?="0",
    @SerializedName("inwardrate")
    var inwardrate: String?="0",
    @SerializedName("WarehouseID")
    var warehouseID: String?="0",
    @SerializedName("GroupID")
    var groupID: String?="",
    @SerializedName("ItemCode")
    var itemCode: String?="",
    @SerializedName("ismonthcheckbox")
    var ismonthcheckbox: Boolean?=false,
    @SerializedName("SubCategoryId")
    var subCategoryId: String?="0",
    @SerializedName("ScreenName")
    var screenName: String?="0"
):Serializable