package com.wms.superadmin.network.models.pojos

import com.google.gson.annotations.SerializedName

data class CreateBranchListRequest(

	@field:SerializedName("OrgID")
	val orgID: String? = null
)
