package com.wms.superadmin.modules.dashboardsuperadmin.ui
//import com.wms.app.modules.cashinhand1.ui.CashInHand1Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.databinding.ActivityDashboardSuperadminBinding
import com.wms.superadmin.extensions.*
import com.wms.superadmin.modules.attedence.ui.AttedenceActivity
import com.wms.superadmin.modules.bankbook.ui.BankbookActivity
import com.wms.superadmin.modules.dashboardsuperadmin.data.model.DashboardSuperadmin1RowModel
import com.wms.superadmin.modules.dashboardsuperadmin.data.model.DashboardSuperadmin2RowModel
import com.wms.superadmin.modules.dashboardsuperadmin.data.viewmodel.DashboardSuperadminVM
import com.wms.superadmin.modules.stocksummary.stockreport.ui.StockreportActivity
//import com.wms.superadmin.modules.stocksummary.stockreport.ui.StockreportActivity
import com.wms.superadmin.modules.transaction.ui.BranchTXNActivity
import com.wms.superadmin.modules.transaction.ui.TransactionActivity
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.createallbranchreceivables.AgingRequestItem
import com.wms.superadmin.network.models.createallbranchreceivables.TransactionRequest
import com.wms.superadmin.network.models.pojos.BranchListItem
import com.wms.superadmin.network.models.pojos.CatSubCatItem
import com.wms.superadmin.network.models.pojos.WarehouseItem
//import com.wms.superadmin.network.models.pojos.CatSubCatItem
//import com.wms.superadmin.network.models.pojos.WarehouseItem
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


