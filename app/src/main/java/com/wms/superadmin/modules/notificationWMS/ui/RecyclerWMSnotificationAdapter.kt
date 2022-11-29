package com.wms.superadmin.modules.notificationWMS.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.modules.mobilenotification.data.model.MobileNotificationRowModel
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowNotification4Binding
import java.lang.Exception
import kotlin.Int
import kotlin.Unit
import kotlin.collections.List

public class RecyclerWMSnotificationAdapter(public var mlist: List<MobileNotificationRowModel>) : RecyclerView.Adapter<RecyclerWMSnotificationAdapter.RowNotification4VH>(), Filterable {
  private var clickListener: OnItemClickListener? = null

  var ItemList = arrayListOf<MobileNotificationRowModel>()
  var list  = arrayListOf<MobileNotificationRowModel>()
  init {
    ItemList = mlist as ArrayList<MobileNotificationRowModel>
    list = mlist as ArrayList<MobileNotificationRowModel>
  }

  public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowNotification4VH {
    val view=LayoutInflater.from(parent.context).inflate(R.layout.row_notification4,parent,false)
    return RowNotification4VH(view,clickListener!!)
  }

  public override fun onBindViewHolder(holder: RowNotification4VH, position: Int): Unit {
    val notification4RowModel = list[position]
    holder.binding.notification4RowModel = notification4RowModel
  }

  public override fun getItemCount(): Int = mlist.size

  public fun updateData(newData: List<MobileNotificationRowModel>): Unit {
    mlist = newData
    if (ItemList!!.size > 0) {
      ItemList!!.clear()
      notifyItemRangeRemoved(0, ItemList!!.size);
    }
    if (list!!.size > 0) {
      list!!.clear()
      notifyItemRangeRemoved(0, list!!.size);
    }
    list = newData as ArrayList<MobileNotificationRowModel>
    ItemList = newData

    notifyDataSetChanged()
  }

  override fun getFilter(): android.widget.Filter {
    return object : android.widget.Filter() {
      override fun performFiltering(constraint: CharSequence?): FilterResults {
        val charSearch = constraint.toString()
        if (charSearch.isEmpty()) {
          ItemList = list as ArrayList<MobileNotificationRowModel>
        } else {
          val resultList = ArrayList<MobileNotificationRowModel>()

          for (row in list!!) {
            if (row.txtSOnum?.contains(charSearch, true)!!
              || row.txt1122021?.contains(charSearch, true)!!
              || row.date?.contains(charSearch, true)!!
            ) {
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
          ItemList = results?.values as ArrayList<MobileNotificationRowModel>

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
    public fun onItemClick(view: View, position: Int, item: MobileNotificationRowModel): Unit {
    }
  }

  public inner class RowNotification4VH(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view) {
    public val binding: RowNotification4Binding = RowNotification4Binding.bind(itemView)
    init {
      binding.constraintGroupNotification.setOnClickListener {
        listener.onItemClick(it, adapterPosition, list[adapterPosition])
      }
    }
  }
}
