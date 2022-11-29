package com.wms.superadmin.modules.salesregday.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowSalesRegDay1Binding
import com.wms.superadmin.modules.salesregday.data.model.SalesRegDay1RowModel
import com.wms.superadmin.modules.salesregmonth.data.model.SalesRegMonth1RowModel
import java.lang.Exception
import kotlin.Int
import kotlin.collections.List

class RecyclerSalesRegDayAdapter(var mlist: List<SalesRegDay1RowModel>) : RecyclerView.Adapter<RecyclerSalesRegDayAdapter.RowSalesRegDay1VH>(), Filterable {
  private var clickListener: OnItemClickListener? = null

  var ItemList = arrayListOf<SalesRegDay1RowModel>()
  var list  = arrayListOf<SalesRegDay1RowModel>()
  init {
    ItemList = mlist as ArrayList<SalesRegDay1RowModel>
    list = mlist as ArrayList<SalesRegDay1RowModel>
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowSalesRegDay1VH {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.row_sales_reg_day1, parent, false)
    return RowSalesRegDay1VH(view, clickListener!!)
  }

  override fun onBindViewHolder(holder: RowSalesRegDay1VH, position: Int) {
    val salesRegDay1RowModel = ItemList[position]
    holder.binding.salesRegDay1RowModel = salesRegDay1RowModel
  }

  override fun getItemCount(): Int = ItemList.size

  public fun updateData(newData: ArrayList<SalesRegDay1RowModel>) {
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
          ItemList = list as ArrayList<SalesRegDay1RowModel>
        } else{
          val resultList = ArrayList<SalesRegDay1RowModel>()

          for (row in list!!) {
            if (row.txtDateSalesregday?.contains(charSearch, true)!!){
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
          ItemList = results?.values as ArrayList<SalesRegDay1RowModel>

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
    fun onItemClick(view: View, position: Int, item: SalesRegDay1RowModel) {
    }
  }

  inner class RowSalesRegDay1VH(view: View, listner: OnItemClickListener) :
    RecyclerView.ViewHolder(view) {
    val binding: RowSalesRegDay1Binding = RowSalesRegDay1Binding.bind(itemView)
    init {
      binding.constraintGroupVoucher.setOnClickListener {
        listner.onItemClick(it, adapterPosition, list[adapterPosition])
      }
    }
  }

}
