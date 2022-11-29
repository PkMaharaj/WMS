package com.wms.superadmin.modules.splashscreen.`data`.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.modules.splashscreen.`data`.model.SplashscreenModel
import com.wms.superadmin.network.models.createtoken.CreateTokenRequest
import com.wms.superadmin.network.models.createtoken.CreateTokenResponse
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlin.Boolean
import kotlin.Unit
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

public class SplashscreenVM : ViewModel(), KoinComponent {
  public val splashscreenModel: MutableLiveData<SplashscreenModel> =
      MutableLiveData(SplashscreenModel())

  public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

  private val networkRepository: NetworkRepository by inject()

  public val createTokenLiveData: MutableLiveData<Response<CreateTokenResponse>> =
      MutableLiveData<Response<CreateTokenResponse>>()

  private val prefs: PreferenceHelper by inject()

  public fun onClickOnCreate(): Unit {
    viewModelScope.launch{
    progressLiveData.postValue(true)
    createTokenLiveData.postValue(networkRepository.createToken(createTokenRequest =
        CreateTokenRequest()))
    progressLiveData.postValue(false)
    }
  }

  public fun bindCreateTokenResponse(responseData: CreateTokenResponse): Unit {
    val splashscreenModelValue = splashscreenModel.value ?:SplashscreenModel()
    prefs.setAccess_token(responseData.accessToken)
    prefs.setToken_type(responseData.tokenType)
    prefs.setExpires_in(responseData.expiresIn)
    splashscreenModel.value = splashscreenModelValue
  }
}
