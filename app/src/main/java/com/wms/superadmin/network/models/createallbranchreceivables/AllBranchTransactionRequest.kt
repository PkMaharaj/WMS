package com.wms.superadmin.network.models.createallbranchreceivables

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TransactionRequest(

	@field:SerializedName("OrgID")
	val orgID: Int? = null,

	@field:SerializedName("AgeingwiseProperties")
	val agewiseProperties: AgingRequestItem? = null,

	@field:SerializedName("ScreenName")
	val screenName: String? = null,

	@field:SerializedName("FromDate")
	val fromDate: String? = null,

	@field:SerializedName("ToDate")
	val toDate: String? = null,

	@field:SerializedName("BranchID")
	val branchID: String? = null,

	@field:SerializedName("ReportType")
	val reportType: String? = null
):Serializable

data class AgingRequestItem(

	@field:SerializedName("BillByDate")
	var billByDate: Boolean? = false,

	@field:SerializedName("FromDays1")
	var fromDays1: Int? = 0,

	@field:SerializedName("IsAgewiseSelected")
	var isAgewiseSelected: Boolean? = false,

	@field:SerializedName("BillByDueDate")
	var billByDueDate: Boolean? = false,

	@field:SerializedName("ToDays1")
	var toDays1: Int? = 0,

	@field:SerializedName("FromDays2")
	var fromDays2: Int? = 0,

	@field:SerializedName("ToDays2")
	var toDays2: Int? = 0
):Serializable
