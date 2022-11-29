package com.wms.superadmin.modules.vocherbankbook.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowVocherbankbook1Binding
import com.wms.superadmin.modules.vocherbankbook.`data`.model.Vocherbankbook1RowModel
import kotlin.Int
import kotlin.Unit
import kotlin.collections.List

public class RecyclerVocherwisebooklistAdapter(public var list: List<Vocherbankbook1RowModel>) : RecyclerView.Adapter<RecyclerVocherwisebooklistAdapter.RowVocherbankbook1VH>() {
  private var clickListener: OnItemClickListener? = null

  public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowVocherbankbook1VH {
    val view=LayoutInflater.from(parent.context).inflate(R.layout.row_vocherbankbook1,parent,false)
    return RowVocherbankbook1VH(view)
  }

  public override fun onBindViewHolder(holder: RowVocherbankbook1VH, position: Int): Unit {
     val vocherbankbook1RowModel = list[position]
    holder.binding.vocherbankbook1RowModel = vocherbankbook1RowModel
  }

  public override fun getItemCount(): Int = list.size
  // TODO uncomment following line after integration with data source
  // return list.size

  public fun updateData(newData: List<Vocherbankbook1RowModel>): Unit {
    list = newData
    notifyDataSetChanged()
  }

  public fun setOnItemClickListener(clickListener: OnItemClickListener): Unit {
    this.clickListener = clickListener
  }

  public interface OnItemClickListener {
    public fun onItemClick(
      view: View,
      position: Int,
      item: Vocherbankbook1RowModel
    ): Unit {
    }
  }

  public inner class RowVocherbankbook1VH(
    view: View
  ) : RecyclerView.ViewHolder(view) {
    public val binding: RowVocherbankbook1Binding = RowVocherbankbook1Binding.bind(itemView)
  }
}
