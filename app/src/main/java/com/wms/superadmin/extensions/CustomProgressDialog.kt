package com.wms.superadmin.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.wms.superadmin.R
import com.wms.superadmin.databinding.LayoutProgressDialogBinding

/**
 * To show progress dialog for long running tasks.
 * @param setCancellationOnTouchOutside : true, if dialog should be dismissed on outside click
 */
fun Context.showProgressDialog(
    message: String? = null,
    setCancellationOnTouchOutside: Boolean = false
): AlertDialog {
    AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
        .apply {
            val layoutInflater = LayoutInflater.from(this@showProgressDialog)
            val binding = LayoutProgressDialogBinding.inflate(layoutInflater, null, false)

            setView(binding.root)

            if (!message.isNullOrBlank()) {
                binding.message = message
            } else {
                binding.message = getString(R.string.loading)
            }
            val dialog = create()
            dialog.setCanceledOnTouchOutside(setCancellationOnTouchOutside)
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.show()

            return dialog
        }
}
