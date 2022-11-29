package com.wms.superadmin.modules.notificationWMS.ui

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
import com.wms.superadmin.modules.mobilenotification.data.model.MobileNotificationRowModel
import com.wms.superadmin.modules.mobilenotification.data.viewmodel.MobileNotificationVM
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.databinding.ActivityNotificationBinding
//import com.wms.superadmin.databinding.ActivityNotification3Binding
import com.wms.superadmin.extensions.NoInternetConnection
import com.wms.superadmin.extensions.hideKeyboard
import com.wms.superadmin.extensions.isJSONObject
import com.wms.superadmin.extensions.showProgressDialog
import com.wms.superadmin.modules.mobilenotification.ui.MobileNotificationAdapter
import com.wms.superadmin.modules.notificationapproval.ui.NotificationApprovalActivity
import com.wms.superadmin.network.models.fetch0.NotificationResponse
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import java.lang.Exception
import kotlin.Int
import kotlin.String
import kotlin.Unit
import org.json.JSONObject
import retrofit2.HttpException

public class NotificationWMSActivity :
    BaseActivity<ActivityNotificationBinding>(R.layout.activity_notification) {
//  lateinit var recyclerwmsnotificationAdapter: RecyclerWMSnotificationAdapter
  lateinit var recyclerMobileNotificationAdapter: MobileNotificationAdapter
  private val viewModel: MobileNotificationVM by viewModels<MobileNotificationVM>()

  public override fun onInitialized(): Unit {
    viewModel.navArguments = intent.extras?.getBundle("bundle")
    recyclerMobileNotificationAdapter =
      MobileNotificationAdapter(viewModel.recyclerGroup19List.value?:mutableListOf())
    binding.recyclerGroup19.adapter = recyclerMobileNotificationAdapter
    binding.recyclerGroup19.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

    recyclerMobileNotificationAdapter.setOnItemClickListener(
    object : MobileNotificationAdapter.OnItemClickListener {
      override fun onItemClick(view:View, position:Int, item : MobileNotificationRowModel) {
        onClickRecyclerGroup19(view, position, item)
      }
    }
    )
    binding.txtNotification.text = "WMS Notification"
    viewModel.recyclerGroup19List.observe(this) {
      recyclerMobileNotificationAdapter.updateData(it)
    }
    binding.notification3VM = viewModel
    this@NotificationWMSActivity.hideKeyboard()

  }

  override fun onResume() {
    super.onResume()
    viewModel.onClickOnCreate()
  }

  public override fun setUpClicks(): Unit {

    binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {

        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        recyclerMobileNotificationAdapter .filter.filter(newText)
        return false
      }
    })

    binding.backscreenL.setOnClickListener {
      super.onBackPressed()
    }
  }

  public fun onClickRecyclerGroup19(view: View, position: Int, item: MobileNotificationRowModel): Unit {
    Log.e("SONUMBER ",item.txtSOnum.toString())
    var destIntent= NotificationApprovalActivity.getIntent(this,null)
    destIntent.putExtra("SONUMBER",item.txtSOnum)
    startActivity(destIntent)
  }

  public override fun addObservers(): Unit {
    var progressDialog : AlertDialog? = null
    viewModel.progressLiveData.observe(this@NotificationWMSActivity) {
      if(it) {
        progressDialog?.dismiss()
        progressDialog = null
        progressDialog = this@NotificationWMSActivity.showProgressDialog()
      } else {
        progressDialog?.dismiss()
      }
    }
    viewModel.fetch0LiveData.observe(this@NotificationWMSActivity) {
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
    viewModel.bindFetch0Response(response.data,"W")
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
      val destIntent = Intent(context, NotificationWMSActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }
}
