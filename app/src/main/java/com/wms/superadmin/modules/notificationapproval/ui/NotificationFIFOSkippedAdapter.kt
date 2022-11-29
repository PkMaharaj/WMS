package com.wms.superadmin.modules.notificationapproval.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowNotificationApprovalFifoskippedBinding
import com.wms.superadmin.modules.notificationapproval.data.model.NotificationApprovalRowModel
import kotlin.Int
import kotlin.collections.List

class NotificationFIFOSkippedAdapter(
  var list: List<NotificationApprovalRowModel>
) : RecyclerView.Adapter<NotificationFIFOSkippedAdapter.RowNotificationApproval2VH>() {
  private var clickListener: OnItemClickListener? = null

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowNotificationApproval2VH {
    val view=LayoutInflater.from(parent.context).inflate(R.layout.row_notification_approval_fifoskipped,parent,false)
    return RowNotificationApproval2VH(view,clickListener!!)
  }

  override fun onBindViewHolder(holder: RowNotificationApproval2VH, position: Int) {
    val notificationApproval2RowModel = list[position]
    holder.binding.notificationApproval2RowModel = notificationApproval2RowModel
  }

  override fun getItemCount(): Int = list.size

  public fun updateData(newData: List<NotificationApprovalRowModel>) {
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

  inner class RowNotificationApproval2VH(view: View,listener:OnItemClickListener) : RecyclerView.ViewHolder(view) {
    val binding: RowNotificationApprovalFifoskippedBinding = RowNotificationApprovalFifoskippedBinding.bind(itemView)
    init {
        binding.chxFifo.setOnClickListener {
          var item=list[adapterPosition]
          item.isFifoApproved=binding.chxFifo.isChecked
        }
    }
  }
}
