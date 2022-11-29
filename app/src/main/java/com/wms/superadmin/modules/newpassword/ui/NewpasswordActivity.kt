package com.wms.superadmin.modules.newpassword.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.databinding.ActivityNewpasswordBinding
import com.wms.superadmin.extensions.NoInternetConnection
import com.wms.superadmin.extensions.hideKeyboard
import com.wms.superadmin.extensions.isJSONObject
import com.wms.superadmin.extensions.showProgressDialog
import com.wms.superadmin.modules.newpassword.data.model.NewpasswordModel
import com.wms.superadmin.modules.newpassword.`data`.viewmodel.NewpasswordVM
import com.wms.superadmin.modules.updatedpassword.ui.UpdatedpasswordActivity
import com.wms.superadmin.network.models.create123.Create123Response
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import java.lang.Exception
import kotlin.String
import kotlin.Unit
import org.json.JSONObject
import retrofit2.HttpException

public class NewpasswordActivity :
  BaseActivity<ActivityNewpasswordBinding>(R.layout.activity_newpassword) {
  private val viewModel: NewpasswordVM by viewModels<NewpasswordVM>()
  public val newpasswordModel: MutableLiveData<NewpasswordModel> =
    MutableLiveData(NewpasswordModel())

  public override fun setUpClicks(): Unit {

    binding.imageBack.setOnClickListener {
      finish()
    }

    binding.btnConfirm.setOnClickListener {
      this@NewpasswordActivity.hideKeyboard()

      System.out.println("New_Password: " + newpasswordModel.value?.etEnterYourNewPasswordValue)
      System.out.println("Confirm_New_Password: " + newpasswordModel.value?.etConfirmYourNewPasswordValue)

      if (!newpasswordModel.value?.etEnterYourNewPasswordValue?.equals(newpasswordModel.value?.etConfirmYourNewPasswordValue)!!){
        viewModel.onClickBtnConfirm()
      }
      else{
        Toast.makeText(this,"Password Mismatch",Toast.LENGTH_LONG).show()
      }
    }
  }

  public override fun onInitialized(): Unit {
    binding.newpasswordVM = viewModel
  }

  public override fun addObservers(): Unit {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@NewpasswordActivity){
      if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@NewpasswordActivity.showProgressDialog()
      } else {
        progressDialog?.dismiss()
      }
    }
    viewModel.create123LiveData.observe(this@NewpasswordActivity){
      if(it is SuccessResponse) {
        val response = it.getContentIfNotHandled()
        onSuccessCreate123(it)
      } else if(it is ErrorResponse) {
        onErrorCreate123(it.data ?:Exception())
      }
    }
  }

  private fun onSuccessCreate123(response: SuccessResponse<Create123Response>): Unit {
    viewModel.bindCreate123Response(response.data)
    val destIntent = UpdatedpasswordActivity.getIntent(this, null)
    startActivity(destIntent)
    this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
  }

  private fun onErrorCreate123(exception: Exception): Unit {
    when(exception){
      is NoInternetConnection -> {
        Snackbar.make(binding.root, exception.message?:"", Snackbar.LENGTH_LONG).show()
      }
      is HttpException -> {
        val errorBody = exception.response()?.errorBody()?.string()
        val errorObject = if (errorBody != null  && errorBody.isJSONObject()) JSONObject(errorBody) else
          JSONObject()
        Snackbar.make(binding.root, MyApp.getInstance().getString(R.string.msg_enter_proper_password),
          Snackbar.LENGTH_LONG).show()
      }
    }
  }

  public companion object {
    public const val TAG: String = "NEWPASSWORD_ACTIVITY"

    public fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, NewpasswordActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }
}


//package com.wms.app.modules.newpassword.ui
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
//import com.wms.app.databinding.ActivityNewpasswordBinding
//import com.wms.app.extensions.NoInternetConnection
//import com.wms.app.extensions.hideKeyboard
//import com.wms.app.extensions.isJSONObject
//import com.wms.app.extensions.showProgressDialog
//import com.wms.app.modules.newpassword.`data`.viewmodel.NewpasswordVM
//import com.wms.app.modules.updatedpassword.ui.UpdatedpasswordActivity
//import com.wms.app.network.models.create123.Create123Response
//import com.wms.app.network.resources.ErrorResponse
//import com.wms.app.network.resources.SuccessResponse
//import java.lang.Exception
//import kotlin.String
//import kotlin.Unit
//import org.json.JSONObject
//import retrofit2.HttpException
//
//public class NewpasswordActivity :
//    BaseActivity<ActivityNewpasswordBinding>(R.layout.activity_newpassword) {
//  private val viewModel: NewpasswordVM by viewModels<NewpasswordVM>()
//
//  public override fun setUpClicks(): Unit {
//    binding.imageBack.setOnClickListener {
//    finish()
//    }
//    binding.btnConfirm.setOnClickListener {
//    this@NewpasswordActivity.hideKeyboard()
//    viewModel.onClickBtnConfirm()
//    }
//  }
//
//  public override fun onInitialized(): Unit {
//    binding.newpasswordVM = viewModel
//  }
//
//  public override fun addObservers(): Unit {
//    var progressDialog : AlertDialog? = null
//    viewModel.progressLiveData.observe(this@NewpasswordActivity){
//    if(it) {
//    progressDialog?.dismiss()
//    progressDialog = null
//    progressDialog = this@NewpasswordActivity.showProgressDialog()
//    } else {
//    progressDialog?.dismiss()
//    }
//    }
//    viewModel.create123LiveData.observe(this@NewpasswordActivity){
//    if(it is SuccessResponse) {
//    val response = it.getContentIfNotHandled()
//    onSuccessCreate123(it)
//    } else if(it is ErrorResponse) {
//    onErrorCreate123(it.data ?:Exception())
//    }
//    }
//  }
//
//  private fun onSuccessCreate123(response: SuccessResponse<Create123Response>): Unit {
//    viewModel.bindCreate123Response(response.data)
//    val destIntent = UpdatedpasswordActivity.getIntent(this, null)
//    startActivity(destIntent)
//    this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
//  }
//
//  private fun onErrorCreate123(exception: Exception): Unit {
//    when(exception){
//    is NoInternetConnection -> {
//    Snackbar.make(binding.root, exception.message?:"", Snackbar.LENGTH_LONG).show()
//    }
//    is HttpException -> {
//    val errorBody = exception.response()?.errorBody()?.string()
//    val errorObject = if (errorBody != null  && errorBody.isJSONObject()) JSONObject(errorBody) else
//        JSONObject()
//    Snackbar.make(binding.root, MyApp.getInstance().getString(R.string.msg_enter_proper_password),
//        Snackbar.LENGTH_LONG).show()
//    }
//    }
//  }
//
//  public companion object {
//    public const val TAG: String = "NEWPASSWORD_ACTIVITY"
//
//    public fun getIntent(context: Context, bundle: Bundle?): Intent {
//      val destIntent = Intent(context, NewpasswordActivity::class.java)
//      destIntent.putExtra("bundle", bundle)
//      return destIntent
//    }
//  }
//}
