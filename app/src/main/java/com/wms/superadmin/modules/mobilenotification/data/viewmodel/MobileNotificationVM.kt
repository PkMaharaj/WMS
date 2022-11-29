package com.wms.superadmin.modules.mobilenotification.data.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.modules.mobilenotification.data.model.MobileNotificationModel
import com.wms.superadmin.modules.mobilenotification.data.model.MobileNotificationRowModel
import com.wms.superadmin.network.models.fetch0.Fetch0ResponseItem
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.fetch0.NotificationResponse
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import com.wms.superadmin.network.resources.SuccessResponse
import kotlin.Boolean
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

public class MobileNotificationVM : ViewModel(), KoinComponent {
  public val mobileNotificationModel: MutableLiveData<MobileNotificationModel> =
    MutableLiveData(MobileNotificationModel())

  public var navArguments: Bundle? = null

  public val recyclerGroup19List: MutableLiveData<MutableList<MobileNotificationRowModel>> =MutableLiveData(mutableListOf())

  public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

  private val networkRepository: NetworkRepository by inject()

  public val fetch0LiveData: MutableLiveData<Response<NotificationResponse>> =
    MutableLiveData<Response<NotificationResponse>>()
  private val prefs: PreferenceHelper by inject()
  var user=prefs.getSADetails<LoginResponse>()!!
  private val CreatedType: String? = null

  public fun onClickOnCreate(): Unit {
    viewModelScope.launch {
      progressLiveData.postValue(true)
      fetch0LiveData.postValue(
        networkRepository.fetchNotificationList(
          authorization = "bearer ${prefs.getAccess_token()}",
          OrgId = user.orgID,
          BranchId = user.branchID
        )
      )
      progressLiveData.postValue(false)
    }
  }

  public fun bindFetch0Response(response: NotificationResponse,type:String): Unit {
    val notification3ModelValue = mobileNotificationModel.value ?: MobileNotificationModel()
    var finalNotificationlist= arrayListOf<MobileNotificationRowModel>()
    Log.e("typecheck ", type)
    val recyclerGroup19 = response.notificationlist?.map{
      Log.e("Check_response ", it.toString())
        MobileNotificationRowModel(
          txt1122021 = it.creationDate.toString()?:"",
          txt200 = it.approvalStatus.toString(),
          createdType = it.createdType?:"",
          txtSOnum = it.salesOrderNumber?:"",
          txtLanguage = it?.firmName?:"",
          date = it?.creationDate?:""
        )
    }?.toMutableList()
    for(item in recyclerGroup19 as ArrayList)
    {
      if(item.createdType==type)
      finalNotificationlist.add(item)

      else if (item.createdType.equals(""))
        finalNotificationlist.add(item)
    }
    recyclerGroup19List.value = finalNotificationlist.toMutableList()
    mobileNotificationModel.value = notification3ModelValue
  }
}
