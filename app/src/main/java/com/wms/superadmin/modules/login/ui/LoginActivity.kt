package com.wms.superadmin.modules.login.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.databinding.ActivityLoginBinding
import com.wms.superadmin.extensions.NoInternetConnection
import com.wms.superadmin.extensions.hideKeyboard
import com.wms.superadmin.extensions.isJSONObject
import com.wms.superadmin.extensions.showProgressDialog
import com.wms.superadmin.modules.dashboardsuperadmin.ui.DashboardSuperadminActivity
import com.wms.superadmin.modules.enternumberforgotpassword.ui.EnternumberforgotpasswordActivity
import com.wms.superadmin.modules.login.`data`.viewmodel.LoginVM
import com.wms.superadmin.network.models.Login.LoginResponse
//import com.wms.app.network.models.fetch123.Fetch123Response
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import java.lang.Exception
import kotlin.String
import kotlin.Unit
import org.json.JSONObject
import retrofit2.HttpException
import org.koin.android.ext.android.inject

public class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
  private val viewModel: LoginVM by viewModels<LoginVM>()

  private val prefs: PreferenceHelper by inject()
  var requestpermissions = arrayOf(
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
  )

  public override fun setUpClicks(): Unit {
    if (!hasPermissions(this, requestpermissions)) {
      ActivityCompat.requestPermissions(this, requestpermissions, 1);
    }
    viewModel.onClickOnCreate()

    binding.txtForgotpassword.setOnClickListener {
    val destIntent = EnternumberforgotpasswordActivity.getIntent(this, null)
    startActivity(destIntent)
    this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
    }

    binding.btnLogin.setOnClickListener {
    this@LoginActivity.hideKeyboard()
    viewModel.onClickBtnLogin()
    }

    binding.imageEye.setOnClickListener {
      if(binding.etPassword.transformationMethod.equals(PasswordTransformationMethod.getInstance()))
      {
        // binding.etPassword.inputType=InputType.TYPE_CLASS_TEXT
        binding.imageEye.setImageResource(R.drawable.eye_slash)
        binding.etPassword.transformationMethod=HideReturnsTransformationMethod.getInstance()
      }
      else
      {
        // binding.etPassword.inputType=InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.imageEye.setImageResource(R.drawable.img_eye)
        binding.etPassword.transformationMethod=PasswordTransformationMethod.getInstance()
      }
    }
  }


  public override fun onInitialized(): Unit {
    System.out.println("--------------------------------"+ prefs.getCheckState())
    if(prefs.getCheckState()!!)
//      viewModel.splashModel.value?.chkstate=true
    viewModel.loginModel.value?.chkstate=true

    binding.loginVM = viewModel
  }
  private fun hasPermissions(context: Context?, PERMISSIONS: Array<String>): Boolean {
    if (context != null && PERMISSIONS != null) {
      for (permission in PERMISSIONS) {
        if (ActivityCompat.checkSelfPermission(
            context,
            permission
          ) != PackageManager.PERMISSION_GRANTED
        ) {
          return false
        }
      }
    }
    return true
  }
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String?>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == 1) {
      grantResults.forEach {
        if(it== PackageManager.PERMISSION_GRANTED)
          Log.e("Permissions", "write permissions Granted")
        else
          hasPermissions(this,requestpermissions)
      }
    }
  }

  public override fun addObservers(): Unit {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@LoginActivity){
    if(it) {
    progressDialog?.dismiss()
    progressDialog = null
    progressDialog = this@LoginActivity.showProgressDialog()
    } else {
    progressDialog?.dismiss()
    }
    }
    viewModel.fetch123LiveData.observe(this@LoginActivity){
    if(it is SuccessResponse) {
    val response = it.getContentIfNotHandled()
    onSuccessFetch123(it)
    } else if(it is ErrorResponse) {
    onErrorFetch123(it.data ?:Exception())
    }
    }
  }

  private fun onSuccessFetch123(response: SuccessResponse<LoginResponse>): Unit {

    //----  Added manually to check checkbox is checked or not   ----//
    if (response.data.mobile!=null){
      if (binding.checkBox.isChecked){
        viewModel.saveRememberMe(mobile = viewModel.loginModel.value?.etMobilenumberValue, password = viewModel.loginModel.value?.etPasswordValue,isSaved = true)
      }
      else{
        viewModel.saveRememberMe("null","null",false)
      }
    }
    //--------//

    viewModel.bindLoginResponse(response.data)
    val destIntent = DashboardSuperadminActivity.getIntent(this, null)
    startActivity(destIntent)
    this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
  }

  private fun onErrorFetch123(exception: Exception): Unit {
    when(exception){
    is NoInternetConnection -> {
    Snackbar.make(binding.root, exception.message?:"", Snackbar.LENGTH_LONG).show()
    }
    is HttpException -> {
    val errorBody = exception.response()?.errorBody()?.string()
    val errorObject = if (errorBody != null  && errorBody.isJSONObject()) JSONObject(errorBody) else
        JSONObject()
    Snackbar.make(binding.root, MyApp.getInstance().getString(R.string.lbl_login_failed),
        Snackbar.LENGTH_LONG).show()
    }
    }
  }

  public companion object {
    public const val TAG: String = "LOGIN_ACTIVITY"

    public fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, LoginActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }
}
