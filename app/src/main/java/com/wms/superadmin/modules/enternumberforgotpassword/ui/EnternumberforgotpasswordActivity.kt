package com.wms.superadmin.modules.enternumberforgotpassword.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.databinding.ActivityEnternumberforgotpasswordBinding
import com.wms.superadmin.extensions.NoInternetConnection
import com.wms.superadmin.extensions.hideKeyboard
import com.wms.superadmin.extensions.isJSONObject
import com.wms.superadmin.extensions.showProgressDialog
import com.wms.superadmin.modules.enternumberforgotpassword.`data`.viewmodel.EnternumberforgotpasswordVM
import com.wms.superadmin.modules.otppassword.ui.OtppasswordActivity
import com.wms.superadmin.network.models.create7411313141.Create7411313141Response
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import java.lang.Exception
import kotlin.String
import kotlin.Unit
import org.json.JSONObject
import retrofit2.HttpException

public class EnternumberforgotpasswordActivity :
    BaseActivity<ActivityEnternumberforgotpasswordBinding>(R.layout.activity_enternumberforgotpassword)
{
    private val viewModel: EnternumberforgotpasswordVM by viewModels<EnternumberforgotpasswordVM>()

    public override fun setUpClicks(): Unit {
        binding.imageBack.setOnClickListener {
            finish()
        }
        binding.btnConfirm.setOnClickListener {
            this@EnternumberforgotpasswordActivity.hideKeyboard()
            viewModel.onClickBtnConfirm()
        }
    }

    public override fun onInitialized(): Unit {
        binding.enternumberforgotpasswordVM = viewModel
    }

    public override fun addObservers(): Unit {
        var progressDialog : AlertDialog? = null
        viewModel.progressLiveData.observe(this@EnternumberforgotpasswordActivity){
            if(it) {
                progressDialog?.dismiss()
                progressDialog = null
                progressDialog = this@EnternumberforgotpasswordActivity.showProgressDialog()
            } else {
                progressDialog?.dismiss()
            }
        }
        viewModel.create7411313141LiveData.observe(this@EnternumberforgotpasswordActivity){
            if(it is SuccessResponse) {
                val response = it.getContentIfNotHandled()
                onSuccessCreate7411313141(it)
            } else if(it is ErrorResponse) {
                onErrorCreate7411313141(it.data ?:Exception())
            }
        }
    }

    private fun onSuccessCreate7411313141(response: SuccessResponse<Create7411313141Response>): Unit {
        viewModel.bindCreate7411313141Response(response.data)
        val destIntent = OtppasswordActivity.getIntent(this, null)
        startActivity(destIntent)
        this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
    }

    private fun onErrorCreate7411313141(exception: Exception): Unit {
        when(exception){
            is NoInternetConnection -> {
                Snackbar.make(binding.root, exception.message?:"", Snackbar.LENGTH_LONG).show()
            }
            is HttpException -> {
                val errorBody = exception.response()?.errorBody()?.string()
                val errorObject = if (errorBody != null  && errorBody.isJSONObject())
                    JSONObject(errorBody)
                else
                    JSONObject()
                Snackbar.make(binding.root,
                    MyApp.getInstance().getString(R.string.msg_enter_valid_mobile_nu),
                    Snackbar.LENGTH_LONG).show()
            }
        }
    }

    public companion object {
        public const val TAG: String = "ENTERNUMBERFORGOTPASSWORD_ACTIVITY"

        public fun getIntent(context: Context, bundle: Bundle?): Intent {
            val destIntent = Intent(context, EnternumberforgotpasswordActivity::class.java)
            destIntent.putExtra("bundle", bundle)
            return destIntent
        }
    }
}


//package com.wms.app.modules.enternumberforgotpassword.ui
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
//import com.wms.app.databinding.ActivityEnternumberforgotpasswordBinding
//import com.wms.app.extensions.NoInternetConnection
//import com.wms.app.extensions.hideKeyboard
//import com.wms.app.extensions.isJSONObject
//import com.wms.app.extensions.showProgressDialog
//import com.wms.app.modules.enternumberforgotpassword.`data`.viewmodel.EnternumberforgotpasswordVM
//import com.wms.app.modules.otppassword.ui.OtppasswordActivity
//import com.wms.app.network.models.create7411313141.Create7411313141Response
//import com.wms.app.network.resources.ErrorResponse
//import com.wms.app.network.resources.SuccessResponse
//import java.lang.Exception
//import kotlin.String
//import kotlin.Unit
//import org.json.JSONObject
//import retrofit2.HttpException
//
//public class EnternumberforgotpasswordActivity :
//    BaseActivity<ActivityEnternumberforgotpasswordBinding>(R.layout.activity_enternumberforgotpassword)
//    {
//  private val viewModel: EnternumberforgotpasswordVM by viewModels<EnternumberforgotpasswordVM>()
//
//  public override fun setUpClicks(): Unit {
//    binding.imageBack.setOnClickListener {
//            finish()
//            }
//    binding.btnConfirm.setOnClickListener {
//            this@EnternumberforgotpasswordActivity.hideKeyboard()
//            viewModel.onClickBtnConfirm()
//            }
//  }
//
//  public override fun onInitialized(): Unit {
//    binding.enternumberforgotpasswordVM = viewModel
//  }
//
//  public override fun addObservers(): Unit {
//    var progressDialog : AlertDialog? = null
//    viewModel.progressLiveData.observe(this@EnternumberforgotpasswordActivity){
//            if(it) {
//            progressDialog?.dismiss()
//            progressDialog = null
//            progressDialog = this@EnternumberforgotpasswordActivity.showProgressDialog()
//            } else {
//            progressDialog?.dismiss()
//            }
//            }
//    viewModel.create7411313141LiveData.observe(this@EnternumberforgotpasswordActivity){
//            if(it is SuccessResponse) {
//            val response = it.getContentIfNotHandled()
//            onSuccessCreate7411313141(it)
//            } else if(it is ErrorResponse) {
//            onErrorCreate7411313141(it.data ?:Exception())
//            }
//            }
//  }
//
//  private fun onSuccessCreate7411313141(response: SuccessResponse<Create7411313141Response>): Unit {
//    viewModel.bindCreate7411313141Response(response.data)
//    val destIntent = OtppasswordActivity.getIntent(this, null)
//    startActivity(destIntent)
//    this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
//  }
//
//  private fun onErrorCreate7411313141(exception: Exception): Unit {
//    when(exception){
//            is NoInternetConnection -> {
//            Snackbar.make(binding.root, exception.message?:"", Snackbar.LENGTH_LONG).show()
//            }
//            is HttpException -> {
//            val errorBody = exception.response()?.errorBody()?.string()
//            val errorObject = if (errorBody != null  && errorBody.isJSONObject())
//        JSONObject(errorBody)
//            else
//                JSONObject()
//            Snackbar.make(binding.root,
//            MyApp.getInstance().getString(R.string.msg_enter_valid_mobile_nu),
//                Snackbar.LENGTH_LONG).show()
//            }
//            }
//  }
//
//  public companion object {
//    public const val TAG: String = "ENTERNUMBERFORGOTPASSWORD_ACTIVITY"
//
//    public fun getIntent(context: Context, bundle: Bundle?): Intent {
//      val destIntent = Intent(context, EnternumberforgotpasswordActivity::class.java)
//      destIntent.putExtra("bundle", bundle)
//      return destIntent
//    }
//  }
//}
