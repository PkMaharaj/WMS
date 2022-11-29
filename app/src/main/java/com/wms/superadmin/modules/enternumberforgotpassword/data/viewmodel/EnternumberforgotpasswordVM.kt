package com.wms.superadmin.modules.enternumberforgotpassword.`data`.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.modules.enternumberforgotpassword.`data`.model.EnternumberforgotpasswordModel
import com.wms.superadmin.network.models.create7411313141.Create7411313141Response
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlin.Boolean
import kotlin.Unit
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

public class EnternumberforgotpasswordVM : ViewModel(), KoinComponent {
    public val enternumberforgotpasswordModel: MutableLiveData<EnternumberforgotpasswordModel> =
        MutableLiveData(EnternumberforgotpasswordModel())

    public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    private val networkRepository: NetworkRepository by inject()
    private val prefs: PreferenceHelper by inject()

    public val create7411313141LiveData: MutableLiveData<Response<Create7411313141Response>> =
        MutableLiveData<Response<Create7411313141Response>>()

    public fun onClickBtnConfirm(): Unit {
        viewModelScope.launch{
            progressLiveData.postValue(true)
            create7411313141LiveData.postValue(networkRepository.create7411313141(
                authorization = "bearer " + prefs.getAccess_token(),
                MobileNumber = enternumberforgotpasswordModel.value?.etEnterMobileNumberValue))
            progressLiveData.postValue(false)
        }
    }

    public fun bindCreate7411313141Response(responseData: Create7411313141Response): Unit {
        val enternumberforgotpasswordModelValue = enternumberforgotpasswordModel.value
            ?:EnternumberforgotpasswordModel()
        enternumberforgotpasswordModel.value = enternumberforgotpasswordModelValue
    }
}

//package com.wms.app.modules.enternumberforgotpassword.`data`.viewmodel
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.wms.app.modules.enternumberforgotpassword.`data`.model.EnternumberforgotpasswordModel
//import com.wms.app.network.models.create7411313141.Create7411313141Request
//import com.wms.app.network.models.create7411313141.Create7411313141Response
//import com.wms.app.network.repository.NetworkRepository
//import com.wms.app.network.resources.Response
//import kotlin.Boolean
//import kotlin.Unit
//import kotlinx.coroutines.launch
//import org.koin.core.KoinComponent
//import org.koin.core.inject
//
//public class EnternumberforgotpasswordVM : ViewModel(), KoinComponent {
//  public val enternumberforgotpasswordModel: MutableLiveData<EnternumberforgotpasswordModel> =
//      MutableLiveData(EnternumberforgotpasswordModel())
//
//  public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
//
//  private val networkRepository: NetworkRepository by inject()
//
//  public val create7411313141LiveData: MutableLiveData<Response<Create7411313141Response>> =
//      MutableLiveData<Response<Create7411313141Response>>()
//
//  public fun onClickBtnConfirm(): Unit {
//    viewModelScope.launch{
//    progressLiveData.postValue(true)
//    create7411313141LiveData.postValue(networkRepository.create7411313141(authorization =
//        """bearer VENu96gx-hQ1aipIUJ_1WZmEsQprxsXi1Gi5F_Aa-mW0xOiNYf04oJZO-2yzoKHtFB-WIJN6q8nNg7ky1BAfUAqTbAjRBGoBpqmrcvwuD7qZKPbhutPxla_2V-AO_x5099jctNI_a3V4rdKuGZ0glJT71PnVGVsIjlWjXr8opt9KUmwGkWPG4ESyYti9TOCsiTyz7QnmFrOqbaF4GpUr0Ouo18n3nH_o8vvK7o6ygxixc_g6pRtDnqUoM_JH3WvCNNzZk0dju3xsadwN-MsKfT2XAazLCQ2yBydP-okKCew""",
//        create7411313141Request = Create7411313141Request()))
//    progressLiveData.postValue(false)
//    }
//  }
//
//  public fun bindCreate7411313141Response(responseData: Create7411313141Response): Unit {
//    val enternumberforgotpasswordModelValue = enternumberforgotpasswordModel.value
//        ?:EnternumberforgotpasswordModel()
//    enternumberforgotpasswordModel.value = enternumberforgotpasswordModelValue
//  }
//}
