package com.wms.superadmin.network.repository

import android.util.Log
import com.wms.superadmin.network.models.NotificationVericationSOList.NotificationVerifySOListResponse
import com.wms.superadmin.network.models.createsalesorderreport.SalesOrderReportRequest
import com.wms.superadmin.network.models.createsalesorderreport.CreateSalesOrderReportResponse
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.extensions.NoInternetConnection
import com.wms.superadmin.extensions.isOnline
import com.wms.superadmin.network.RetrofitServices
import com.wms.superadmin.network.models.Login.FetchLoginRequest
import com.wms.superadmin.network.models.create123.Create123Response
import com.wms.superadmin.network.models.create7411313141.Create7411313141Response
import com.wms.superadmin.network.models.create820469.Create820469Response
import com.wms.superadmin.network.models.createtoken.CreateTokenRequest
import com.wms.superadmin.network.models.createtoken.CreateTokenResponse
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.Salesregister.SalesRegisterRequest
import com.wms.superadmin.network.models.Salesregister.SalesRegisterResponse
import com.wms.superadmin.network.models.bankbook.BankBookRequest
import com.wms.superadmin.network.models.bankbook.BankBookResponse
import com.wms.superadmin.network.models.bankbook.VoucherRequest
import com.wms.superadmin.network.models.createallbranchreceivables.CreateAllBranchReceivablesResponse
import com.wms.superadmin.network.models.createallbranchreceivables.TransactionRequest
import com.wms.superadmin.network.models.fetch0.NotificationResponse
import com.wms.superadmin.network.models.notificationApprove.NotificationApproveObject
import com.wms.superadmin.network.models.notificationApprove.Notificationapproveresponse
import com.wms.superadmin.network.models.pojos.*
import com.wms.superadmin.network.models.stocksummary.StockReportRequest
import com.wms.superadmin.network.models.stocksummary.StockReportResponse
//import com.wms.superadmin.network.models.stocksummary.StockReportRequest
//import com.wms.superadmin.network.models.stocksummary.StockReportResponse
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.Response
import com.wms.superadmin.network.resources.SuccessResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.lang.Exception
import kotlin.String
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.File

public class NetworkRepository : KoinComponent {
  private val retrofitServices: RetrofitServices by inject()

  private val errorMessage: String = "Something went wrong."

  public suspend fun create123(authorization: String?, CustID: String?, NewPassword: String?):
          Response<Create123Response> {
    Log.d("create123", "Network call")
    return try{
      val isOnline = MyApp.getInstance().isOnline()
      if(isOnline) {
        SuccessResponse(retrofitServices.create123(authorization, CustID, NewPassword))
      } else {
        val internetException =
          NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
        ErrorResponse(internetException.message ?:errorMessage, internetException)
      }
    } catch(e:Exception){
      e.printStackTrace()
      ErrorResponse(e.message ?:errorMessage, e)
    }
  }

  public suspend fun create820469(authorization: String?, MobileNumber: String?, OTP:String?): Response<Create820469Response> {
    Log.d("create820469", "Network call")
    return try{
      val isOnline = MyApp.getInstance().isOnline()
      if(isOnline) {
        SuccessResponse(retrofitServices.create820469(authorization, MobileNumber, OTP))
      } else {
        val internetException =
          NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
        ErrorResponse(internetException.message ?:errorMessage, internetException)
      }
    } catch(e:Exception){
      e.printStackTrace()
      ErrorResponse(e.message ?:errorMessage, e)
    }
  }

  public suspend fun create7411313141(authorization: String?, MobileNumber:String?): Response<Create7411313141Response> {
    Log.d("create7411313141", "Network call")
    return try{
      val isOnline = MyApp.getInstance().isOnline()
      if(isOnline) {
        SuccessResponse(retrofitServices.create7411313141(authorization, MobileNumber))
      } else {
        val internetException =
          NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
        ErrorResponse(internetException.message ?:errorMessage, internetException)
      }
    } catch(e:Exception){
      e.printStackTrace()
      ErrorResponse(e.message ?:errorMessage, e)
    }
  }

  /*public suspend fun fetch123(authorization: String?, MobileNumber:String?, Password:String?):
          Response<LoginResponse> {
    Log.d("fetch123", "Network call")
    return try{
      val isOnline = MyApp.getInstance().isOnline()
      if(isOnline) {
        SuccessResponse(retrofitServices.Login(authorization, MobileNumber, Password))
      } else {
        val internetException =
          NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
        ErrorResponse(internetException.message ?:errorMessage, internetException)
      }
    } catch(e:Exception){
      e.printStackTrace()
      ErrorResponse(e.message ?:errorMessage, e)
    }
  }*/

  public suspend fun Login(authorization: String?,request: FetchLoginRequest):
          Response<LoginResponse> {
    Log.d("fetch123", "Network call")
    return try {
      val isOnline = MyApp.getInstance().isOnline()
      if (isOnline) {
        SuccessResponse(retrofitServices.Login(authorization, request))
      } else {
        val internetException =
          NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
        ErrorResponse(internetException.message ?: errorMessage, internetException)
      }
    } catch (e: Exception) {
      e.printStackTrace()
      ErrorResponse(e.message ?: errorMessage, e)
    }
  }

