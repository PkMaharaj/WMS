package com.wms.superadmin.modules.notificationapproval.data.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wms.superadmin.network.models.NotificationVericationSOList.NotificationVerifySOListResponse
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.modules.notificationapproval.data.model.*
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.notificationApprove.NotificationApproveObject
import com.wms.superadmin.network.models.notificationApprove.Notificationapproveresponse
import com.wms.superadmin.network.repository.NetworkRepository
import com.wms.superadmin.network.resources.Response
import kotlinx.coroutines.launch
import kotlin.collections.MutableList
import org.koin.core.KoinComponent
import org.koin.core.inject

class NotificationApprovalVM : ViewModel(), KoinComponent {
    val notificationApprovalModel: MutableLiveData<NotificationApprovalModel> =
        MutableLiveData(NotificationApprovalModel())

    var navArguments: Bundle? = null

    val recyclerFifoList: MutableLiveData<MutableList<NotificationApprovalRowModel>> =
        MutableLiveData(mutableListOf())

    val recyclerdiscountitemList: MutableLiveData<MutableList<NotificationApprovalRowModel>> =
        MutableLiveData(mutableListOf())

    val recyclernegativestockList: MutableLiveData<MutableList<NotificationApprovalRowModel>> =
        MutableLiveData(mutableListOf())

    val progressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    private val networkRepository: NetworkRepository by inject()
    private val prefs: PreferenceHelper by inject()
    var user=prefs.getSADetails<LoginResponse>()!!

    val fetch0522SMU010513LiveData: MutableLiveData<Response<NotificationVerifySOListResponse>> =
        MutableLiveData<Response<NotificationVerifySOListResponse>>()

    val notificationapprovalLiveData: MutableLiveData<Response<Notificationapproveresponse>> =
        MutableLiveData<Response<Notificationapproveresponse>>()

    fun notification(so_number: String?) {
        viewModelScope.launch {
            progressLiveData.postValue(true)
            fetch0522SMU010513LiveData.postValue(
                networkRepository.fetchNotificationVerifyList(
                    authorization = "bearer ${prefs.getAccess_token()}",
                    OrgId = user.orgID,
                    BranchId = user.branchID,
                    Salesordernumber = so_number
                ))
            progressLiveData.postValue(false)
        }
    }

    fun notificationapprove(request: NotificationApproveObject, ApprovalRequest: String){
        viewModelScope.launch {
            progressLiveData.postValue(true)
            notificationapprovalLiveData.postValue(
                networkRepository.notificationapprove(
                    authorization = "bearer ${prefs.getAccess_token()}",
                    RequestApprove = ApprovalRequest,
                    notificationapproveRequest = request
                ))
            progressLiveData.postValue(false)
        }
    }


