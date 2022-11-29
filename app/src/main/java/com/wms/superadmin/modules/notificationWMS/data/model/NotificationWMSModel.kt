package com.wms.superadmin.modules.notificationWMS.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class NotificationWMSModel(
  public var txtWMSNotificatio: String? = MyApp.getInstance().resources.getString(R.string.msg_wms_notificatio),
  public var txtApprovalStatus: String? = MyApp.getInstance().resources.getString(R.string.lbl_approval_status),
  public var txtBranchName: String? = MyApp.getInstance().resources.getString(R.string.lbl_branchname),
  public var txtFirmName: String? = MyApp.getInstance().resources.getString(R.string.lbl_firm_name)
)
