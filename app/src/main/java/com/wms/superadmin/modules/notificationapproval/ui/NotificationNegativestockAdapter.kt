package com.wms.superadmin.modules.notificationapproval.ui

import android.content.Context
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowNotificationApprovalNegativestockBinding
import com.wms.superadmin.modules.notificationapproval.data.model.NotificationApprovalRowModel

class NotificationNegativestockAdapter(
  var list: List<NotificationApprovalRowModel>
) : RecyclerView.Adapter<NotificationNegativestockAdapter.RowNotificationApproval1VH>() {
  private var clickListener: OnItemClickListener? = null
  private val context: Context? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowNotificationApproval1VH {
    val view=LayoutInflater.from(parent.context).inflate(R.layout.row_notification_approval_negativestock,parent,false)
    return RowNotificationApproval1VH(view,clickListener!!)
  }

  override fun onBindViewHolder(holder: RowNotificationApproval1VH, position: Int) {

     val notificationApproval1RowModel = list[position]
    holder.binding.notificationApproval1RowModel = notificationApproval1RowModel
  }

  override fun getItemCount(): Int = list.size
  // TODO uncomment following line after integration with data source
  // return list.size

  public fun updateData(newData: List<NotificationApprovalRowModel>) {
    list = newData
    notifyDataSetChanged()
  }

  fun setOnItemClickListener(clickListener: OnItemClickListener) {
    this.clickListener = clickListener
  }

  interface OnItemClickListener : NotificationFIFOSkippedAdapter.OnItemClickListener {
    override fun onItemClick(view: View, position: Int, item: NotificationApprovalRowModel)
  }

  inner class RowNotificationApproval1VH(view: View, listner: OnItemClickListener) : RecyclerView.ViewHolder(view) {
    val binding: RowNotificationApprovalNegativestockBinding = RowNotificationApprovalNegativestockBinding.bind(itemView)
    init {
      binding.chxNegative.setOnClickListener {
        var item=list[adapterPosition]
        item.isNegitiveApproved=binding.chxNegative.isChecked
      }
      binding.etchangeqty.addTextChangedListener {
        listner.onItemClick(binding.etchangeqty,adapterPosition,list[adapterPosition])
      }
    }
  }
}
