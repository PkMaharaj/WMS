package com.wms.superadmin.modules.splashscreen.`data`.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

public data class SplashscreenModel(
  /**
   * TODO Replace with dynamic value
   */
  public var txtWMSMOBILE: String? =
      MyApp.getInstance().resources.getString(R.string.lbl_wms_mobile)

)
