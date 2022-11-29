package com.wms.superadmin.modules.splashscreen.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.databinding.ActivitySplashscreenBinding
import com.wms.superadmin.extensions.*
import com.wms.superadmin.modules.login.ui.LoginActivity
import com.wms.superadmin.modules.splashscreen.`data`.viewmodel.SplashscreenVM
import com.wms.superadmin.network.models.createtoken.CreateTokenResponse
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import java.lang.Exception
import kotlin.String
import kotlin.Unit
import org.json.JSONObject
import retrofit2.HttpException

public class SplashscreenActivity :
    BaseActivity<ActivitySplashscreenBinding>(R.layout.activity_splashscreen) {
  private val viewModel: SplashscreenVM by viewModels<SplashscreenVM>()

  public override fun setUpClicks(): Unit {
  }

  public override fun onInitialized(): Unit {
    binding.splashscreenVM = viewModel
    this@SplashscreenActivity.hideKeyboard()
    viewModel.onClickOnCreate()
      appendLog("Token called")
  }

  public override fun addObservers(): Unit {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@SplashscreenActivity){
        if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@SplashscreenActivity.showProgressDialog()
        } else {
        progressDialog?.dismiss()
        }
        }
    viewModel.createTokenLiveData.observe(this@SplashscreenActivity){
        if(it is SuccessResponse) {
        val response = it.getContentIfNotHandled()
        onSuccessCreateToken(it)
        } else if(it is ErrorResponse) {
        onErrorCreateToken(it.data ?:Exception())
        }
        }
  }

  private fun onSuccessCreateToken(response: SuccessResponse<CreateTokenResponse>): Unit {
    viewModel.bindCreateTokenResponse(response.data)
    Handler(Looper.getMainLooper()).postDelayed({
        val destIntent = LoginActivity.getIntent(this, null)
        startActivity(destIntent)
        this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
        }, 3000)
  }

  private fun onErrorCreateToken(exception: Exception): Unit {
    when(exception){
        is NoInternetConnection -> {
        Snackbar.make(binding.root, exception.message?:"", Snackbar.LENGTH_LONG).show()
        }
        is HttpException -> {
        val errorBody = exception.response()?.errorBody()?.string()
        val errorObject = if (errorBody != null  && errorBody.isJSONObject()) JSONObject(errorBody)
        else
            JSONObject()
        Toast.makeText(this@SplashscreenActivity,MyApp.getInstance().getString(R.string.msg_token_not_available),Toast.LENGTH_LONG).show()
        }
        }
  }

  public companion object {
    public const val TAG: String = "SPLASHSCREEN_ACTIVITY"

    public fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, SplashscreenActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }
}

