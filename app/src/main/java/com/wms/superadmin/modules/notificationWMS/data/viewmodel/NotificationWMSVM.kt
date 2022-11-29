package com.wms.superadmin.modules.notificationWMS.data.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.modules.mobilenotification.data.model.MobileNotificationModel
import com.wms.superadmin.modules.mobilenotification.data.model.MobileNotificationRowModel
import com.wms.superadmin.network.models.fetch0.Fetch0ResponseItem
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.modules.notificationWMS.data.model.NotificationWMSModel
import com.wms.superadmin.modules.notificationWMS.data.model.NotificationWMSRowModel
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.fetch0.NotificationResponse
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlin.Boolean
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

public class NotificationWMSVM : ViewModel(), KoinComponent {

  public val wmsNotificationModel: MutableLiveData<NotificationWMSModel> =
    MutableLiveData(NotificationWMSModel())

  public var navArguments: Bundle? = null

  public val recyclerGroup19List: MutableLiveData<MutableList<NotificationWMSRowModel>> =
    MutableLiveData(mutableListOf())

  public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

  private val networkRepository: NetworkRepository by inject()

  public val fetch0LiveData: MutableLiveData<Response<NotificationResponse>> =
    MutableLiveData<Response<NotificationResponse>>()

  private val prefs: PreferenceHelper by inject()
  var user = prefs.getSADetails<LoginResponse>()!!

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

  public fun bindFetch0Response(response: List<Fetch0ResponseItem>): Unit {
    val notification3ModelValue = wmsNotificationModel.value ?: NotificationWMSModel()
    val recyclerGroup19 = response.map {
      NotificationWMSRowModel(
        txt1122021 = it?.firmName.toString()?:"",
        txtLanguage = it?.branchName.toString()?:"",
        txt200 = it?.approvalStatus.toString()?:"",
        date = it?.creationDate?:""
      )
    }.toMutableList()
    recyclerGroup19List.value = recyclerGroup19
    wmsNotificationModel.value = notification3ModelValue
  }
}
