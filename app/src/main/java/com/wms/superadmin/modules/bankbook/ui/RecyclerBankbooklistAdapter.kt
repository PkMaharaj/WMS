package com.wms.superadmin.modules.bankbook.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowBankbook1Binding
import com.wms.superadmin.modules.bankbook.`data`.model.Bankbook1RowModel
import kotlin.Int
import kotlin.Unit
import kotlin.collections.List

public class RecyclerBankbooklistAdapter(
  public var list: List<Bankbook1RowModel>
) : RecyclerView.Adapter<RecyclerBankbooklistAdapter.RowBankbook1VH>() {
  private var clickListener: OnItemClickListener? = null

  public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowBankbook1VH {
    val view=LayoutInflater.from(parent.context).inflate(R.layout.row_bankbook1,parent,false)
    return RowBankbook1VH(view,clickListener!!)
  }

  public override fun onBindViewHolder(holder: RowBankbook1VH, position: Int): Unit {
    val bankbook1RowModel = list[position]
    holder.binding.bankbook1RowModel = bankbook1RowModel
  }
  public override fun getItemCount(): Int = list.size

  public fun updateData(newData: List<Bankbook1RowModel>): Unit {
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
      item: Bankbook1RowModel
    ): Unit {
    }
  }

  public inner class RowBankbook1VH(view: View,listener:OnItemClickListener) : RecyclerView.ViewHolder(view) {
    public val binding: RowBankbook1Binding = RowBankbook1Binding.bind(itemView)
    init {
        binding.linearGroup.setOnClickListener {
          listener.onItemClick(it,adapterPosition,list[adapterPosition])
        }
    }
  }
}
