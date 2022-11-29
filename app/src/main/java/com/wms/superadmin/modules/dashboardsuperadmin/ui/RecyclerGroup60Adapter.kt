package com.wms.superadmin.modules.dashboardsuperadmin.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowDashboardSuperadmin2Binding
import com.wms.superadmin.modules.dashboardsuperadmin.`data`.model.DashboardSuperadmin2RowModel
import kotlin.Int
import kotlin.Unit
import kotlin.collections.List

public class RecyclerGroup60Adapter(
  public var list: List<DashboardSuperadmin2RowModel>
) : RecyclerView.Adapter<RecyclerGroup60Adapter.RowDashboardSuperadmin2VH>() {
  private var clickListener: OnItemClickListener? = null

  public fun updateData(newData: List<DashboardSuperadmin2RowModel>): Unit {
    list = newData
    notifyDataSetChanged()
  }

  public fun setOnItemClickListener(clickListener: OnItemClickListener): Unit {
    this.clickListener = clickListener
  }

  public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
      RowDashboardSuperadmin2VH {
    val
        view=LayoutInflater.from(parent.context).inflate(R.layout.row_dashboard_superadmin2,parent,false)
    return RowDashboardSuperadmin2VH(view)
  }

  public override fun onBindViewHolder(holder: RowDashboardSuperadmin2VH, position: Int): Unit {
    val dashboardSuperadmin2RowModel = DashboardSuperadmin2RowModel()
    // TODO uncomment following line after integration with data source
    // val dashboardSuperadmin2RowModel = list[position]
    holder.binding.dashboardSuperadmin2RowModel = dashboardSuperadmin2RowModel
  }

  public override fun getItemCount(): Int = 4
  // TODO uncomment following line after integration with data source
  // return list.size

  public interface OnItemClickListener {
    public fun onItemClick(
      view: View,
      position: Int,
      item: DashboardSuperadmin2RowModel
    ): Unit {
    }
  }

  public inner class RowDashboardSuperadmin2VH(
    view: View
  ) : RecyclerView.ViewHolder(view) {
    public val binding: RowDashboardSuperadmin2Binding =
        RowDashboardSuperadmin2Binding.bind(itemView)
    init {
      binding.imageGroup319.setOnClickListener {
        // TODO replace with value from datasource
        clickListener?.onItemClick(it, adapterPosition, DashboardSuperadmin2RowModel())
      }
    }
  }
}
