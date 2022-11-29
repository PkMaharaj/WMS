package com.wms.superadmin.modules.salesregbranches.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowSalesRegBranches1Binding
import com.wms.superadmin.modules.salesregbranches.data.model.SalesRegBranches1RowModel

class RecyclerSalesRegBranchesAdapter(var mlist: List<SalesRegBranches1RowModel>) : RecyclerView.Adapter<RecyclerSalesRegBranchesAdapter.RowSalesRegBranches1VH>(), Filterable {
  private var clickListener: OnItemClickListener? = null

  var ItemList = arrayListOf<SalesRegBranches1RowModel>()
  var list  = arrayListOf<SalesRegBranches1RowModel>()
  init {
    ItemList = mlist as ArrayList<SalesRegBranches1RowModel>
    list = mlist as ArrayList<SalesRegBranches1RowModel>
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowSalesRegBranches1VH {
    val view=LayoutInflater.from(parent.context).inflate(R.layout.row_sales_reg_branches1,parent,false)
    return RowSalesRegBranches1VH(view, clickListener!!)
  }

  override fun onBindViewHolder(holder: RowSalesRegBranches1VH, position: Int) {
    val salesRegBranches1RowModel = ItemList[position]
    holder.binding.salesRegBranches1RowModel = salesRegBranches1RowModel
  }

  override fun getItemCount(): Int = ItemList.size

  public fun updateData(newData: ArrayList<SalesRegBranches1RowModel>) {
    if (ItemList!!.size > 0) {
      ItemList!!.clear()
      notifyItemRangeRemoved(0, ItemList!!.size);
    }
    if (list!!.size > 0) {
      list!!.clear()
      notifyItemRangeRemoved(0, list!!.size);
    }
    list = newData
    ItemList = newData
    // notifyItemRangeChanged(0, newData.size);
    notifyDataSetChanged()
  }


  override fun getFilter(): android.widget.Filter {
    return object : android.widget.Filter() {
      override fun performFiltering(constraint: CharSequence?): FilterResults {
        val charSearch = constraint.toString()
        if (charSearch.isEmpty()) {
          ItemList = list as ArrayList<SalesRegBranches1RowModel>
        } else{
          val resultList = ArrayList<SalesRegBranches1RowModel>()

          for (row in list!!) {
            if (row.branchname?.contains(charSearch, true)!!){
              resultList.add(row)
            }
          }
          ItemList = resultList
          Log.e("FilterAdapter", "$list")
        }
        val filterResults = FilterResults()
        filterResults.values = ItemList
        return filterResults
      }

      override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        try {
          ItemList = results?.values as ArrayList<SalesRegBranches1RowModel>

          notifyDataSetChanged()
        } catch (Ex: Exception) {
        }
      }
    }
  }

  fun setOnItemClickListener(clickListener: OnItemClickListener) {
    this.clickListener = clickListener
  }

  interface OnItemClickListener {
    fun onItemClick(view: View, position: Int, item: SalesRegBranches1RowModel) {}
  }

  inner class RowSalesRegBranches1VH(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
    val binding: RowSalesRegBranches1Binding = RowSalesRegBranches1Binding.bind(itemView)
    init {
      binding.SalesregbaranchGroup.setOnClickListener {
        listener.onItemClick(it, adapterPosition, ItemList[adapterPosition])
      }
    }
  }

}
