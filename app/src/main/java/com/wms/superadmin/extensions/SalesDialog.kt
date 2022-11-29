package com.wms.superadmin.extensions

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.databinding.SalesdialogBinding
import com.wms.superadmin.modules.salesorderbranch.ui.SalesOrderBranchActivity
import com.wms.superadmin.modules.salesregbranches.ui.SalesRegBranchesActivity
import com.wms.superadmin.network.models.Login.LoginResponse

class SalesDialog (var context:Context,var isSales:Boolean,var mFromDate:String, var mToDate:String, var userDetails:LoginResponse) {
    var dialog=AlertDialog.Builder(context)
        .apply {
            val layoutInflater = LayoutInflater.from(context)
            val  shake= AnimationUtils.loadAnimation(context, R.anim.slide_up);
            val binding = SalesdialogBinding.inflate(layoutInflater, null, false)
            setView(binding.root)

            binding.order.text=if(isSales)"Sales Order" else "Purchase Order"
            binding.register.text=if(isSales)"Sales Register" else "Purchase Register"

            binding.salesorder.setOnClickListener {
                if(isSales){
                    if(userDetails.screenSettings.sales_order)
                    {
                        val intent = SalesOrderBranchActivity.getIntent(context,null)
                       /* intent.putExtra(IntentParams.ORDERSCREEN,ReportTypes.SALESORDER)
                        intent.putExtra(IntentParams.FROM_DATE, mFromDate)
                        intent.putExtra(IntentParams.To_DATE, mToDate)
                        context.startActivity(intent)*/
                        sendActivity(intent,IntentParams.ORDERSCREEN,ReportTypes.SALESORDER)

                    }  else
                        context.customAlert("Alert..!", MyApp.getInstance().resources.getString(R.string.notAccess_msg),false)
                }else{
                    if(userDetails.screenSettings.purchase_order)
                    {
                        val intent = SalesOrderBranchActivity.getIntent(context,null)
                        /*intent.putExtra(IntentParams.ORDERSCREEN,ReportTypes.PURCHASEORDER)
                        intent.putExtra(IntentParams.FROM_DATE, mFromDate)
                        intent.putExtra(IntentParams.To_DATE, mToDate)
                        context.startActivity(intent)*/
                        sendActivity(intent,IntentParams.ORDERSCREEN,ReportTypes.PURCHASEORDER)

                    }  else
                        context.customAlert("Alert..!", MyApp.getInstance().resources.getString(R.string.notAccess_msg),false)

                }
              //  binding.saleslayout.visibility = View.GONE



            }

            binding.salesregister.setOnClickListener {
                if(isSales){
                    if(userDetails.screenSettings.sales_register)
                    {
                        val intent = SalesRegBranchesActivity.getIntent(context,null)
                       /* intent.putExtra(IntentParams.REGISTERSCREEN,ReportTypes.SALESREGISTER)
                        intent.putExtra(IntentParams.FROM_DATE, mFromDate)
                        intent.putExtra(IntentParams.To_DATE, mToDate)
                        context.startActivity(intent)*/
                        sendActivity(intent,IntentParams.REGISTERSCREEN,ReportTypes.SALESREGISTER)


                    }  else
                        context.customAlert("Alert..!", MyApp.getInstance().resources.getString(R.string.notAccess_msg),false)
                }else{
                    if(userDetails.screenSettings.purchase_register)
                    {
                        val intent = SalesRegBranchesActivity.getIntent(context,null)
                        /*intent.putExtra(IntentParams.REGISTERSCREEN,ReportTypes.PURCHASEREGISTER)
                        intent.putExtra(IntentParams.FROM_DATE, mFromDate)
                        intent.putExtra(IntentParams.To_DATE, mToDate)
                        context.startActivity(intent)*/
                        sendActivity(intent,IntentParams.REGISTERSCREEN,ReportTypes.PURCHASEREGISTER)

                    }  else
                        context.customAlert("Alert..!", MyApp.getInstance().resources.getString(R.string.notAccess_msg),false)

                }
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
    fun sendActivity(intent:Intent,IntentScreen:String,ScreenParam:String){
        intent.putExtra(IntentScreen,ScreenParam)
        intent.putExtra(IntentParams.FROM_DATE, mFromDate)
        intent.putExtra(IntentParams.To_DATE, mToDate)
        context.startActivity(intent)


    }

}
