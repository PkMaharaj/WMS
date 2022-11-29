package com.wms.superadmin.modules.otppassword.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.databinding.ActivityOtppasswordBinding
import com.wms.superadmin.extensions.NoInternetConnection
import com.wms.superadmin.extensions.hideKeyboard
import com.wms.superadmin.extensions.isJSONObject
import com.wms.superadmin.extensions.showProgressDialog
import com.wms.superadmin.modules.newpassword.ui.NewpasswordActivity
import com.wms.superadmin.modules.otppassword.`data`.viewmodel.OtppasswordVM
import com.wms.superadmin.network.models.create820469.Create820469Response
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import java.lang.Exception
import kotlin.String
import kotlin.Unit
import org.json.JSONObject
import retrofit2.HttpException

public class OtppasswordActivity :
  BaseActivity<ActivityOtppasswordBinding>(R.layout.activity_otppassword) {
  private val viewModel: OtppasswordVM by viewModels<OtppasswordVM>()

  public override fun setUpClicks(): Unit {
    binding.btnVerify.setOnClickListener {
      this@OtppasswordActivity.hideKeyboard()
      viewModel.onClickBtnVerify()
    }
    binding.imageBack.setOnClickListener {
      finish()
    }
  }

  public override fun onInitialized(): Unit {
    binding.otppasswordVM = viewModel
  }

  public override fun addObservers(): Unit {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@OtppasswordActivity){
      if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@OtppasswordActivity.showProgressDialog()
      } else {
        progressDialog?.dismiss()
      }
    }
    viewModel.create820469LiveData.observe(this@OtppasswordActivity){
      if(it is SuccessResponse) {
        val response = it.getContentIfNotHandled()
        onSuccessCreate820469(it)
      } else if(it is ErrorResponse) {
        onErrorCreate820469(it.data ?:Exception())
      }
    }
  }

  private fun onSuccessCreate820469(response: SuccessResponse<Create820469Response>): Unit {
    viewModel.bindCreate820469Response(response.data)
    val destIntent = NewpasswordActivity.getIntent(this, null)
    startActivity(destIntent)
    this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
  }

  private fun onErrorCreate820469(exception: Exception): Unit {
    when(exception){
      is NoInternetConnection -> {
        Snackbar.make(binding.root, exception.message?:"", Snackbar.LENGTH_LONG).show()
      }
      is HttpException -> {
        val errorBody = exception.response()?.errorBody()?.string()
        val errorObject = if (errorBody != null  && errorBody.isJSONObject()) JSONObject(errorBody) else
          JSONObject()
        Snackbar.make(binding.root, MyApp.getInstance().getString(R.string.lbl_enter_valid_otp),
          Snackbar.LENGTH_LONG).show()
      }
    }
  }

  public companion object {
    public const val TAG: String = "OTPPASSWORD_ACTIVITY"

    public fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, OtppasswordActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }
}


//package com.wms.app.modules.otppassword.ui
//
//import android.content.Context
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.viewModels
//import androidx.appcompat.app.AlertDialog
//import com.google.android.material.snackbar.Snackbar
//import com.wms.app.R
//import com.wms.app.appcomponents.base.BaseActivity
//import com.wms.app.appcomponents.di.MyApp
//import com.wms.app.databinding.ActivityOtppasswordBinding
//import com.wms.app.extensions.NoInternetConnection
//import com.wms.app.extensions.hideKeyboard
//import com.wms.app.extensions.isJSONObject
//import com.wms.app.extensions.showProgressDialog
//import com.wms.app.modules.newpassword.ui.NewpasswordActivity
//import com.wms.app.modules.otppassword.`data`.viewmodel.OtppasswordVM
//import com.wms.app.network.models.create820469.Create820469Response
//import com.wms.app.network.resources.ErrorResponse
//import com.wms.app.network.resources.SuccessResponse
//import java.lang.Exception
//import kotlin.String
//import kotlin.Unit
//import org.json.JSONObject
//import retrofit2.HttpException
//
//public class OtppasswordActivity :
//    BaseActivity<ActivityOtppasswordBinding>(R.layout.activity_otppassword) {
//  private val viewModel: OtppasswordVM by viewModels<OtppasswordVM>()
//
//  public override fun setUpClicks(): Unit {
//    binding.btnVerify.setOnClickListener {
//    this@OtppasswordActivity.hideKeyboard()
//    viewModel.onClickBtnVerify()
//    }
//    binding.imageBack.setOnClickListener {
//    finish()
//    }
//  }
//
//  public override fun onInitialized(): Unit {
//    binding.otppasswordVM = viewModel
//  }
//
//  public override fun addObservers(): Unit {
//    var progressDialog : AlertDialog? = null
//    viewModel.progressLiveData.observe(this@OtppasswordActivity){
//    if(it) {
//    progressDialog?.dismiss()
//    progressDialog = null
//    progressDialog = this@OtppasswordActivity.showProgressDialog()
//    } else {
//    progressDialog?.dismiss()
//    }
//    }
//    viewModel.create820469LiveData.observe(this@OtppasswordActivity){
//    if(it is SuccessResponse) {
//    val response = it.getContentIfNotHandled()
//    onSuccessCreate820469(it)
//    } else if(it is ErrorResponse) {
//    onErrorCreate820469(it.data ?:Exception())
//    }
//    }
//  }
//
//  private fun onSuccessCreate820469(response: SuccessResponse<Create820469Response>): Unit {
//    viewModel.bindCreate820469Response(response.data)
//    val destIntent = NewpasswordActivity.getIntent(this, null)
//    startActivity(destIntent)
//    this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
//  }
//
//  private fun onErrorCreate820469(exception: Exception): Unit {
//    when(exception){
//    is NoInternetConnection -> {
//    Snackbar.make(binding.root, exception.message?:"", Snackbar.LENGTH_LONG).show()
//    }
//    is HttpException -> {
//    val errorBody = exception.response()?.errorBody()?.string()
//    val errorObject = if (errorBody != null  && errorBody.isJSONObject()) JSONObject(errorBody) else
//        JSONObject()
//    Snackbar.make(binding.root, MyApp.getInstance().getString(R.string.lbl_enter_valid_otp),
//        Snackbar.LENGTH_LONG).show()
//    }
//    }
//  }
//
//  public companion object {
//    public const val TAG: String = "OTPPASSWORD_ACTIVITY"
//
//    public fun getIntent(context: Context, bundle: Bundle?): Intent {
//      val destIntent = Intent(context, OtppasswordActivity::class.java)
//      destIntent.putExtra("bundle", bundle)
//      return destIntent
//    }
//  }
//}
