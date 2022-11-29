package com.wms.superadmin.modules.login.`data`.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.extensions.MenuNames
import com.wms.superadmin.modules.login.`data`.model.LoginModel
import com.wms.superadmin.network.models.Login.FetchLoginRequest
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.Login.ScreenAccessibility
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlin.Boolean
import kotlin.Unit
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

public class LoginVM : ViewModel(), KoinComponent {
  public val loginModel: MutableLiveData<LoginModel> = MutableLiveData(LoginModel())

  public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

  private val networkRepository: NetworkRepository by inject()

  public val fetch123LiveData: MutableLiveData<Response<LoginResponse>> =
    MutableLiveData<Response<LoginResponse>>()

  private val prefs: PreferenceHelper by inject()

  /*public fun onClickBtnLogin(): Unit {
    viewModelScope.launch{
      progressLiveData.postValue(true)
      fetch123LiveData.postValue(networkRepository.fetch123(
        authorization = "bearer " + prefs.getAccess_token(),
        MobileNumber = loginModel.value?.etMobilenumberValue,
        Password = loginModel.value?.etPasswordValue
      ))
      progressLiveData.postValue(false)
    }*/

  public fun onClickBtnLogin(): Unit {
    var Request= FetchLoginRequest(mobile = loginModel.value?.etMobilenumberValue,password = loginModel.value?.etPasswordValue,"M","")
    viewModelScope.launch{
      progressLiveData.postValue(true)
      fetch123LiveData.postValue(networkRepository.Login(authorization = "bearer " + prefs.getAccess_token(),Request))
      progressLiveData.postValue(false)
    }
  }

//    System.out.println("checkingState==== " + prefs.getCheckState())
//    if (prefs.getCheckState()!!){
//      loginModel.value?.etMobilenumberValue = prefs.getMobile()
//      loginModel.value?.etPasswordValue = prefs.getPassword()
//
//    }


  public fun bindLoginResponse(responseData: LoginResponse): Unit {
    val loginModelValue = loginModel.value ?:LoginModel()
    prefs.setUserid(responseData.userID)
    var screenAccessibility=ScreenAccessibility()
    for(rightItem in responseData.lstofAccessRights?: arrayListOf()){
      when(rightItem.subMenuName){
        MenuNames.SalesOrder->
          screenAccessibility.sales_order=true
        MenuNames.SalesRegister->
          screenAccessibility.sales_register=true
        MenuNames.PurchaseOrder->
          screenAccessibility.purchase_order=true
        MenuNames.PurchaseRegister->
          screenAccessibility.purchase_register=true
        MenuNames.CaskBook->
          screenAccessibility.caskBook=true
        MenuNames.BankBook->
          screenAccessibility.bankBook=true
        MenuNames.Payable->
          screenAccessibility.payable=true
        MenuNames.Receivables->
          screenAccessibility.receivable=true
        MenuNames.Stock->
          screenAccessibility.stocks=true
        MenuNames.Notification->
          screenAccessibility.notification=true
      }
    }
   responseData.screenSettings=screenAccessibility
    loginModel.value = loginModelValue
    prefs.setSADetails(responseData)
  }

  fun saveRememberMe(mobile:String?,password: String?,isSaved:Boolean): Unit {
    Log.e("remember", "saving $isSaved $mobile  $password")
    val loginModelValue = loginModel.value ?: LoginModel()
    prefs.setMobile(mobile)
    prefs.setPassword(password)
    prefs.setCheckState(isSaved)
    loginModel.value = loginModelValue
  }

  fun onClickOnCreate() {
    System.out.println("checkingState==== " + prefs.getCheckState())
    if (prefs.getCheckState()!!){
      loginModel.value?.etMobilenumberValue = prefs.getMobile()
      loginModel.value?.etPasswordValue = prefs.getPassword()
    }
  }
}
