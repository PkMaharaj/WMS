package com.wms.superadmin.modules.otppassword.`data`.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.modules.otppassword.`data`.model.OtppasswordModel
import com.wms.superadmin.network.models.create820469.Create820469Response
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlin.Boolean
import kotlin.Unit
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

public class OtppasswordVM : ViewModel(), KoinComponent {
  public val otppasswordModel: MutableLiveData<OtppasswordModel> =
    MutableLiveData(OtppasswordModel())

  public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

  private val networkRepository: NetworkRepository by inject()

  public val create820469LiveData: MutableLiveData<Response<Create820469Response>> =
    MutableLiveData<Response<Create820469Response>>()

  private val prefs: PreferenceHelper by inject()

  public fun onClickBtnVerify(): Unit {
    viewModelScope.launch{
      progressLiveData.postValue(true)
      create820469LiveData.postValue(networkRepository.create820469(
        authorization = "bearer " + prefs.getAccess_token(),
        MobileNumber = prefs.getMobile(),
        OTP = otppasswordModel.value?.etOTPValue
      ))
      progressLiveData.postValue(false)
    }
  }

  public fun bindCreate820469Response(responseData: Create820469Response): Unit {
    val otppasswordModelValue = otppasswordModel.value ?:OtppasswordModel()
    prefs.setCustID(responseData.custID)
    otppasswordModel.value = otppasswordModelValue
  }
}

//package com.wms.app.modules.otppassword.`data`.viewmodel
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.wms.app.appcomponents.utility.PreferenceHelper
//import com.wms.app.modules.otppassword.`data`.model.OtppasswordModel
//import com.wms.app.network.models.create820469.Create820469Request
//import com.wms.app.network.models.create820469.Create820469Response
//import com.wms.app.network.repository.NetworkRepository
//import com.wms.app.network.resources.Response
//import kotlin.Boolean
//import kotlin.Unit
//import kotlinx.coroutines.launch
//import org.koin.core.KoinComponent
//import org.koin.core.inject
//
//public class OtppasswordVM : ViewModel(), KoinComponent {
//  public val otppasswordModel: MutableLiveData<OtppasswordModel> =
//      MutableLiveData(OtppasswordModel())
//
//  public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
//
//  private val networkRepository: NetworkRepository by inject()
//
//  public val create820469LiveData: MutableLiveData<Response<Create820469Response>> =
//      MutableLiveData<Response<Create820469Response>>()
//
//  private val prefs: PreferenceHelper by inject()
//
//  public fun onClickBtnVerify(): Unit {
//    viewModelScope.launch{
//    progressLiveData.postValue(true)
//    create820469LiveData.postValue(networkRepository.create820469(authorization =
//        """bearer VENu96gx-hQ1aipIUJ_1WZmEsQprxsXi1Gi5F_Aa-mW0xOiNYf04oJZO-2yzoKHtFB-WIJN6q8nNg7ky1BAfUAqTbAjRBGoBpqmrcvwuD7qZKPbhutPxla_2V-AO_x5099jctNI_a3V4rdKuGZ0glJT71PnVGVsIjlWjXr8opt9KUmwGkWPG4ESyYti9TOCsiTyz7QnmFrOqbaF4GpUr0Ouo18n3nH_o8vvK7o6ygxixc_g6pRtDnqUoM_JH3WvCNNzZk0dju3xsadwN-MsKfT2XAazLCQ2yBydP-okKCew""",
//        create820469Request = Create820469Request()))
//    progressLiveData.postValue(false)
//    }
//  }
//
//  public fun bindCreate820469Response(responseData: Create820469Response): Unit {
//    val otppasswordModelValue = otppasswordModel.value ?:OtppasswordModel()
//    prefs.setCustID(responseData.custID)
//    otppasswordModel.value = otppasswordModelValue
//  }
//}
