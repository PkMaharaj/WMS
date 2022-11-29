package com.wms.superadmin.appcomponents.views

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.modules.dashboardsuperadmin.ui.DashboardSuperadminActivity
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.text.SimpleDateFormat
import java.util.*

class SuccessMsgDialogue (val context: Activity, var msg:String): KoinComponent
{
    var mDialogPayment : Dialog?=null
    var GifImage: ImageView?=null
    var tvMsg: TextView?=null
    var Ok: Button?=null
    var myMsg: String?=msg
    private val prefs: PreferenceHelper by inject()
    var outputDate: String = SimpleDateFormat("yyyy/MM/dd HH:mm:ss a", Locale.getDefault()).format(
        Date()
    )
    fun show()
    {
        mDialogPayment = Dialog(context)
        mDialogPayment!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialogPayment!!.getWindow()?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        val window: Window? = mDialogPayment!!.getWindow()
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        mDialogPayment!!.setContentView(R.layout.activity_success_screen)
        mDialogPayment!!.setCancelable(false)

        GifImage = mDialogPayment!!.findViewById<View>(R.id.imageView5) as? ImageView
        tvMsg = mDialogPayment!!.findViewById<View>(R.id.TextMsg) as? TextView
        Ok = mDialogPayment!!.findViewById<View>(R.id.exitbtn) as? Button
        Glide.with(context).load(R.drawable.done1).into(GifImage!!)
        tvMsg!!.setText(myMsg)
        Ok!!.setOnClickListener {
            mDialogPayment!!.cancel()
            context.finish()
        }
        mDialogPayment!!.show()
    }

}