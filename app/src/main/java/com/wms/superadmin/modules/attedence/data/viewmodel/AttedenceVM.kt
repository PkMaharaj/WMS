package com.wms.superadmin.modules.attedence.data.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper

import com.wms.superadmin.modules.attedence.data.model.AttedenceModel
import com.wms.superadmin.network.models.pojos.CreateSaveAttendencetDataRequest
import com.wms.superadmin.network.models.pojos.CreateSaveAttendencetDataResponse
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.File

public class AttedenceVM : ViewModel(), KoinComponent {
  public val attedenceModel: MutableLiveData<AttedenceModel> = MutableLiveData(AttedenceModel())

  public var navArguments: Bundle? = null


  public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

  private val networkRepository: NetworkRepository by inject()

  public val saveattendanceLiveData: MutableLiveData<Response<CreateSaveAttendencetDataResponse>> =
      MutableLiveData<Response<CreateSaveAttendencetDataResponse>>()

  private val prefs: PreferenceHelper by inject()

  public fun saveattendanceVM(createSaveAttendencetDataRequest: CreateSaveAttendencetDataRequest, file: File?): Unit {
    viewModelScope.launch {
      progressLiveData.postValue(true)
      saveattendanceLiveData.postValue(
      networkRepository.saveattendance(
        authorization = "bearer " + prefs.getAccess_token(),
          createSaveAttendencetDataRequest,file
      )
      )
      progressLiveData.postValue(false)
    }
  }

}
