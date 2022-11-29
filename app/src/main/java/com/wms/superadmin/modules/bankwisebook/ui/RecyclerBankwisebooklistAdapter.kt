package com.wms.superadmin.modules.bankwisebook.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowBankwisebook1Binding
import com.wms.superadmin.modules.bankwisebook.`data`.model.Bankwisebook1RowModel
import kotlin.Int
import kotlin.Unit
import kotlin.collections.List

public class RecyclerBankwisebooklistAdapter(
  public var list: List<Bankwisebook1RowModel>
) : RecyclerView.Adapter<RecyclerBankwisebooklistAdapter.RowBankwisebook1VH>() {
  private var clickListener: OnItemClickListener? = null

  public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowBankwisebook1VH {
    val view=LayoutInflater.from(parent.context).inflate(R.layout.row_bankwisebook1,parent,false)
    return RowBankwisebook1VH(view)
  }

  public override fun onBindViewHolder(holder: RowBankwisebook1VH, position: Int): Unit {
    val bankwisebook1RowModel = list[position]
    holder.binding.bankwisebook1RowModel = bankwisebook1RowModel
  }

  public override fun getItemCount(): Int = list.size

  public fun updateData(newData: List<Bankwisebook1RowModel>): Unit {
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
      item: Bankwisebook1RowModel
    ): Unit {
    }
  }

  public inner class RowBankwisebook1VH(
    view: View
  ) : RecyclerView.ViewHolder(view) {
    public val binding: RowBankwisebook1Binding = RowBankwisebook1Binding.bind(itemView)
    init {
        binding.linearGroup.setOnClickListener {
          clickListener!!.onItemClick(itemView,adapterPosition,list[adapterPosition])
        }
    }
  }
}
