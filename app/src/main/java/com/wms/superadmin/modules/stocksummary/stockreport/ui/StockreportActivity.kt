package com.wms.superadmin.modules.stocksummary.stockreport.ui

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.databinding.ActivityStockreportBinding
import com.wms.superadmin.extensions.loadFragment
import com.wms.superadmin.modules.stocksummary.sritem.ui.SrWarehouseFragment
import com.wms.superadmin.modules.stocksummary.sritem.ui.SrbranchFragment
import com.wms.superadmin.modules.stocksummary.stockreport.data.viewmodel.StockreportVM
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.pojos.BranchListItem
import com.wms.superadmin.network.models.pojos.CatSubCatItem
import com.wms.superadmin.network.models.pojos.WarehouseItem
import com.wms.superadmin.network.models.stocksummary.StockReportRequest
import org.koin.android.ext.android.inject
import java.util.*


class StockreportActivity : BaseActivity<ActivityStockreportBinding>(R.layout.activity_stockreport), AdapterView.OnItemSelectedListener
    {
         var mItemCode: String?=""
        private val viewModel: StockreportVM by viewModels<StockreportVM>()
      private var mBranchList= arrayListOf<BranchListItem>()
        public var mListener: OnActivityListener?= null
      private var mWarehouseList= arrayListOf<WarehouseItem>()
      private var mCatList= arrayListOf<CatSubCatItem>()
      private var mSubCatList= arrayListOf<CatSubCatItem>()
        private val prefs: PreferenceHelper by inject()
        private var allWarehouseList=prefs.getWarehouseList<ArrayList<WarehouseItem>>()
        private var allCatList=prefs.getCatList<ArrayList<CatSubCatItem>>()
        private var mUser=prefs.getSADetails<LoginResponse>()
        var mBranchId="0"
        private var mOrgId=mUser?.orgID?:"0"
         var mFromDate="";
         var mToDate:String=""
       var mWarehouseId="0"
       var mCatId="0"
       var mSubCatId="0"
        var mInwardRate="0"
        private var isAdmin=mUser?.branchID!=null

  override fun onInitialized(): Unit {
    viewModel.navArguments = intent.extras?.getBundle("bundle")
      initializeDates()
    binding.spinnerWarehouse.isEnabled=false
    binding.spinnerGroup.isEnabled=false
    binding.spinnerSubcat.isEnabled=false

    binding.stockreportVM = viewModel
    setUpSearchViewSearchBArListener()
    val destFragment = SrbranchFragment.getInstance(null,this)
    this.loadFragment(
        R.id.fragmentContainer,
        destFragment,
        bundle = destFragment.arguments,
        tag = SrbranchFragment.TAG,
        addToBackStack = false,
        add = false,
        enter = null,
        exit = null,
    )
  }


  override fun setUpClicks(): Unit {
      initSpinner(true)
      binding.backButton.setOnClickListener {
          this.mListener?.onActionChange(true)
      }
      binding.txtfromdate.setOnClickListener {
          openCalender(true)
      }
      binding.txtTodate.setOnClickListener {
          openCalender(false)
      }
      binding.checkBoxMonthwise.setOnClickListener {
          sendListerner()
      }
  }


        public fun setScreenAction(Branch:Boolean,Warehouse:Boolean,Group:Boolean,SubCat:Boolean,MonthWise:Boolean){
            initSpinner(Branch)
            initWareHouseSpinner(mBranchId,Warehouse)
            initCatSpinner(Group)
            initSubcatSpinner(mCatId.toInt()?:0,SubCat)
            binding.checkBoxMonthwise.visibility=if(MonthWise) View.VISIBLE else View.GONE
        }
        public fun openCalender(isFromDate:Boolean){
            val mcurrentTime = Calendar.getInstance()
            val year = mcurrentTime.get(Calendar.YEAR)
            val month = mcurrentTime.get(Calendar.MONTH)
            val day = mcurrentTime.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(this@StockreportActivity, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    if(isFromDate){
                        binding.txtfromdate.text= String.format("%02d-%02d-%d", dayOfMonth, month+1, year)
                        mFromDate=String.format("%d-%02d-%02d", year,month+1,dayOfMonth)
                    }else{
                        binding.txtTodate.text= String.format("%02d-%02d-%d", dayOfMonth, month+1, year)
                        mToDate=String.format("%d-%02d-%02d", year,month+1,dayOfMonth)
                    }
                    sendListerner()
                }
            }, year, month, day);

            datePicker.show()

        }

        private  fun initializeDates(){
            val mcurrentTime = Calendar.getInstance()
            val year = mcurrentTime.get(Calendar.YEAR)
            val month = mcurrentTime.get(Calendar.MONTH)
            val day = mcurrentTime.get(Calendar.DAY_OF_MONTH)
            if(month>=3)
            {
                mFromDate=String.format("%d-%02d-%02d", year,4,1)
                mToDate=String.format("%d-%02d-%02d", year+1, 3,31 )

            }else{
                mFromDate=String.format("%d-%02d-%02d", year-1, 4,1)
                mToDate=String.format("%d-%02d-%02d", year, 3, 31)

            }
            binding.txtfromdate.text=mFromDate
            binding.txtTodate.text=mToDate

        }
        private fun initSpinner(isEnabled: Boolean){
            BranchList=prefs.getBranchlist<ArrayList<BranchListItem>>()!!
            mBranchList=BranchList
            if(isAdmin)mBranchId=mUser?.branchID?:"0" else mBranchId
            val branchItem=BranchList.find { it.branchID==mBranchId }
            val position=BranchList.indexOf(branchItem)?:0
            val branchlistAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, BranchList.map { it.branchName })
            branchlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerBranch.adapter=branchlistAdapter
            binding.spinnerBranch.setSelection(position)
            binding.spinnerBranch.isEnabled=(!isAdmin && isEnabled)
            binding.spinnerBranch.onItemSelectedListener=this
        }
     private fun initWareHouseSpinner(BranchId:String,isEnabled:Boolean){
         Log.e("warehouselist","branch:$BranchId.($isEnabled)...$allWarehouseList")
         mWarehouseList.clear()
         allWarehouseList?.forEach {
             if(it.branchID==BranchId)
                 mWarehouseList.add(it)
         }
         mWarehouseList.add(0, WarehouseItem(orgID = "0",warehouseID = 0,branchID = BranchId,warehouseName = "All"))
         val spinnerWarehouseAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,mWarehouseList.map { it.warehouseName })
         spinnerWarehouseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
         binding.spinnerWarehouse.adapter = spinnerWarehouseAdapter
         val warehouseItem=mWarehouseList.find { it.warehouseID==mWarehouseId.toIntOrNull()?:0 }
         val position=mWarehouseList.indexOf(warehouseItem)
         binding.spinnerWarehouse.setSelection(position)
         binding.spinnerWarehouse.isEnabled=isEnabled
         binding.spinnerWarehouse.onItemSelectedListener=this

      }
        private fun initCatSpinner(isEnabled: Boolean){
            mCatList.clear()
            allCatList?.forEach {
             allCatList?.distinctBy { it.categoryID }?.map {
                     mCatList.add(it)
             }
            }
            val spinnerGroupAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,mCatList.map { it.categoryName })
            spinnerGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerGroup.adapter = spinnerGroupAdapter
            val catItem=mCatList.find { it.categoryID==mCatId.toIntOrNull()?:0 }
            val position=mCatList.indexOf(catItem)
            binding.spinnerGroup.setSelection(position)
            binding.spinnerGroup.isEnabled=isEnabled
            binding.spinnerGroup.onItemSelectedListener=this


        }
        private fun initSubcatSpinner(Catid:Int,isEnabled: Boolean){
            mSubCatList.clear()
            allCatList?.forEach {
                if(it.categoryID==Catid)
                   mSubCatList.add(it)
            }
            mSubCatList.add(0, CatSubCatItem(orgID = mOrgId,branchID = mBranchId,categoryID = Catid,categoryName = "All",subCategoryID = 0,subCategoryName = "All"))
            val spinnerSubCategoryAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,mSubCatList.map { it.subCategoryName })
            spinnerSubCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerSubcat.adapter = spinnerSubCategoryAdapter
            Log.e("SubCat","SubCateId:$mSubCatId..$mSubCatList")
            val subcatItem=mSubCatList.find { it.subCategoryID==mSubCatId.toIntOrNull()?:0 }
            val position=mSubCatList.indexOf(subcatItem)
            binding.spinnerSubcat.setSelection(position)
            binding.spinnerSubcat.isEnabled=isEnabled
            binding.spinnerSubcat.onItemSelectedListener=this

        }

  private fun setUpSearchViewSearchBArListener(): Unit {
    binding.searchViewSearchBAr.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(p0 : String) : Boolean {
        // Performs search when user hit
        // the search button on the keyboard
        return false
      }
      override fun onQueryTextChange(text : String) : Boolean {
        this@StockreportActivity.mListener?.onSearchTextChange<String>(text)
        return false
      }
      })
    }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
           // try {
            when(parent?.id?:0){

                binding.spinnerBranch.id->{
                 mBranchId=mBranchList[position].branchID.toString()
                    sendListerner()
                }
                binding.spinnerWarehouse.id->{
                    mWarehouseId=mWarehouseList[position].warehouseID.toString()
                    sendListerner()
                }
                binding.spinnerGroup.id->{
                    mCatId=mCatList[position].categoryID.toString()
                    sendListerner()
                }
                binding.spinnerSubcat.id->{
                    mSubCatId=mSubCatList[position].subCategoryID.toString()
                    sendListerner()
                }

            }
            /*}catch (exception:IllegalStateException){
                Log.e(TAG,exception.toString())
            }finally {
                Log.e(TAG,"Fragment Review")
            }*/

        }
        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }

        fun sendListerner(){

            var request=StockReportRequest(fromDate = mFromDate,toDate = mToDate,orgID = mOrgId,branchID = mBranchId,
                inwardrate = mInwardRate,warehouseID =mWarehouseId,groupID = mCatId,itemCode = mItemCode,ismonthcheckbox = binding.checkBoxMonthwise.isChecked,subCategoryId = mSubCatId,screenName = "")
            this@StockreportActivity.mListener!!.onActionChange(request)
        }
    companion object {
      const val TAG: String = "STOCK_REPORT_ACTIVITY"
        public fun getIntent(context: Context, bundle: Bundle?): Intent {
            val destIntent = Intent(context, StockreportActivity::class.java)
            destIntent.putExtra("bundle", bundle)
            return destIntent
        }

    }



        interface OnActivityListener {
            fun <T> onActionChange(`object`: T)
            fun <T> onSearchTextChange(searchtext: String)
        }
        override fun onAttachFragment(fragment: Fragment) {
            super.onAttachFragment(fragment)
            try {
                mListener = fragment as OnActivityListener
            } catch (e: ClassCastException) {
                Log.e(TAG,"$fragment must implement OnActivityListener")
                throw ClassCastException("$fragment must implement OnActivityListener")
            }
        }





  }
