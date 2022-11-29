package com.wms.superadmin.modules.salesorderbranch.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowSalesOrderBranch1Binding
import com.wms.superadmin.databinding.RowSalesOrderMonthly1Binding
import com.wms.superadmin.databinding.RowSalesOrderVoucher1Binding
import com.wms.superadmin.modules.salesorderbranch.data.model.SalesOrderRowModel
import com.wms.superadmin.modules.salesregbranches.data.model.SalesRegBranches1RowModel
import java.lang.Exception
import kotlin.Int
import kotlin.Unit
import kotlin.collections.List

public class RecyclerSalesOrderAdapter(public var mlist: List<SalesOrderRowModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
  private var total: Int = 0
  private var currentViewHolder=0
  private var clickListener: OnItemClickListener? = null

  var ItemList = arrayListOf<SalesOrderRowModel>()
  var list  = arrayListOf<SalesOrderRowModel>()
  init {
    ItemList = mlist as ArrayList<SalesOrderRowModel>
    list = mlist as ArrayList<SalesOrderRowModel>
  }


  public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    if (currentViewHolder==0) {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.row_sales_order_branch1, parent, false)
      return RowSalesOrderVH(view,clickListener!!)
    }
    else if (currentViewHolder==1){
      val view = LayoutInflater.from(parent.context).inflate(R.layout.row_sales_order_monthly1, parent, false)
      return RowSalesOrderMonthly1VH(view,clickListener!!)
    }
    else{
      val view = LayoutInflater.from(parent.context).inflate(R.layout.row_sales_order_voucher1,parent,false)
      return RowSalesOrderVoucher1VH(view,clickListener!!)
    }
  }

  public override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int): Unit {

    val salesOrderRowModel = ItemList[position]
    if (holder is RowSalesOrderVH) {
      holder.binding.salesOrderRowModel = salesOrderRowModel
    }
    else if (holder is RowSalesOrderMonthly1VH){
      holder.binding.salesOrderMonthly1RowModel = salesOrderRowModel
    }
    else if (holder is RowSalesOrderVoucher1VH){
      holder.binding.salesOrderVoucher1RowModel = salesOrderRowModel
    }
  }

  public override fun getItemCount(): Int = ItemList.size

  public fun updateData(newData: ArrayList<SalesOrderRowModel>,viewHolder:Int): Unit {
    currentViewHolder=viewHolder
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

    notifyDataSetChanged()
  }

  override fun getFilter(): android.widget.Filter {
    return object : android.widget.Filter() {
      override fun performFiltering(constraint: CharSequence?): FilterResults {
        val charSearch = constraint.toString()
        if (charSearch.isEmpty()) {
          ItemList = list as ArrayList<SalesOrderRowModel>
        } else{
          val resultList = ArrayList<SalesOrderRowModel>()

          for (row in list!!) {
            if (row.so_branchname?.contains(charSearch, true)!!
              || row.txtMonth?.contains(charSearch,true)!!
              || row.txtDate?.contains(charSearch,true)!!
              || row.txtparticulars?.contains(charSearch,true)!!
            ){
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
          ItemList = results?.values as ArrayList<SalesOrderRowModel>

          notifyDataSetChanged()
        } catch (Ex: Exception) {
        }
      }
    }
  }

  public fun setOnItemClickListener(clickListener: OnItemClickListener): Unit {
    this.clickListener = clickListener
  }

  public interface OnItemClickListener {
    public fun onItemClick(
      view: View,
      position: Int,
      item: SalesOrderRowModel
    ): Unit {
    }
  }

  public inner class RowSalesOrderVH(view: View,listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
    public val binding: RowSalesOrderBranch1Binding = RowSalesOrderBranch1Binding.bind(itemView)
    init {
      binding.constraintGroup57.setOnClickListener {
        listener.onItemClick(it, adapterPosition, list[adapterPosition])
      }
    }
  }

  public inner class RowSalesOrderMonthly1VH(view: View,listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
    public val binding: RowSalesOrderMonthly1Binding = RowSalesOrderMonthly1Binding.bind(itemView)
    init {
      binding.constraintGroup9.setOnClickListener {
        listener.onItemClick(it, adapterPosition, list[adapterPosition])
      }
    }
  }

  public inner class RowSalesOrderVoucher1VH(view: View, listener: OnItemClickListener): RecyclerView.ViewHolder(view){
    public val binding: RowSalesOrderVoucher1Binding = RowSalesOrderVoucher1Binding.bind(itemView)
    init {
        binding.constraintsalesorderVoucher.setOnClickListener {
          listener.onItemClick(it, adapterPosition, list[adapterPosition])
        }
    }
  }
}
