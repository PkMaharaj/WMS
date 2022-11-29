package com.wms.superadmin.modules.dashboardsuperadmin.data.model
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp

public data class DrawerItemDrawerMenuModel(
  /**
   * TODO Replace with dynamic value
   */
  public var UserName: String? = MyApp.getInstance().resources.getString(R.string.lbl_user)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var RoleName: String? =
    MyApp.getInstance().resources.getString(R.string.lbl_view_profile)
  ,public var Fermname: String? = MyApp.getInstance().resources.getString(R.string.lbl_view_profile)
  , public var BranchName: String? =
    MyApp.getInstance().resources.getString(R.string.lbl_view_profile)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtHome: String? = MyApp.getInstance().resources.getString(R.string.lbl_sales)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtAudio: String? = MyApp.getInstance().resources.getString(R.string.lbl_purchase)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtReadingList: String? = MyApp.getInstance().resources.getString(R.string.lbl_account)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtInterests: String? = MyApp.getInstance().resources.getString(R.string.lbl_inventory)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtBecomeamember: String? =
    MyApp.getInstance().resources.getString(R.string.lbl_master)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtNewStory: String? = MyApp.getInstance().resources.getString(R.string.lbl_admin)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtReports: String? = MyApp.getInstance().resources.getString(R.string.lbl_reports)
  ,
  /**
   * TODO Replace with dynamic value
   */
  public var txtSettings: String? = MyApp.getInstance().resources.getString(R.string.lbl_settings)

)