    fun bindFetch0522SMU010513Response(response: NotificationVerifySOListResponse) {
        val notificationApprovalModelValue = notificationApprovalModel.value ?: NotificationApprovalModel()
        var fifoskippedlist= arrayListOf<NotificationApprovalRowModel>()
        var itemdiscountlist_temp= arrayListOf<NotificationApprovalRowModel>()
        var itemdiscountlist= arrayListOf<NotificationApprovalRowModel>()
        var negitivestocklist= arrayListOf<NotificationApprovalRowModel>()
        val recyclerHorizontalLines = response.itemDetails?.map {
            NotificationApprovalRowModel(
                txtItemName = it?.itemName.toString()?:"",
                txtWarehousename = it?.warehouseName.toString()?:"",
                txtqtyordered = it?.quantityOrdered.toString()?:"",
                txtnegativeqty = it?.totalItemNegativeStock.toString()?:"",
                txtdiscountpercent = it?.itemDiscounts,
                sowithitemid = it?.salesOrderWithItemID,
                totalqty = it?.totalLinItemQuantity,
                itemdiscounts = it?.itemDiscounts,
                approvenegativestock = it?.approveNegativeStock,
                orderDiscountApprove = it?.approveOrderDiscount,
                orderDiscountPer = it?.orderDiscountValue?.toString(),
                orderdiscountamt = it?.orderDiscountAmt?.toString(),
                salesordernum = it?.salesOrderNumber.toString(),
                itemcode = it?.itemCode,
                warehouseID = it?.warehouseID?.toString(),
                batchID = it?.batchID.toString(),
                orgID = it?.orgID,
                totalLinItemQuantity = it?.totalLinItemQuantity!!.toString(),
                approveitemDis = it.approveitemDis,
//                changeItemDiscounts = it.changeItemDiscounts.toString(),
                rate = it.rate,
                bagqty = it.bagQty,
                value = it.value,
                itemDiscountValue = it.itemDiscountValue,
//                changeQty = it.changeQty.toString(),

                isFifoSkiped = it.isFIFOSkipped ?:false,
                isDiscountItem = it.isItemDiscountExceeded?:false,
                isNegitiveStock = it.isNegativeStock?:false,
                isOrderDiscountRangeExceeded = it.isOrderDiscountRangeExceeded ?:false,
            )
        }?.toMutableList()

        for(item in recyclerHorizontalLines as ArrayList){
            if(item.isFifoSkiped!!)
                fifoskippedlist.add(item)
            if(item.isDiscountItem!!) {
                itemdiscountlist_temp.add(item)
            }
            if(item.isNegitiveStock!!)
                negitivestocklist.add(item)
        }

        itemdiscountlist_temp.groupBy { it?.itemcode }.map {
            var itemgroupped = it.value[0]

            itemgroupped.txtqtyordered = it.value.sumOf { it.txtqtyordered?.toIntOrNull()?:0 }.toString()
            itemdiscountlist.add(itemgroupped)
        }

        recyclerFifoList.value = fifoskippedlist.toMutableList()
        recyclernegativestockList.value = negitivestocklist.toMutableList()
        recyclerdiscountitemList.value = itemdiscountlist.toMutableList()


//        Order discount
        for (item in recyclerHorizontalLines){
            Log.e("Order_discount ", item.isOrderDiscountRangeExceeded.toString())
            if (item.isOrderDiscountRangeExceeded!!){
                notificationApprovalModelValue.IsOrderDiscountRangeExceeded = item.isOrderDiscountRangeExceeded!!
                var orderquantity = response.itemDetails.sumOf { it?.totalLinItemQuantity?:0.0 }
                var orderdiscount = response.itemDetails.sumOf { it?.orderDiscountAmt?:0.0 }

                notificationApprovalModel.value?.totalqtyValue = orderquantity.toString()
                notificationApprovalModelValue.orderdiscount = orderdiscount.div(response.itemDetails.size).toString()
                notificationApprovalModelValue.orderchangeamt = orderdiscount.div(response.itemDetails.size).toString()
            }
        }

//        Customer credit details
        var creditlimit: Boolean = response.salesOrderDetails?.isCreditLimitExceeded!!
        var IsCreditDaysExceeded: Boolean = response.salesOrderDetails.isCreditDaysExceeded!!
        var IsBillsExceeded: Boolean = response.salesOrderDetails.isBillsExceeded!!

        notificationApprovalModelValue.isCreditLimitExceeded = creditlimit
        notificationApprovalModelValue.isCreditDaysExceeded = IsCreditDaysExceeded
        notificationApprovalModelValue.isBillsExceeded = IsBillsExceeded

        notificationApprovalModelValue.oldcreditlmt = response.salesOrderDetails.crLimit
        notificationApprovalModelValue.newcreditlmt = response.salesOrderDetails.newCrdLimit

        notificationApprovalModelValue.oldcreditdays = response.salesOrderDetails.crDays
        notificationApprovalModelValue.newcreditdays = response.salesOrderDetails.newCrdDays

        notificationApprovalModelValue.oldoutstandingbill = response.salesOrderDetails.noofOutstandingBill.toString()
        notificationApprovalModelValue.newoutstandingbill = response.salesOrderDetails.newCrdBills


        if(recyclerFifoList.value!!.isNotEmpty())
            notificationApprovalModelValue.IsFifoSkipped=true

        if (recyclernegativestockList.value!!.isNotEmpty())
            notificationApprovalModelValue.IsNegitiveStockTaken = true

        if (recyclerdiscountitemList.value!!.isNotEmpty()) {
//            notificationApprovalModelValue.IsOrderDiscountRangeExceeded = true
            notificationApprovalModelValue.IsItemDiscountExceeded = true
            for (item in recyclerHorizontalLines) {
                item.approveitemDis = true
            }
        }

        notificationApprovalModelValue.custID = response.salesOrderDetails?.custID

        notificationApprovalModel.value = notificationApprovalModelValue
    }
}

