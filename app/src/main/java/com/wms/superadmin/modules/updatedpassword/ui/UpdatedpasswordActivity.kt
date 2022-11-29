package com.wms.superadmin.modules.updatedpassword.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.databinding.ActivityUpdatedpasswordBinding
import com.wms.superadmin.modules.login.ui.LoginActivity
import com.wms.superadmin.modules.updatedpassword.`data`.viewmodel.UpdatedpasswordVM
import kotlin.String
import kotlin.Unit

public class UpdatedpasswordActivity :
    BaseActivity<ActivityUpdatedpasswordBinding>(R.layout.activity_updatedpassword) {
  private val viewModel: UpdatedpasswordVM by viewModels<UpdatedpasswordVM>()

  public override fun setUpClicks(): Unit {
    binding.imageBack.setOnClickListener {
    finish()
    }
  }

  public override fun onInitialized(): Unit {
    binding.updatedpasswordVM = viewModel
    Handler(Looper.getMainLooper()).postDelayed({
    val destIntent = LoginActivity.getIntent(this, null)
    startActivity(destIntent)
    this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
    }, 3000)
  }

  public companion object {
    public const val TAG: String = "UPDATEDPASSWORD_ACTIVITY"

    public fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, UpdatedpasswordActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }
}
