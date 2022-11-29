import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.wms.superadmin.R
import com.wms.superadmin.databinding.RowStockreportItemBinding
import com.wms.superadmin.databinding.RowStockreportViewBinding
import com.wms.superadmin.databinding.RowStockreportVoucherBinding
import com.wms.superadmin.extensions.ScreenNames
import com.wms.superadmin.modules.stocksummary.sritem.data.model.StockReportRowModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


public class StockReportAdapter(public var mlist: List<StockReportRowModel>, var ScreenName:String) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable {
  private var clickListener: OnItemClickListener? = null
  private var mIsMonthWise:Boolean=false
  var ItemList = arrayListOf<StockReportRowModel>()
  var list  = arrayListOf<StockReportRowModel>()
  init {
    ItemList = mlist as ArrayList<StockReportRowModel>
    list = mlist as ArrayList<StockReportRowModel>
  }

  public override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
    if(ScreenName== ScreenNames.SR_VOUCHERWISE) {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.row_stockreport_voucher, parent, false)
      return RowVoucherVH(view,clickListener!!)
    }else if(ScreenName== ScreenNames.SR_ITEMWISE){
      val view = LayoutInflater.from(parent.context).inflate(R.layout.row_stockreport_item, parent, false)
      return RowItemVH(view,clickListener!!)
    }
    else
    {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.row_stockreport_view, parent, false)
      return RowSRMainVH(view,clickListener!!)
    }

  }

  public override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int): Unit {
    val StockReportRowModel = ItemList[position]

    if(holder is RowVoucherVH) {
      holder.binding.stockReportRowModel = StockReportRowModel
      holder.binding.srHeader.text=StockReportRowModel.PartyName
    }
    else if(holder is RowItemVH) {
      holder.binding.stockReportRowModel = StockReportRowModel
      holder.binding.srHeader.text=StockReportRowModel.ItemName
    }else if(holder is RowSRMainVH) {
      Log.e("MonthTest","$mIsMonthWise : $ScreenName..$StockReportRowModel")
      holder.binding.stockReportRowModel = StockReportRowModel
      var headerName = "monthname1111"
      if (!mIsMonthWise) {
        when (ScreenName) {
          ScreenNames.SR_BRANCHWISE ->
            headerName = StockReportRowModel.BranchName!!
          ScreenNames.SR_WAREHOUSEWISE ->
            headerName = StockReportRowModel.WarehouseName!!
          ScreenNames.SR_GROUPWISE ->
            headerName = StockReportRowModel.GroupName!!
          ScreenNames.SR_SUBCATWISE ->
            headerName = StockReportRowModel.SubCategoryName!!
          ScreenNames.SR_MONTHWISE ->
            headerName = StockReportRowModel.MonthName!!
        }
        holder.binding.srHeader.text = headerName
      } else
        holder.binding.srHeader.text = StockReportRowModel.MonthName?:"Monthname"
    }
  }

  public override fun getItemCount(): Int = ItemList.size

  public fun updateData(newData: ArrayList<StockReportRowModel>): Unit {
    if (ItemList.size > 0) {
      ItemList.clear()
      notifyItemRangeRemoved(0, ItemList.size);
    }
    if (list.size > 0) {
      list.clear()
      notifyItemRangeRemoved(0, list.size);
    }
    list = newData
    ItemList = newData
    notifyDataSetChanged()
  }
  override fun getFilter(): Filter {
    return object : Filter() {
      override fun performFiltering(constraint: CharSequence?): FilterResults {
        val charSearch = constraint.toString()
        if (charSearch.isEmpty()) {
          ItemList = list as ArrayList<StockReportRowModel>
        } else{
          val resultList = ArrayList<StockReportRowModel>()

          for (row in list!!) {
            if (row.BranchName?.contains(charSearch, true)!!
              ||row.WarehouseName?.contains(charSearch, true)!!
              ||row.MonthName?.contains(charSearch, true)!!
              ||row.ItemName?.contains(charSearch, true)!!
              ||row.WarehouseName?.contains(charSearch, true)!!
              ||row.GroupName?.contains(charSearch, true)!!
              ||row.SubCategoryName?.contains(charSearch, true)!!
              ||row.VoucherType?.contains(charSearch, true)!!
              ||row.VoucherNo?.contains(charSearch, true)!!) {
              resultList.add(row)
            }
          }
          ItemList = resultList
        }
        val filterResults = FilterResults()
        filterResults.values = ItemList
        return filterResults
      }

      override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        try {
          ItemList = results?.values as ArrayList<StockReportRowModel>

          notifyDataSetChanged()
        } catch (Ex: Exception) {
        }
      }
    }
  }

  fun updateMonthwise(isMonthWise:Boolean){
    mIsMonthWise=isMonthWise
    notifyDataSetChanged()
  }

  public fun setOnItemClickListener(clickListener: OnItemClickListener): Unit {
    this.clickListener = clickListener
  }

  public interface OnItemClickListener {
    public fun onItemClick(view: View, position: Int, item: StockReportRowModel): Unit {

    }
  }

  public inner class RowSRMainVH(view: View, listener:OnItemClickListener) : RecyclerView.ViewHolder(view) {
    public val binding:RowStockreportViewBinding  = RowStockreportViewBinding.bind(itemView)
    init {
      binding.rowLayout.setOnClickListener {
        if(!mIsMonthWise)
        listener.onItemClick(it,adapterPosition,ItemList[adapterPosition])
      }

    }
  }
  public inner class RowItemVH(view: View,listener:OnItemClickListener) : RecyclerView.ViewHolder(view) {
    public val binding: RowStockreportItemBinding = RowStockreportItemBinding.bind(itemView)
    init {
      binding.rowLayout.setOnClickListener {
        if(!mIsMonthWise)
        listener.onItemClick(it,adapterPosition,ItemList[adapterPosition])
      }

    }
  }

  public inner class RowVoucherVH(view: View,listener:OnItemClickListener) : RecyclerView.ViewHolder(view) {
    public val binding: RowStockreportVoucherBinding = RowStockreportVoucherBinding.bind(itemView)
    init {
      if(!mIsMonthWise)
        binding.rowLayout.setOnClickListener {
          listener.onItemClick(it,adapterPosition,ItemList[adapterPosition])
        }
    }
  }
}


