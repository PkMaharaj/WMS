package com.wms.superadmin.modules.notificationapproval.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowNotificationApprovalItemdiscountsBinding
import com.wms.superadmin.modules.notificationapproval.data.model.NotificationApprovalRowModel
import kotlin.Int
import kotlin.collections.List

class NotificationItemdiscountAdapter(var list: List<NotificationApprovalRowModel>) : RecyclerView.Adapter<NotificationItemdiscountAdapter.RowNotificationApproval3VH>() {
  private var clickListener: OnItemClickListener? = null
  lateinit var context: Context

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowNotificationApproval3VH {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.row_notification_approval_itemdiscounts, parent, false)
    return RowNotificationApproval3VH(view)
  }

  override fun onBindViewHolder(holder: RowNotificationApproval3VH, position: Int) {
    val notificationApproval3RowModel = list[position]
    holder.binding.notificationApprovalRowModel = notificationApproval3RowModel
  }

  override fun getItemCount(): Int = list.size

  public fun updateData(newData: MutableList<NotificationApprovalRowModel>) {
    list = newData
    notifyDataSetChanged()
  }

  fun setOnItemClickListener(clickListener: OnItemClickListener) {
    this.clickListener = clickListener
  }

  interface OnItemClickListener {
    fun onItemClick(view: View, position: Int, item: NotificationApprovalRowModel) {
    }
  }

  inner class RowNotificationApproval3VH(view: View) : RecyclerView.ViewHolder(view) {
    val binding: RowNotificationApprovalItemdiscountsBinding =
      RowNotificationApprovalItemdiscountsBinding.bind(itemView)

    init {
      binding.chxItemdiscount.setOnClickListener {
        var item = list[adapterPosition]
        item.isDiscountApproved = binding.chxItemdiscount.isChecked
      }
      binding.txtchangeamt.addTextChangedListener {
        var item = list[adapterPosition]
        item.changeItemDiscounts = binding.txtchangeamt.text.trim().toString()
      }
    }
  }
}