  public suspend fun createToken(createTokenRequest: CreateTokenRequest?):
          Response<CreateTokenResponse> {
    Log.d("createToken", "Network call")
    return try{
      val isOnline = MyApp.getInstance().isOnline()
      if(isOnline) {
        SuccessResponse(retrofitServices.createToken(createTokenRequest?.username,createTokenRequest?.password,createTokenRequest?.grantType))
      } else {
        val internetException =
          NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
        ErrorResponse(internetException.message ?:errorMessage, internetException)
      }
    } catch(e:Exception){
      e.printStackTrace()
      ErrorResponse(e.message ?:errorMessage, e)
    }
  }
  public suspend fun createBranchList(authorization: String?,
                                      createBranchListRequest: CreateBranchListRequest?): Response<CreateBranchListResponse> {
    Log.d("createBranchList", "Network call")
    return try {
      val isOnline = MyApp.getInstance().isOnline()
      if(isOnline) {
        SuccessResponse(retrofitServices.createBranchList(authorization, createBranchListRequest))
      } else {
        val internetException =
          NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
        ErrorResponse(internetException.message ?:errorMessage, internetException)
      }
    } catch(e:Exception) {
      e.printStackTrace()
      ErrorResponse(e.message ?:errorMessage, e)
    }
  }

  public suspend fun GetBankBookList(authorization: String?,createBankAccountRequest: BankBookRequest?): Response<BankBookResponse> {
    Log.d("createBankAccount", "Network call")
    return try {
      val isOnline = MyApp.getInstance().isOnline()
      if(isOnline) {
        SuccessResponse(retrofitServices.fetchBankBookList(authorization, createBankAccountRequest))
      } else {
        val internetException =
          NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
        ErrorResponse(internetException.message ?:errorMessage, internetException)
      }
    } catch(e:Exception) {
      e.printStackTrace()
      ErrorResponse(e.message ?:errorMessage, e)
    }
  }

  public suspend fun getvocherWiseBankBook(authorization: String?,createBankAccountVoucherRequest: VoucherRequest?):Response<BankBookResponse> {
    Log.d("createBankAccountVoucher", "Network call")
    return try {
      val isOnline = MyApp.getInstance().isOnline()
      if(isOnline) {
        SuccessResponse(retrofitServices.fetchvocherWiseBankBook(authorization, createBankAccountVoucherRequest))
      } else {
        val internetException =
          NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
        ErrorResponse(internetException.message ?:errorMessage, internetException)
      }
    } catch(e:Exception) {
      e.printStackTrace()
      ErrorResponse(e.message ?:errorMessage, e)
    }
  }

  public suspend fun fetchNotificationList(authorization: String?, OrgId: String?, BranchId: String?): Response<NotificationResponse> = try {
    val isOnline = MyApp.getInstance().isOnline()
    if(isOnline) {
      SuccessResponse(retrofitServices.fetchNotificationList(authorization,OrgId,BranchId))
    } else {
      val internetException =
        NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
      ErrorResponse(internetException.message ?:errorMessage, internetException)
    }
  } catch(e:Exception) {
    e.printStackTrace()
    ErrorResponse(e.message ?:errorMessage, e)
  }


//  Sales Order
public suspend fun createSalesOrderReport(authorization: String?, salesOrderReportRequest: SalesOrderReportRequest?):
        Response<CreateSalesOrderReportResponse> = try {
  val isOnline = MyApp.getInstance().isOnline()
  if(isOnline) {
    SuccessResponse(retrofitServices.createSalesOrderReport(authorization, salesOrderReportRequest))
  } else {
    val internetException =
      NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
    ErrorResponse(internetException.message ?:errorMessage, internetException)
  }
} catch(e:Exception) {
  e.printStackTrace()
  ErrorResponse(e.message ?:errorMessage, e)
}

  suspend fun createSalesRegister(authorization: String?, salesRegisterRequest: SalesRegisterRequest?):
          Response<SalesRegisterResponse> = try {
    val isOnline = MyApp.getInstance().isOnline()
    if(isOnline) {
      SuccessResponse(retrofitServices.createSalesRegister(authorization, salesRegisterRequest))
    } else {
      val internetException = NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
      ErrorResponse(internetException.message ?:errorMessage, internetException)
    }
  } catch(e:Exception) {
    e.printStackTrace()
    ErrorResponse(e.message ?:errorMessage, e)
  }


//    Purchase Order
  public suspend fun createPurchaseOrderReport(authorization: String?, salesOrderReportRequest: SalesOrderReportRequest?):
          Response<CreateSalesOrderReportResponse> = try {
    val isOnline = MyApp.getInstance().isOnline()
    if(isOnline) {
      SuccessResponse(retrofitServices.createPurchaseOrderReport(authorization, salesOrderReportRequest))
    } else {
      val internetException =
        NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
      ErrorResponse(internetException.message ?:errorMessage, internetException)
    }
  } catch(e:Exception) {
    e.printStackTrace()
    ErrorResponse(e.message ?:errorMessage, e)
  }

