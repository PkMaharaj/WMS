package com.wms.superadmin.modules.attedence.data.model
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import java.text.SimpleDateFormat
import java.util.*
import kotlin.String

public data class AttedenceModel(
  /**
   * TODO Replace with dynamic value
   */
  public var txtAttedence: String? = MyApp.getInstance().resources.getString(R.string.lbl_attendance)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtWeekday: String? ="Week"
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtTime: String? = "00:00 AM"
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtDate: String? =SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtLocation: String? ="Location"
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtLogin: String? = MyApp.getInstance().resources.getString(R.string.lbl_login)

)
