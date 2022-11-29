package com.wms.superadmin.modules.newpassword.`data`.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.modules.newpassword.`data`.model.NewpasswordModel
import com.wms.superadmin.network.models.create123.Create123Response
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlin.Boolean
import kotlin.Unit
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

public class NewpasswordVM : ViewModel(), KoinComponent {
    public val newpasswordModel: MutableLiveData<NewpasswordModel> =
        MutableLiveData(NewpasswordModel())

    public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    private val networkRepository: NetworkRepository by inject()
    private val prefs : PreferenceHelper by inject()

    public val create123LiveData: MutableLiveData<Response<Create123Response>> =
        MutableLiveData<Response<Create123Response>>()

    public fun onClickBtnConfirm(): Unit {
        viewModelScope.launch{
            progressLiveData.postValue(true)
            create123LiveData.postValue(networkRepository.create123(
                authorization = "bearer " + prefs.getAccess_token(),
                CustID = prefs.getCustID().toString(),
                NewPassword = newpasswordModel.value?.etConfirmYourNewPasswordValue
            ))
            progressLiveData.postValue(false)
        }
    }

    public fun bindCreate123Response(responseData: Create123Response): Unit {
        val newpasswordModelValue = newpasswordModel.value ?:NewpasswordModel()
        newpasswordModel.value = newpasswordModelValue
    }
}


//package com.wms.app.modules.newpassword.`data`.viewmodel
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.wms.app.modules.newpassword.`data`.model.NewpasswordModel
//import com.wms.app.network.models.create123.Create123Request
//import com.wms.app.network.models.create123.Create123Response
//import com.wms.app.network.repository.NetworkRepository
//import com.wms.app.network.resources.Response
//import kotlin.Boolean
//import kotlin.Unit
//import kotlinx.coroutines.launch
//import org.koin.core.KoinComponent
//import org.koin.core.inject
//
//public class NewpasswordVM : ViewModel(), KoinComponent {
//  public val newpasswordModel: MutableLiveData<NewpasswordModel> =
//      MutableLiveData(NewpasswordModel())
//
//  public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
//
//  private val networkRepository: NetworkRepository by inject()
//
//  public val create123LiveData: MutableLiveData<Response<Create123Response>> =
//      MutableLiveData<Response<Create123Response>>()
//
//  public fun onClickBtnConfirm(): Unit {
//    viewModelScope.launch{
//    progressLiveData.postValue(true)
//    create123LiveData.postValue(networkRepository.create123(authorization =
//        """bearer 3kiBmbnuAhfZAYwc3o-8GNDPj1JkL2y2zBwqdg_fWhsR4Nh-8s7MM8j5WId8qK_LGnmKjsMRCMafiXVyG_YpgnMVohcJXr62u7CklM-RY1WLAydjh9BcSGTnxugcS1a3GlKzh8pF38PBZU--JT1AXV1BqZvw05ISWB5SzU5vbS5b8JzfS6jZdZMO-dP_l4WeLqMl4zDbldv1OrioXByv8mB3WEq9rxN1FWC0fQ488iPmsFyEzYwi4m-ARLrrsHEO-zqtLfH2XTZkoJV-Pi-gsHJohjrIMDTwjip6FEzXRBY""",
//        create123Request = Create123Request()))
//    progressLiveData.postValue(false)
//    }
//  }
//
//  public fun bindCreate123Response(responseData: Create123Response): Unit {
//    val newpasswordModelValue = newpasswordModel.value ?:NewpasswordModel()
//    newpasswordModel.value = newpasswordModelValue
//  }
//}
