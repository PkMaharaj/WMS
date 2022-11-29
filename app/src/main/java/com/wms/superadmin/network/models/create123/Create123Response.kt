package com.wms.superadmin.network.models.create123

import com.google.gson.annotations.SerializedName

data class Create123Response(

	@field:SerializedName("SMSOTP")
	val sMSOTP: Any? = null,

	@field:SerializedName("UserID")
	val userID: Int? = null,

	@field:SerializedName("IsOTPVerified")
	val isOTPVerified: Any? = null,

	@field:SerializedName("OTPStatus")
	val oTPStatus: Any? = null,

	@field:SerializedName("IsRegistered")
	val isRegistered: Any? = null,

	@field:SerializedName("UserType")
	val userType: Any? = null,

	@field:SerializedName("Password")
	val password: Any? = null,

	@field:SerializedName("status")
	val status: String? = null
)
