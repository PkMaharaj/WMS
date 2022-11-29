package com.wms.superadmin.modules.salesordervoucher.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowSalesOrderVoucher1Binding
import com.wms.superadmin.modules.salesorderbranch.data.model.SalesOrderRowModel
import com.wms.superadmin.modules.salesordervoucher.data.model.SalesOrderVoucher1RowModel
import kotlin.Int
import kotlin.Unit
import kotlin.collections.List

public class RecyclerSalesOrderVoucherAdapter(
  public var list: List<SalesOrderVoucher1RowModel>
) : RecyclerView.Adapter<RecyclerSalesOrderVoucherAdapter.RowSalesOrderVoucher1VH>() {
  private var clickListener: OnItemClickListener? = null

  public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
          RowSalesOrderVoucher1VH {
    val
        view=LayoutInflater.from(parent.context).inflate(R.layout.row_sales_order_voucher1,parent,false)
    return RowSalesOrderVoucher1VH(view)
  }

  public override fun onBindViewHolder(holder: RowSalesOrderVoucher1VH, position: Int): Unit {
    val salesOrderVoucher1RowModel = list[position]
//    holder.binding.salesOrderVoucher1RowModel = salesOrderVoucher1RowModel
  }

  public override fun getItemCount(): Int = list.size

  public fun updateData(newData: MutableList<SalesOrderRowModel>): Unit {
//    list = newData
    notifyDataSetChanged()
  }

  public fun setOnItemClickListener(clickListener: OnItemClickListener): Unit {
    this.clickListener = clickListener
  }

  public interface OnItemClickListener {
    public fun onItemClick(
      view: View,
      position: Int,
      item: SalesOrderVoucher1RowModel
    ): Unit {
    }
  }

  public inner class RowSalesOrderVoucher1VH(
    view: View
  ) : RecyclerView.ViewHolder(view) {
    public val binding: RowSalesOrderVoucher1Binding = RowSalesOrderVoucher1Binding.bind(itemView)
  }
}
