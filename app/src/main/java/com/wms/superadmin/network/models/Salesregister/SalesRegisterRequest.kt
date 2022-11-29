package com.wms.superadmin.network.models.Salesregister

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SalesRegisterRequest(

	@field:SerializedName("OrgID")
	val orgID: String? = null,

	@field:SerializedName("ScreenName")
	val screenName: String? = null,

	@field:SerializedName("FromDate")
	val fromDate: String? = null,

	@field:SerializedName("ToDate")
	val toDate: String? = null,

	@field:SerializedName("BranchID")
	val branchID: String? = null
): Serializable
