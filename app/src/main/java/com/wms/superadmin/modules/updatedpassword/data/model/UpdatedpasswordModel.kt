package com.wms.superadmin.modules.updatedpassword.`data`.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class UpdatedpasswordModel(
  /**
   * TODO Replace with dynamic value
   */
  public var txtForgotPassword: String? =
      MyApp.getInstance().resources.getString(R.string.lbl_forgot_password)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtYourpasswordh: String? =
      MyApp.getInstance().resources.getString(R.string.msg_your_password_h)

)
