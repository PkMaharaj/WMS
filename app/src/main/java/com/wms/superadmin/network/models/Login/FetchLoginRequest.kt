package com.wms.superadmin.network.models.Login

import com.google.gson.annotations.SerializedName

data class FetchLoginRequest(

    @field:SerializedName("MobileNumber")
    var mobile: String? = "",

    @field:SerializedName("Password")
    var password: String? = "",

    @field:SerializedName("SourceOfUpdate")
    var sourceOfUpdate: String? = "M",

    @field:SerializedName("DeviceID")
    var deviceID: String? = ""
)