  suspend fun createPurchaseRegister(authorization: String?, salesRegisterRequest: SalesRegisterRequest?):
          Response<SalesRegisterResponse> = try {
    val isOnline = MyApp.getInstance().isOnline()
    if(isOnline) {
      SuccessResponse(retrofitServices.createPurchaseRegister(authorization, salesRegisterRequest))
    } else {
      val internetException =
        NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
      ErrorResponse(internetException.message ?:errorMessage, internetException)
    }
  } catch(e:Exception) {
    e.printStackTrace()
    ErrorResponse(e.message ?:errorMessage, e)
  }



  suspend fun fetchNotificationVerifyList(authorization:String?, OrgId:String?, BranchId:String?, Salesordernumber:String?
                                          ): Response<NotificationVerifySOListResponse> =
    try {
      val isOnline = MyApp.getInstance().isOnline()
      if(isOnline) {
        SuccessResponse(retrofitServices.fetchNotificationVerifyList(authorization,OrgId,BranchId,Salesordernumber))
      } else {
        val internetException =
          NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
        ErrorResponse(internetException.message ?:errorMessage, internetException)
      }
    } catch(e:Exception) {
      e.printStackTrace()
      ErrorResponse(e.message ?:errorMessage, e)
    }

  suspend fun notificationapprove(authorization: String?, RequestApprove: String?, notificationapproveRequest: NotificationApproveObject):
          Response<Notificationapproveresponse> = try {
    val isOnline = MyApp.getInstance().isOnline()
    if(isOnline) {
      SuccessResponse(retrofitServices.approveSONotification(authorization,RequestApprove, notificationapproveRequest))
    } else {
      val internetException =
        NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
      ErrorResponse(internetException.message ?:errorMessage, internetException)
    }
  } catch(e:Exception) {
    e.printStackTrace()
    ErrorResponse(e.message ?:errorMessage, e)
  }

//receivablke//

  suspend fun createAllBranchReceivables(authorization: String?,
                                         createAllBranchReceivablesRequest: TransactionRequest?):
          Response<CreateAllBranchReceivablesResponse> = try {
    val isOnline = MyApp.getInstance().isOnline()
    if(isOnline) {
      SuccessResponse(retrofitServices.createAllBranchReceivables(authorization,
        createAllBranchReceivablesRequest))
    } else {
      val internetException =
        NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
      ErrorResponse(internetException.message ?:errorMessage, internetException)
    }
  } catch(e:Exception) {
    e.printStackTrace()
    ErrorResponse(e.message ?:errorMessage, e)
  }


  //Stock Summary//

  suspend fun fecthStockReport(authorization: String?, stockReportRequest: StockReportRequest):
          Response<StockReportResponse> = try {
    val isOnline = MyApp.getInstance().isOnline()
    if(isOnline) {
      SuccessResponse(retrofitServices.getStockReport(authorization, stockReportRequest))
    } else {
      val internetException =
        NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
      ErrorResponse(internetException.message ?:errorMessage, internetException)
    }
  } catch(e:Exception) {
    e.printStackTrace()
    ErrorResponse(e.message ?:errorMessage, e)
  }

  //DashBoard Counts//

  suspend fun getDasgBoardDetails(authorization: String?,userId:String,branchid:String,orgid:String,fromdare:String,todate:String):
          Response<DashboardResponse> = try {
    val isOnline = MyApp.getInstance().isOnline()
    if(isOnline) {
      SuccessResponse(retrofitServices.fetchDashboardDetails(authorization,orgid,branchid,userId,fromdare,todate))
    } else {
      val internetException =
        NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
      ErrorResponse(internetException.message ?:errorMessage, internetException)
    }
  } catch(e:Exception) {
    e.printStackTrace()
    ErrorResponse(e.message ?:errorMessage, e)
  }


      // Save Attendance//

  public suspend fun saveattendance(
    authorization: String?,
    payment: CreateSaveAttendencetDataRequest,
    file: File?): Response<CreateSaveAttendencetDataResponse> {
    return try {
      val isOnline = MyApp.getInstance().isOnline()
      if (isOnline) {
        val map_obj: MutableMap<String, CreateSaveAttendencetDataRequest> = HashMap()
        map_obj["json object"] = payment
        val map_file: MutableMap<String, RequestBody> = HashMap()
        val requestbody: RequestBody = file!!.asRequestBody("image/jpg".toMediaTypeOrNull())
        map_file["file1"] = requestbody
        SuccessResponse(retrofitServices.saveattendance(authorization, map_obj, map_file))

      } else {
        val internetException =
          NoInternetConnection(MyApp.getInstance().getString(R.string.no_internet_connection))
        ErrorResponse(internetException.message ?: errorMessage, internetException)
      }
    } catch (e: Exception) {
      e.printStackTrace()
      ErrorResponse(e.message ?: errorMessage, e)
    }
  }

}
