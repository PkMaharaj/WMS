package com.wms.superadmin.modules.newpassword.`data`.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class NewpasswordModel(
  /**
   * TODO Replace with dynamic value
   */
  public var txtForgotPassword: String? =
      MyApp.getInstance().resources.getString(R.string.lbl_forgot_password)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var etEnterYourNewPasswordValue: String? = null,
  /**
   * TODO Replace with dynamic value
   */
  public var etConfirmYourNewPasswordValue: String? = null
)
