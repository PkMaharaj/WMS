package com.wms.superadmin.network.models.createsalesorderreport

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SalesOrderReportRequest(

	@field:SerializedName("OrgID")
	var orgID: String? = "",

	@field:SerializedName("ScreenName")
	var screenName: String? = "",

	@field:SerializedName("IsDetailedVoucher")
	var isDetailedVoucher: Boolean? = false,

	@field:SerializedName("FromDate")
	var fromDate: String? = "",

	@field:SerializedName("ToDate")
	var toDate: String? = "",

	@field:SerializedName("BranchID")
	var branchID: String? = ""
): Serializable
