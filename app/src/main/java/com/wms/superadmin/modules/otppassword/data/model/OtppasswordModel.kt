package com.wms.superadmin.modules.otppassword.`data`.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class OtppasswordModel(
    /**
     * TODO Replace with dynamic value
     */
    public var txtForgotPassword: String? =
        MyApp.getInstance().resources.getString(R.string.lbl_forgot_password)
    ,
    /**
     * TODO Replace with dynamic value
     */
    public var txtPleaseenterOT: String? =
        MyApp.getInstance().resources.getString(R.string.msg_please_enter_ot)
    ,
    /**
     * TODO Replace with dynamic value
     */
    public var etOTPValue: String? = null
)


//package com.wms.app.modules.otppassword.`data`.model
//
//import com.wms.app.R
//import com.wms.app.appcomponents.di.MyApp
//import kotlin.String
//
//public data class OtppasswordModel(
//  /**
//   * TODO Replace with dynamic value
//   */
//  public var txtForgotPassword: String? =
//      MyApp.getInstance().resources.getString(R.string.lbl_forgot_password)
//  ,
//  /**
//   * TODO Replace with dynamic value
//   */
//  public var txtPleaseenterOT: String? =
//      MyApp.getInstance().resources.getString(R.string.msg_please_enter_ot)
//  ,
//  /**
//   * TODO Replace with dynamic value
//   */
//  public var etMobilenumberValue: String? = null
//)
