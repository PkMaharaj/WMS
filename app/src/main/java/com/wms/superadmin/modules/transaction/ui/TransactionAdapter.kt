package com.wms.superadmin.modules.transaction.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowBranchrecyclerBinding
import com.wms.superadmin.modules.transaction.data.model.TransactionRowModel
import java.lang.Exception
import kotlin.Int
import kotlin.collections.List

class TransactionAdapter(var mlist: List<TransactionRowModel>
) : RecyclerView.Adapter<TransactionAdapter.RowBranchrecyclerVH>(),Filterable {
  private var clickListener: OnItemClickListener? = null
  private var mIsAgeing=false

  var ItemList = arrayListOf<TransactionRowModel>()
  var list  = arrayListOf<TransactionRowModel>()
  init {
    ItemList = mlist as ArrayList<TransactionRowModel>
    list = mlist as ArrayList<TransactionRowModel>
  }
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowBranchrecyclerVH {
    val view=LayoutInflater.from(parent.context).inflate(R.layout.row_branchrecycler,parent,false)
    return RowBranchrecyclerVH(view,clickListener!!)
  }

  override fun onBindViewHolder(holder: RowBranchrecyclerVH, position: Int) {
    val branchrecyclerRowModel = ItemList[position]
    holder.binding.branchrecyclerRowModel = branchrecyclerRowModel
    holder.binding.agingLayout.visibility=if(mIsAgeing)View.VISIBLE else View.GONE
  }

  override fun getItemCount(): Int = ItemList.size

  public fun updateData(newData: ArrayList<TransactionRowModel>) {
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
          ItemList = list as ArrayList<TransactionRowModel>
        } else{
          val resultList = ArrayList<TransactionRowModel>()

          for (row in list!!) {
            if (row.BranchName?.contains(charSearch, true)!!) {
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
          ItemList = results?.values as ArrayList<TransactionRowModel>

          notifyDataSetChanged()
        } catch (Ex: Exception) {
        }
      }
    }
  }
  fun setOnItemClickListener(clickListener: OnItemClickListener) {
    this.clickListener = clickListener
  }

  fun showAgingDetails(isAging:Boolean){
    mIsAgeing=isAging
    notifyDataSetChanged()
  }

  interface OnItemClickListener {
    fun onItemClick(view: View, position: Int, item: TransactionRowModel) {
    }
  }

  inner class RowBranchrecyclerVH(view: View,listener:OnItemClickListener) : RecyclerView.ViewHolder(view) {
    val binding: RowBranchrecyclerBinding = RowBranchrecyclerBinding.bind(itemView)
    init {
        binding.mainLayout.setOnClickListener {
          listener.onItemClick(it,adapterPosition,ItemList[adapterPosition])
        }
    }
  }
}
