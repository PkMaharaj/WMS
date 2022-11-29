package com.wms.superadmin.extensions
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.CountDownTimer
import android.view.Gravity
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.card.MaterialCardView
import com.wms.superadmin.R


fun Context.customAlert(Title:String, Message:String,Status:Boolean)
{


      val  mDialogCType = Dialog(this)
        mDialogCType.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialogCType.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val shake: Animation = AnimationUtils.loadAnimation(this, R.anim.shake);

        val window: Window? = mDialogCType!!.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.CENTER
        mDialogCType.setContentView(R.layout.custom_alert)
      val  title= mDialogCType.findViewById(R.id.Title) as TextView
     val   icon= mDialogCType.findViewById(R.id.icon) as ImageView
      val message= mDialogCType.findViewById(R.id.message) as TextView
      val card= mDialogCType.findViewById(R.id.card) as MaterialCardView
        icon.startAnimation(shake)
      if(Status){
          card.setCardBackgroundColor(this.getColor(R.color.green_600))
          icon.setImageResource(R.drawable.ic_success)

      }
       else{
          card.setCardBackgroundColor(this.getColor(R.color.red_600))
          icon.setImageResource(R.drawable.ic_alert)

      }
        title.text=Title
        message.text=Message

        mDialogCType.setCancelable(true)
        mDialogCType.show()
}
fun Context.showToast(msg: String?,period:Int) {
    val  mToastToShow = Toast.makeText(this, msg, Toast.LENGTH_LONG)
    // Set the countdown to display the toast
    val toastCountDown: CountDownTimer
    toastCountDown =
        object : CountDownTimer(period.toLong(), 1000 /*Tick duration*/) {
            override fun onTick(millisUntilFinished: Long) {
                mToastToShow.show()
            }

            override fun onFinish() {
                mToastToShow.cancel()
            }
        }
    // Show the toast and starts the countdown
    mToastToShow.show()
    toastCountDown.start()
}
