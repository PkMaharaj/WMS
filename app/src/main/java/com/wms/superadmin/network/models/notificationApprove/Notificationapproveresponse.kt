package com.wms.superadmin.network.models.notificationApprove

import com.google.gson.annotations.SerializedName
import com.wms.superadmin.network.models.NotificationVericationSOList.NotificationResponseItemDetailsItem

data class Notificationapproveresponse(

    @field:SerializedName("Message")
    val message: String = "",
)
