package com.wms.superadmin.network.models.fetch0

import com.google.gson.annotations.SerializedName

data class NotificationResponse(

	@field:SerializedName("Notificationlist")
	var notificationlist: List<Fetch0ResponseItem>? = null,
)
data class Fetch0ResponseItem(

	@field:SerializedName("CreationDate")
	var creationDate: String? = null,

	@field:SerializedName("FirmName")
	var firmName: String? = null,

	@field:SerializedName("ApprovalStatus")
	var approvalStatus: String? = null,

	@field:SerializedName("SalesOrderNumber")
	var salesOrderNumber: String? = null,

	@field:SerializedName("CreatedType")
	var createdType: String? = null,

	@field:SerializedName("BranchName")
	var branchName: String? = null
)
