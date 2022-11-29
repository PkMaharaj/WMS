package com.wms.superadmin.network.models.createtoken

import com.google.gson.annotations.SerializedName

data class CreateTokenRequest(

	@field:SerializedName("password")
	val password: String? = "Dgy7t7MvU3JZx+ObnW6z3A==",

	@field:SerializedName("grant_type")
	val grantType: String? = "password",

	@field:SerializedName("username")
	val username: String? = "dMRo3oqVLOs="
)