public class DashboardSuperadminActivity : BaseActivity<ActivityDashboardSuperadminBinding>(R.layout.activity_dashboard_superadmin),
    AdapterView.OnItemSelectedListener {
    private var specificbranch: BranchListItem? = null
    private var logged_branchid: String?=null
    private val viewModel: DashboardSuperadminVM by viewModels<DashboardSuperadminVM>()
    private val prefs: PreferenceHelper by inject()
    private var mMainBranchList= arrayListOf<BranchListItem>()
    val mCurrentTime = Calendar.getInstance()
    val mYear = mCurrentTime.get(Calendar.YEAR)
    val mMonth = mCurrentTime.get(Calendar.MONTH)+1
    val mDay = mCurrentTime.get(Calendar.DAY_OF_WEEK)-1
    var SuperAdminDetails=prefs.getSADetails<LoginResponse>()
    var mBranchId="0"
    var mPeriodPosition=0
    var mToDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    var mFromDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    var muser=prefs.getSADetails<LoginResponse>()!!
    var spinnerBranchList= arrayListOf<BranchListItem>()
    private var isAdmin=muser?.branchID!=null
    var mCurrentDate= SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

    public override fun onInitialized(): Unit {

        logged_branchid = muser.branchID.toString()
        Log.e("loggedbranchid ",logged_branchid!!)

        binding.username.text = muser.username

        viewModel.onClickOnCreate()
        Log.e("Admindetails","$SuperAdminDetails")


        val timePeriodAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            getTimePeriodList().map {
                it.PeriodName
            })
        timePeriodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.periodSpinner.adapter=timePeriodAdapter
        binding.dashboardSuperadminVM = viewModel
    }

  public override fun setUpClicks(): Unit {
      binding.cardViewSalesCard.setBackgroundResource(R.drawable.sales_grad_back)
      binding.cardViewPurchaseCard.setBackgroundResource(R.drawable.purchase_grad_back)
      binding.cardViewReceivableCard.setBackgroundResource(R.drawable.recevables_grad_back)
      binding.cardViewPayablesCard.setBackgroundResource(R.drawable.paybles_grad_back)
      binding.cardViewCashCard.setBackgroundResource(R.drawable.cash_grad_back)
      binding.cardViewBankCard.setBackgroundResource(R.drawable.bank_grad_back)
      binding.cardViewStockCard.setBackgroundResource(R.drawable.stock_grad_back)
      binding.cardViewNotificationCard.setBackgroundResource(R.drawable.note_grad_back)


      binding.imageAlignjustify.setOnClickListener {
          toggleDrawer()
      }

      binding.included.Logout.setOnClickListener {
          LogoutDialog()
      }
      binding.included.Attendence.setOnClickListener {
          toggleDrawer()
          var destIntent = AttedenceActivity.getIntent(this, null)
          startActivity(destIntent)
      }
      binding.Signout.setOnClickListener {
         LogoutDialog()
      }

      binding.cardViewSalesCard.setOnClickListener {
          if(SuperAdminDetails?.screenSettings?.sales_order!!||SuperAdminDetails?.screenSettings?.sales_register!!)
          SalesDialog(this,true,mFromDate,mToDate,SuperAdminDetails!!)
          else
              customAlert("Alert..!", MyApp.getInstance().resources.getString(R.string.notAccess_msg),false)
      }

      binding.cardViewPurchaseCard.setOnClickListener {
          if(SuperAdminDetails?.screenSettings?.purchase_order!!||SuperAdminDetails?.screenSettings?.purchase_register!!)
          SalesDialog(this, false,mFromDate,mToDate,SuperAdminDetails!!)
          else
              customAlert("Alert..!", MyApp.getInstance().resources.getString(R.string.notAccess_msg),false)
      }

      binding.cardViewBankCard.setOnClickListener {
          if(SuperAdminDetails?.screenSettings?.bankBook == true) {
              val intent = BankbookActivity.getIntent(this, null)
              intent.putExtra(IntentParams.BRANCH_ID, mBranchId)
              intent.putExtra(IntentParams.PERIOD, mPeriodPosition)
              prefs.setBookType("BankBook")
              startActivity(intent)
          }else
              customAlert("Alert..!", MyApp.getInstance().resources.getString(R.string.notAccess_msg),false)
      }
      binding.cardViewCashCard.setOnClickListener {
          if(SuperAdminDetails?.screenSettings?.caskBook == true) {
              val intent = BankbookActivity.getIntent(this, null)
              intent.putExtra(IntentParams.BRANCH_ID, mBranchId)
              intent.putExtra(IntentParams.PERIOD, mPeriodPosition)
              prefs.setBookType("CashBook")
              startActivity(intent)
          }else
              customAlert("Alert..!", MyApp.getInstance().resources.getString(R.string.notAccess_msg),false)
      }
      binding.cardViewPayablesCard.setOnClickListener {
          if(SuperAdminDetails?.screenSettings?.payable == true) {
              var bundle = Bundle()
              if (isAdmin) {
                  val Request = TransactionRequest(
                      orgID = muser.orgID?.toIntOrNull() ?: 0,
                      AgingRequestItem(),
                      ScreenNames.TXN_VOUCHER,
                      mFromDate,
                      mToDate,
                      muser.branchID ?: "0",
                      ReportTypes.PAYBLES
                  )
                  bundle.putSerializable(IntentParams.TRANSACTION_REQUEST, Request)
                  val intent = BranchTXNActivity.getIntent(this, bundle)


                  startActivity(intent)
              } else {
                  bundle.putString(IntentParams.REPORT_TYPE, ReportTypes.PAYBLES)
                  bundle.putString(IntentParams.FROM_DATE, mFromDate)
                  bundle.putString(IntentParams.To_DATE, mToDate)
                  val intent = TransactionActivity.getIntent(this, bundle)

                  startActivity(intent)
              }
          }
          else
              customAlert("Alert..!", MyApp.getInstance().resources.getString(R.string.notAccess_msg),false)
      }
      binding.cardViewReceivableCard.setOnClickListener {
          if(SuperAdminDetails?.screenSettings?.receivable == true) {
          var bundle = Bundle()
          if (isAdmin) {
              val Request = TransactionRequest(
                  orgID = muser.orgID?.toIntOrNull() ?: 0,
                  AgingRequestItem(),
                  ScreenNames.TXN_VOUCHER,
                  mFromDate,
                  mToDate,
                  muser.branchID ?: "0",
                  ReportTypes.RECEIVABLES
              )
              bundle.putSerializable(IntentParams.TRANSACTION_REQUEST, Request)
              val intent = BranchTXNActivity.getIntent(this, bundle)
              startActivity(intent)
          } else {
              bundle.putString(IntentParams.REPORT_TYPE, ReportTypes.RECEIVABLES)
              val intent = TransactionActivity.getIntent(this, bundle)
              bundle.putString(IntentParams.FROM_DATE, mFromDate)
              bundle.putString(IntentParams.To_DATE, mToDate)
              startActivity(intent)
          }
      }else
      customAlert("Alert..!", MyApp.getInstance().resources.getString(R.string.notAccess_msg),false)
      }

      binding.constraintGroup371.setOnClickListener {
          if(SuperAdminDetails?.screenSettings?.notification == true) {

              if (muser.branchID.isNullOrEmpty()) {
              binding.cardViewNotificationCard.isEnabled=false
              Toast.makeText(this,"Notifications are available only for branch admin",Toast.LENGTH_SHORT).show()
          }
          else{
              NotificationDialog(this)
          }
          }else
              customAlert("Alert..!", MyApp.getInstance().resources.getString(R.string.notAccess_msg),false)
      }
      binding.cardViewStockCard.setOnClickListener {
          if(SuperAdminDetails?.screenSettings?.stocks == true) {
              val intent = StockreportActivity.getIntent(this, null)
              startActivity(intent)
          }else
              customAlert("Alert..!", MyApp.getInstance().resources.getString(R.string.notAccess_msg),false)
      }
      binding.refresh.setOnClickListener {

          viewModel.getDashBoardDetails(mBranchId,mFromDate,mToDate)
      }
  }

    private fun LogoutDialog() {
        val builder = AlertDialog.Builder(this)
        val itemdetail_heading = "<b>" + "ALERT" + "</b> "
        builder.setTitle(Html.fromHtml(itemdetail_heading))
        builder.setMessage("Do you want to exit?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            super.onBackPressed();
        }
        builder.setNegativeButton(android.R.string.no){ dialog, which -> }

        builder.show()
    }

    private fun toggleDrawer(): Unit {
    if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        binding.drawerLayout.openDrawer(GravityCompat.START)
                    }
                    else {
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }
  }

  public fun onClickRecyclerDashboardSuperadminlist(view: View, position: Int, item: DashboardSuperadmin1RowModel): Unit {
    when(view.id) {
    }
  }

  public fun onClickRecyclerGroup60(view: View, position: Int, item: DashboardSuperadmin2RowModel): Unit {
    when(view.id) {
            R.id.imageGroup319 -> {
//            val destIntent = CashInHand1Activity.getIntent(this, null)
//            startActivity(destIntent)
//            this.overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
                        }
    }
  }


    public override fun addObservers(): Unit {
        var progressDialog : AlertDialog? = null
        viewModel.progressLiveData.observe(this@DashboardSuperadminActivity) {
            if(it) {
                progressDialog?.dismiss()
                progressDialog = null
                progressDialog = this@DashboardSuperadminActivity.showProgressDialog()
            } else  {
                progressDialog?.dismiss()
            }
        }
        viewModel.createBranchListLiveData.observe(this@DashboardSuperadminActivity) {
            if(it is SuccessResponse) {
                val response = it.getContentIfNotHandled()
                val branchlist= arrayListOf<BranchListItem>()
               val warehouselist= arrayListOf<WarehouseItem>()
               val catSubCatlist= arrayListOf<CatSubCatItem>()
                branchlist.add(BranchListItem(SuperAdminDetails!!.orgID,"0","All"))
               warehouselist.add(WarehouseItem(orgID = SuperAdminDetails!!.orgID,0,branchID = "0","All"))
               catSubCatlist.add(CatSubCatItem(orgID = SuperAdminDetails?.orgID,branchID = "0",categoryName = "All",categoryID = 0,subCategoryName = "All",subCategoryID = 0))
                branchlist.addAll(it.data.branchList as ArrayList<BranchListItem>)
                warehouselist.addAll(it.data.warehouselist as ArrayList<WarehouseItem>)
                catSubCatlist.addAll(it.data.catSubcatList as ArrayList<CatSubCatItem>)
                prefs.setBranchlist(branchlist)
                prefs.setWarehouseList(warehouselist)
                prefs.setCatList(catSubCatlist)
                BranchList=branchlist

                if (isAdmin) {
                    specificbranch = BranchList.find { it.branchID==logged_branchid}
                    if(specificbranch!=null)
                    spinnerBranchList.add(specificbranch!!)
                }
                else
                    spinnerBranchList=BranchList
                val branchlistAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerBranchList.map { it.branchName })
                branchlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.branchlistspinner.adapter=branchlistAdapter
                binding.branchlistspinner.onItemSelectedListener=this
                binding.periodSpinner.onItemSelectedListener=this
            } else if(it is ErrorResponse)  {
                onError(it.data ?:Exception())
            }
        }
        viewModel.dashBoardDetailsLiveData.observe(this@DashboardSuperadminActivity){
            if(it is SuccessResponse){
                val response = it.getContentIfNotHandled()
                viewModel.bindDashBoardDetails(response!!)
            }
            else if(it is ErrorResponse){
                onError(it.data ?:Exception())
            }

        }
    }


    private fun onError(exception: Exception): Unit {
        when(exception) {
            is NoInternetConnection -> {
                Snackbar.make(binding.root, exception.message?:"Network error", Snackbar.LENGTH_LONG).show()
            }
        }
    }

  public companion object {
    public const val TAG: String = "DASHBOARD_SUPERADMIN_ACTIVITY"

    public fun getIntent(context: Context, bundle: Bundle?): Intent {
      val destIntent = Intent(context, DashboardSuperadminActivity::class.java)
      destIntent.putExtra("bundle", bundle)
      return destIntent
    }
  }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, p3: Long)
    {
        if(parent!!.id==binding.branchlistspinner.id) {
            mBranchId=spinnerBranchList[position].branchID?:"0"
            viewModel.getDashBoardDetails(mBranchId,mFromDate,mToDate)
        }
        if(parent!!.id==binding.periodSpinner.id) {
            mPeriodPosition=position
            when(position){
                0->{
                    mToDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                    mFromDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                    Log.e("dates","From:..$mFromDate....To:..$mToDate")
                    viewModel.getDashBoardDetails(mBranchId,mFromDate,mToDate)


                }
                1->{
                    mToDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                    var FromDate = Date(Date().getTime() - ( mDay* 86400000))
                    mFromDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(FromDate)
                    viewModel.getDashBoardDetails(mBranchId,mFromDate,mToDate)


                }
                2->{
                    mToDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                    mFromDate=String.format("%02d-%02d-%d",1,mMonth,mYear)
                    viewModel.getDashBoardDetails(mBranchId,mFromDate,mToDate)

                }
                3->{
                    mToDate=SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                    if(mMonth>=4){
                        mFromDate=String.format("%02d-%02d-%d",1,4,mYear)
                    } else{
                        mFromDate=String.format("%02d-%02d-%d",1,4,mYear-1)
                        mToDate=String.format("%02d-%02d-%d",31,3,mYear)
                    }
                    viewModel.getDashBoardDetails(mBranchId,mFromDate,mToDate)

                }
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        //TODO("Not yet implemented")
  }

    public override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        val itemdetail_heading = "<b>" + "ALERT" + "</b> "
        builder.setTitle(Html.fromHtml(itemdetail_heading))
        builder.setMessage("Do you want to exit?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            super.onBackPressed();
        }
        builder.setNegativeButton(android.R.string.no){ dialog, which -> }

        builder.show()
    }
}
