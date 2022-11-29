package com.wms.superadmin.modules.dashboardsuperadmin.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowDashboardSuperadmin1Binding
import com.wms.superadmin.modules.dashboardsuperadmin.`data`.model.DashboardSuperadmin1RowModel
import kotlin.Int
import kotlin.Unit
import kotlin.collections.List

public class RecyclerDashboardSuperadminlistAdapter(public var list: List<DashboardSuperadmin1RowModel>) : RecyclerView.Adapter<RecyclerDashboardSuperadminlistAdapter.RowDashboardSuperadmin1VH>() {
  private var clickListener: OnItemClickListener? = null

  public fun updateData(newData: List<DashboardSuperadmin1RowModel>): Unit {
    list = newData
    notifyDataSetChanged()
  }

  public fun setOnItemClickListener(clickListener: OnItemClickListener): Unit {
    this.clickListener = clickListener
  }

  public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowDashboardSuperadmin1VH {
    val view=LayoutInflater.from(parent.context).inflate(R.layout.row_dashboard_superadmin1,parent,false)
    return RowDashboardSuperadmin1VH(view)
  }

  public override fun onBindViewHolder(holder: RowDashboardSuperadmin1VH, position: Int): Unit {
    val dashboardSuperadmin1RowModel = DashboardSuperadmin1RowModel()
    // TODO uncomment following line after integration with data source
    // val dashboardSuperadmin1RowModel = list[position]
    holder.binding.dashboardSuperadmin1RowModel = dashboardSuperadmin1RowModel
  }

  public override fun getItemCount(): Int = 2
  // TODO uncomment following line after integration with data source
  // return list.size

  public interface OnItemClickListener {
    public fun onItemClick(
      view: View,
      position: Int,
      item: DashboardSuperadmin1RowModel
    ): Unit {
    }
  }

  public inner class RowDashboardSuperadmin1VH(view: View) : RecyclerView.ViewHolder(view) {
    public val binding: RowDashboardSuperadmin1Binding = RowDashboardSuperadmin1Binding.bind(itemView)
  }
}
