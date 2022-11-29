package com.wms.superadmin.modules.mobilenotification.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class MobileNotificationModel(
  /**
   * TODO Replace with dynamic value
   */
  public var txtWMSNotification: String? =
      MyApp.getInstance().resources.getString(R.string.msg_wms_notificatio)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtApprovalStatus: String? =
      MyApp.getInstance().resources.getString(R.string.lbl_approval_status)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtFirmName: String? = MyApp.getInstance().resources.getString(R.string.lbl_firm_name)
//      MyApp.getInstance().resources.getString(R.string.lbl_branchname)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtorderno: String? = MyApp.getInstance().resources.getString(R.string.lbl_orderNo)
//      MyApp.getInstance().resources.getString(R.string.lbl_firm_name)

)
