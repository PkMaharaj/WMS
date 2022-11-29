package com.wms.superadmin.extensions

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import com.wms.superadmin.modules.notificationWMS.ui.NotificationWMSActivity
import com.wms.superadmin.R
import com.wms.superadmin.databinding.NotificationDialogBinding
import com.wms.superadmin.modules.mobilenotification.ui.MobileNotificationActivity

class NotificationDialog (context:Context) {

    var dialog=AlertDialog.Builder(context)
        .apply {
            val layoutInflater = LayoutInflater.from(context)
            val  shake= AnimationUtils.loadAnimation(context, R.anim.slide_up);
            val binding = NotificationDialogBinding.inflate(layoutInflater, null, false)
            setView(binding.root)

            binding.mobilenotification.setOnClickListener {
                binding.firstLayout.visibility=View.GONE
                binding.secondLayout.visibility=View.VISIBLE
            }

            binding.wmsnotification.setOnClickListener {
                binding.firstLayout.visibility=View.GONE
                val intent = NotificationWMSActivity.getIntent(context,null)
                context.startActivity(intent)
            }

            binding.layoutSalesmanNotification.setOnClickListener {
                var destIntent= MobileNotificationActivity.getIntent(context,null)
                destIntent.putExtra("notification","S")
                context.startActivity(destIntent)
            }

            binding.layoutRetailerNotification.setOnClickListener {
                var destIntent=MobileNotificationActivity.getIntent(context,null)
                destIntent.putExtra("notification","R")
                context.startActivity(destIntent)
            }


            val dialog = create()
            dialog.setCanceledOnTouchOutside(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            dialog.show()

        }
}