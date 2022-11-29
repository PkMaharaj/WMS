package com.wms.superadmin.modules.dashboardsuperadmin.`data`.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.network.models.pojos.CreateBranchListRequest
import com.wms.superadmin.network.models.pojos.CreateBranchListResponse
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.modules.dashboardsuperadmin.data.model.*
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.pojos.DashboardResponse
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import kotlin.collections.MutableList
import org.koin.core.inject

public class DashboardSuperadminVM : ViewModel(),KoinComponent {
    public val dashboardSuperadminModel: MutableLiveData<DashboardSuperadminModel> =
        MutableLiveData(DashboardSuperadminModel())

    public var includedModel: MutableLiveData<DrawerItemDrawerMenuModel> =
        MutableLiveData(DrawerItemDrawerMenuModel())

    public val spinnerRectangle159List: MutableLiveData<MutableList<SpinnerRectangle159Model>> =
        MutableLiveData()

    public val spinnerCategory4List: MutableLiveData<MutableList<SpinnerCategory4Model>> =
        MutableLiveData()

    public val spinnerCategory3List: MutableLiveData<MutableList<SpinnerCategory3Model>> =
        MutableLiveData()

    public val recyclerDashboardSuperadminlistList: MutableLiveData<MutableList<DashboardSuperadmin1RowModel>> = MutableLiveData(mutableListOf())

    public val createBranchListLiveData: MutableLiveData<Response<CreateBranchListResponse>> =
        MutableLiveData<Response<CreateBranchListResponse>>()

    public val dashBoardDetailsLiveData: MutableLiveData<Response<DashboardResponse>> = MutableLiveData<Response<DashboardResponse>>()

    public val recyclerGroup60List: MutableLiveData<MutableList<DashboardSuperadmin2RowModel>> =
        MutableLiveData(mutableListOf())
    private val networkRepository: NetworkRepository by inject()
    public val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    private val prefs: PreferenceHelper by inject()
    var SuperAdminDetails=prefs.getSADetails<LoginResponse>()

    public fun onClickOnCreate(): Unit {
        Log.e("SADetails","$SuperAdminDetails")
        viewModelScope.launch {
            progressLiveData.postValue(true)
            createBranchListLiveData.postValue(
                networkRepository.createBranchList(
                    authorization = "bearer " + prefs.getAccess_token(),
                    createBranchListRequest = CreateBranchListRequest(SuperAdminDetails!!.orgID)
                )
            )
        }
    }

    public fun getDashBoardDetails(branchId:String,FromDate:String,ToDate:String): Unit {
        viewModelScope.launch {
            progressLiveData.postValue(true)
            dashBoardDetailsLiveData.postValue(
                networkRepository.getDasgBoardDetails(
                    authorization = "bearer "+prefs.getAccess_token(),
                    userId = SuperAdminDetails?.userID?.toString()?:"0",
                    branchid = branchId,
                    orgid = SuperAdminDetails?.orgID?:"0",
                    fromdare = FromDate,todate = ToDate
                )
            )
            progressLiveData.postValue(false)
        }
    }

    fun bindDashBoardDetails(response:DashboardResponse){
        val dashboardModelValue=dashboardSuperadminModel.value?:DashboardSuperadminModel()
        val drawermodelvalue = includedModel.value ?:DrawerItemDrawerMenuModel()

        dashboardModelValue.Sales=response.sales?:"00"
        dashboardModelValue.Purchase=response.purchase?:"00"
        dashboardModelValue.Payables=response.payable?:"00"
        dashboardModelValue.Receivables=response.receivable?:"00"
        dashboardModelValue.CashBook=response.cashBook?:"00"
        dashboardModelValue.BankBook=response.bankBook?:"00"
        dashboardModelValue.Stock=response.stock?:"00"
        drawermodelvalue.UserName=SuperAdminDetails?.username?:""
        drawermodelvalue.RoleName=SuperAdminDetails?.roleName?:""
        drawermodelvalue.Fermname=SuperAdminDetails?.email?:""
        includedModel.value=drawermodelvalue
        dashboardSuperadminModel.value=dashboardModelValue


    }
}
