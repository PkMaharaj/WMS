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
import com.wms.superadmin.databinding.RowTraxbrachrecyclerBinding
import com.wms.superadmin.modules.transaction.data.model.BranchTXNRowModel
import java.lang.Exception
import kotlin.Int
import kotlin.collections.List

class BranchTXNAdapter(var mlist: List<BranchTXNRowModel>,var activity:BranchTXNActivity) : RecyclerView.Adapter<BranchTXNAdapter.RowBranchrecyclerVH>(),Filterable {
    private var clickListener: OnItemClickListener? = null
    private var mIsAgeing=false

    var ItemList = arrayListOf<BranchTXNRowModel>()
    var list  = arrayListOf<BranchTXNRowModel>()
    init {
        ItemList = mlist as ArrayList<BranchTXNRowModel>
        list = mlist as ArrayList<BranchTXNRowModel>
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowBranchrecyclerVH {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.row_traxbrachrecycler,parent,false)
        return RowBranchrecyclerVH(view,clickListener!!)
    }

    override fun onBindViewHolder(holder: RowBranchrecyclerVH, position: Int) {
        val branchrecyclerRowModel = ItemList[position]
        holder.binding.branchrecyclerRowModel = branchrecyclerRowModel
        holder.binding.agingLayout.visibility=if(mIsAgeing)View.VISIBLE else View.GONE

    }
    fun showAgingDetails(isAging:Boolean){
        mIsAgeing=isAging
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = ItemList.size

    public fun updateData(newData: ArrayList<BranchTXNRowModel>) {
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
                    ItemList = list as ArrayList<BranchTXNRowModel>
                } else{
                    val resultList = ArrayList<BranchTXNRowModel>()

                    for (row in list!!) {
                        if (row.PartyName?.contains(charSearch, true)!!
                            ||( row.ReferenceNo?.contains(charSearch, true))!!
                            ||( row.BillDate?.contains(charSearch, true))!!) {
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
                    ItemList = results?.values as ArrayList<BranchTXNRowModel>
                    notifyDataSetChanged()
                    activity.calCulateSummary()
                } catch (Ex: Exception) {
                }
            }
        }
    }
    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        this.clickListener = clickListener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, item: BranchTXNRowModel) {
        }
    }

    inner class RowBranchrecyclerVH(view: View,listener:OnItemClickListener) : RecyclerView.ViewHolder(view) {
        val binding: RowTraxbrachrecyclerBinding = RowTraxbrachrecyclerBinding.bind(itemView)
        init {
            binding.constraintGroup57.setOnClickListener {
                listener.onItemClick(itemView,adapterPosition,ItemList[adapterPosition])
            }
        }
    }
}
