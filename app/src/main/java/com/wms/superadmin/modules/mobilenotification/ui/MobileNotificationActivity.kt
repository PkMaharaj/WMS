package com.wms.superadmin.modules.mobilenotification.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.databinding.ActivityNotificationBinding
//import com.wms.superadmin.databinding.ActivityNotification3Binding
import com.wms.superadmin.extensions.NoInternetConnection
import com.wms.superadmin.extensions.hideKeyboard
import com.wms.superadmin.extensions.isJSONObject
import com.wms.superadmin.extensions.showProgressDialog
import com.wms.superadmin.modules.mobilenotification.data.model.MobileNotificationRowModel
import com.wms.superadmin.modules.mobilenotification.data.viewmodel.MobileNotificationVM
import com.wms.superadmin.modules.notificationapproval.ui.NotificationApprovalActivity
import com.wms.superadmin.network.models.fetch0.NotificationResponse
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import org.json.JSONObject
import retrofit2.HttpException

public class MobileNotificationActivity :
  BaseActivity<ActivityNotificationBinding>(R.layout.activity_notification) {
  private val viewModel: MobileNotificationVM by viewModels<MobileNotificationVM>()
  lateinit var mobileNotificationAdapter: MobileNotificationAdapter
  var notification=""

  public override fun onInitialized(): Unit {
    viewModel.navArguments = intent.extras?.getBundle("bundle")

    notification= intent.getStringExtra("notification")!!
    Log.e("CheckValue1-- ",notification.toString())

    if (notification == "S"){
      viewModel.mobileNotificationModel.value?.txtWMSNotification="Salesman Notification"
    }
    else if (notification == "R"){
      binding.txtNotification.setText("Retailer Notification")
      viewModel.mobileNotificationModel.value?.txtWMSNotification="Retailer Notification"
    }
    else {
      viewModel.mobileNotificationModel.value?.txtWMSNotification = "SPVR/MNGR Notification"
    }
//    viewModel.onClickOnCreate()

    mobileNotificationAdapter = MobileNotificationAdapter(viewModel.recyclerGroup19List.value?:mutableListOf())
    binding.recyclerGroup19.adapter = mobileNotificationAdapter
    binding.recyclerGroup19.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

    mobileNotificationAdapter.setOnItemClickListener(
      object : MobileNotificationAdapter.OnItemClickListener {
        override fun onItemClick(view:View, position:Int, item : MobileNotificationRowModel) {
          onClickRecyclerGroup19(view, position, item)
        }
      }
    )
    viewModel.recyclerGroup19List.observe(this) {
      mobileNotificationAdapter.updateData(it)
    }
    binding.notification3VM = viewModel
    this@MobileNotificationActivity.hideKeyboard()

  }

  public override fun setUpClicks(): Unit {


    binding.backscreenL.setOnClickListener {
      super.onBackPressed()
    }

    binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {

        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        mobileNotificationAdapter.filter.filter(newText)
        return false
      }
    })
  }

  override fun onResume() {
    super.onResume()
    viewModel.onClickOnCreate()
  }
  public fun onClickRecyclerGroup19(view: View, position: Int, item: MobileNotificationRowModel): Unit {
    Log.e("SONUMBER ",item.txtSOnum.toString())
    var destIntent= NotificationApprovalActivity.getIntent(this,null)
    destIntent.putExtra("SONUMBER",item.txtSOnum)
    startActivity(destIntent)
  }

  public override fun addObservers(): Unit {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@MobileNotificationActivity) {
      if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@MobileNotificationActivity.showProgressDialog()
      } else {
        progressDialog?.dismiss()
      }
    }
    viewModel.fetch0LiveData.observe(this@MobileNotificationActivity) {
      if(it is SuccessResponse) {
        val response = it.getContentIfNotHandled()
        onSuccessFetch0(it)
      } else if(it is ErrorResponse) {
        onErrorFetch0(it.data ?:Exception())
      }
    }
  }

  private fun onSuccessFetch0(response: SuccessResponse<NotificationResponse>): Unit {
    Snackbar.make(binding.root, MyApp.getInstance().getString(R.string.lbl_success),
      Snackbar.LENGTH_LONG).show()
    Log.e("id_passed ",notification)
    viewModel.bindFetch0Response(response.data,notification)
  }

  private fun onErrorFetch0(exception: Exception): Unit {
    when(exception) {
      is NoInternetConnection -> {
        Snackbar.make(binding.root, exception.message?:"", Snackbar.LENGTH_LONG).show()
      }
      is HttpException -> {
        val errorBody = exception.response()?.errorBody()?.string()
        val errorObject = if (errorBody != null  && errorBody.isJSONObject()) JSONObject(errorBody)
        else JSONObject()
        Snackbar.make(binding.root, MyApp.getInstance().getString(R.string.lbl_failure),
          Snackbar.LENGTH_LONG).show()
      }
    }
  }

  public companion object {
    public const val TAG: String = "NOTIFICATION3ACTIVITY"

    public fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, MobileNotificationActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }
}
