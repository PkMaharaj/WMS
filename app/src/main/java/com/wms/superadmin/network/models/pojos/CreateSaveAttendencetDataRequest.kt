package com.wms.superadmin.network.models.pojos

import com.google.gson.annotations.SerializedName

data class CreateSaveAttendencetDataRequest(

	@field:SerializedName("Att_outtime")
	val attOuttime: Any? = null,

	@field:SerializedName("Att_Status")
	val attStatus: Any? = null,

	@field:SerializedName("CreatedBy")
	val createdBy: String? = null,

	@field:SerializedName("Att_Photo")
	val attPhoto: Any? = null,

	@field:SerializedName("Att_Intime")
	val attIntime: String? = null,

	@field:SerializedName("GPS_Lattitude")
	val gPSLattitude: String? = null,

	@field:SerializedName("Att_DateTime")
	val attDateTime: String? = null,

	@field:SerializedName("Difference")
	val difference: Any? = null,

	@field:SerializedName("Att_Type")
	val attType: String? = null,

	@field:SerializedName("GPS_Location")
	val gPSLocation: String? = null,

	@field:SerializedName("CreationDate ")
	val creationDate: String? = null,

	@field:SerializedName("UserID")
	val userID: String? = null,

	@field:SerializedName("GPS_Longitude")
	val gPSLongitude: String? = null,

	@field:SerializedName("Att_SubType")
	val attSubType: String? = null
)
