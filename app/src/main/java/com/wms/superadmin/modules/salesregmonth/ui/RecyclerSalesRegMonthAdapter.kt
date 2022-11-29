package com.wms.superadmin.modules.salesregmonth.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowSalesRegMonth1Binding
import com.wms.superadmin.modules.salesregbranches.data.model.SalesRegBranches1RowModel
import com.wms.superadmin.modules.salesregmonth.data.model.SalesRegMonth1RowModel
import java.lang.Exception
import kotlin.Int
import kotlin.collections.List

class RecyclerSalesRegMonthAdapter(var mlist: List<SalesRegMonth1RowModel>) : RecyclerView.Adapter<RecyclerSalesRegMonthAdapter.RowSalesRegMonth1VH>(), Filterable {
  private var clickListener: OnItemClickListener? = null

  var ItemList = arrayListOf<SalesRegMonth1RowModel>()
  var list  = arrayListOf<SalesRegMonth1RowModel>()
  init {
    ItemList = mlist as ArrayList<SalesRegMonth1RowModel>
    list = mlist as ArrayList<SalesRegMonth1RowModel>
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowSalesRegMonth1VH {
    val view=LayoutInflater.from(parent.context).inflate(R.layout.row_sales_reg_month1,parent,false)
    return RowSalesRegMonth1VH(view,clickListener!!)
  }

  override fun onBindViewHolder(holder: RowSalesRegMonth1VH, position: Int) {
    val salesRegMonth1RowModel = ItemList[position]
    holder.binding.salesRegMonth1RowModel = salesRegMonth1RowModel
  }

  override fun getItemCount(): Int = ItemList.size

  public fun updateData(newData: ArrayList<SalesRegMonth1RowModel>) {
    if (ItemList!!.size > 0) {
      ItemList!!.clear()
      notifyItemRangeRemoved(0, ItemList!!.size);
    }
    if (list!!.size > 0) {
      list!!.clear()
      notifyItemRangeRemoved(0, list!!.size);
    }
    list = newData as ArrayList<SalesRegMonth1RowModel>
    ItemList = newData
    // notifyItemRangeChanged(0, newData.size);
    notifyDataSetChanged()
  }

  override fun getFilter(): android.widget.Filter {
    return object : android.widget.Filter() {
      override fun performFiltering(constraint: CharSequence?): FilterResults {
        val charSearch = constraint.toString()
        if (charSearch.isEmpty()) {
          ItemList = list as ArrayList<SalesRegMonth1RowModel>
        } else{
          val resultList = ArrayList<SalesRegMonth1RowModel>()

          for (row in list!!) {
            if (row.txtMonthSalesregbranch?.contains(charSearch, true)!!){
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
          ItemList = results?.values as ArrayList<SalesRegMonth1RowModel>

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
    fun onItemClick(view: View, position: Int, item: SalesRegMonth1RowModel) {
    }
  }

  inner class RowSalesRegMonth1VH(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
    val binding: RowSalesRegMonth1Binding = RowSalesRegMonth1Binding.bind(itemView)
    init {
      binding.constraintGroup.setOnClickListener {
        listener.onItemClick(it, adapterPosition, list[adapterPosition])
      }
    }
  }
}
