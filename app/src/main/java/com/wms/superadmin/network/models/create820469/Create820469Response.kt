package com.wms.superadmin.network.models.create820469

import com.google.gson.annotations.SerializedName

data class Create820469Response(

	@field:SerializedName("PONumber")
	val pONumber: Any? = null,

	@field:SerializedName("UserName")
	val userName: Any? = null,

	@field:SerializedName("BrokerId")
	val brokerId: Int? = null,

	@field:SerializedName("IsRegistered")
	val isRegistered: Any? = null,

	@field:SerializedName("SONumber")
	val sONumber: Any? = null,

	@field:SerializedName("OrgID")
	val orgID: Any? = null,

	@field:SerializedName("MobileNumber")
	val mobileNumber: Any? = null,

	@field:SerializedName("SMSOTP")
	val sMSOTP: String? = null,

	@field:SerializedName("CustID")
	val custID: Int? = null,

	@field:SerializedName("IsOTPVerified")
	val isOTPVerified: Boolean? = null,

	@field:SerializedName("OTPStatus")
	val oTPStatus: String? = null,

	@field:SerializedName("UserType")
	val userType: Any? = null,

	@field:SerializedName("Password")
	val password: Any? = null
)
