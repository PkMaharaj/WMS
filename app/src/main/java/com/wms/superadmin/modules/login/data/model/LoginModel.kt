package com.wms.superadmin.modules.login.`data`.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class LoginModel(

  /**
   * TODO Replace with dynamic value
   */

  public var txtLogin: String? = MyApp.getInstance().resources.getString(R.string.lbl_login)
  ,
  /**
   * TODO Replace with dynamic value
   */

  public var txtRememberme: String? = MyApp.getInstance().resources.getString(R.string.lbl_remember_me),

  /**
   * TODO Replace with dynamic value
   */

  public var txtForgotpassword: String? = MyApp.getInstance().resources.getString(R.string.msg_forgot_password),

  /**
   * TODO Replace with dynamic value
   */

  public var etMobilenumberValue: String? = null,

  /**
   * TODO Replace with dynamic value
   */

  public var etPasswordValue: String? = null,

  public var chkstate:Boolean?=false
)
