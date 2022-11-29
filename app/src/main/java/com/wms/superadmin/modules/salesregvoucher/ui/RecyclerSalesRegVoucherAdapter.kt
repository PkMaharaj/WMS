package com.wms.superadmin.modules.salesregvoucher.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowSalesRegVoucher1Binding
import com.wms.superadmin.modules.salesregvoucher.data.model.SalesRegVoucher1RowModel

class RecyclerSalesRegVoucherAdapter(var mlist: List<SalesRegVoucher1RowModel>) : RecyclerView.Adapter<RecyclerSalesRegVoucherAdapter.RowSalesRegVoucher1VH>(),
  Filterable {
  private var clickListener: OnItemClickListener? = null

  var ItemList = arrayListOf<SalesRegVoucher1RowModel>()
  var list  = arrayListOf<SalesRegVoucher1RowModel>()
  init {
    ItemList = mlist as ArrayList<SalesRegVoucher1RowModel>
    list = mlist as ArrayList<SalesRegVoucher1RowModel>
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowSalesRegVoucher1VH {
    val view=LayoutInflater.from(parent.context).inflate(R.layout.row_sales_reg_voucher1,parent,false)
    return RowSalesRegVoucher1VH(view,clickListener!!)
  }

  override fun onBindViewHolder(holder: RowSalesRegVoucher1VH, position: Int) {
    val salesRegVoucher1RowModel = ItemList[position]
    holder.binding.salesRegVoucher1RowModel = salesRegVoucher1RowModel
  }

  override fun getItemCount(): Int = ItemList.size

  public fun updateData(newData: ArrayList<SalesRegVoucher1RowModel>) {
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


  override fun getFilter(): Filter {
    return object : Filter() {
      override fun performFiltering(constraint: CharSequence?): FilterResults {
        val charSearch = constraint.toString()
        if (charSearch.isEmpty()) {
          ItemList = list as ArrayList<SalesRegVoucher1RowModel>
        } else{
          val resultList = ArrayList<SalesRegVoucher1RowModel>()

          for (row in list!!) {
            if (row.txtParticulars?.contains(charSearch, true)!!
              ||( row.txtDate?.contains(charSearch, true))!!
              ||( row.voucherid?.contains(charSearch, true))!!) {
              resultList.add(row)
            }
          }
          ItemList = resultList
          Log.e("FilterAdapter", "$ItemList")
        }
        val filterResults = FilterResults()
        filterResults.values = ItemList
        return filterResults
      }

      override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        try {
          ItemList = results?.values as ArrayList<SalesRegVoucher1RowModel>

          notifyDataSetChanged()
        } catch (Ex: Exception) {
        }
      }
    }
  }


//  override fun getFilter(): Filter {
//    return object : Filter() {
//      override fun performFiltering(constraint: CharSequence?): FilterResults {
//        val charSearch = constraint.toString()
//        if (charSearch.isEmpty()) {
//          list = list as ArrayList<SalesRegVoucher1RowModel>
//        } else{
//          val resultList = ArrayList<SalesRegVoucher1RowModel>()
//
//          for (row in list!!) {
//            if (row.txtDate?.contains(charSearch, true)!!
//              ||( row.Ordernumber?.contains(charSearch, true))!!
//              ||( row.txtParticulars?.contains(charSearch, true))!!) {
//              resultList.add(row)
//            }
//          }
//          list = resultList
//          Log.e("FilterAdapter", "$list")
//        }
//        val filterResults = FilterResults()
//        filterResults.values = list
//        return filterResults
//      }
//
//      override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//        try {
//          list = results?.values as ArrayList<SalesRegVoucher1RowModel>
//
//          notifyDataSetChanged()
//        } catch (Ex: Exception) {
//        }
//      }
//    }
//  }

  fun setOnItemClickListener(clickListener: OnItemClickListener) {
    this.clickListener = clickListener
  }

  interface OnItemClickListener {
    fun onItemClick(view: View, position: Int, item: SalesRegVoucher1RowModel) {
    }
  }

  inner class RowSalesRegVoucher1VH(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
    val binding: RowSalesRegVoucher1Binding = RowSalesRegVoucher1Binding.bind(itemView)
    init {
      binding.constraintGroup.setOnClickListener {
        listener.onItemClick(it, adapterPosition, list[adapterPosition])
      }
    }
  }
}
