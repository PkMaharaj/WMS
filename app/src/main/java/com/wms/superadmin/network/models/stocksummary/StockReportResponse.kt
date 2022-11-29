package com.wms.superadmin.network.models.stocksummary

import com.google.gson.annotations.SerializedName

class StockReportResponse (
    @SerializedName("StockReportitemListObject")
    var StockReportitemList: ArrayList<STockRowObject>?=null

)