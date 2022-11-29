package com.wms.superadmin.network

import com.wms.superadmin.network.models.Login.FetchLoginRequest
//import com.wms.superadmin.network.models.createallbranchreceivables.CreateAllBranchReceivablesResponse
import com.wms.superadmin.network.models.NotificationVericationSOList.NotificationVerifySOListResponse
import com.wms.superadmin.network.models.createsalesorderreport.SalesOrderReportRequest
import com.wms.superadmin.network.models.createsalesorderreport.CreateSalesOrderReportResponse
import com.wms.superadmin.network.models.create123.Create123Response
import com.wms.superadmin.network.models.create7411313141.Create7411313141Response
import com.wms.superadmin.network.models.create820469.Create820469Response
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
import okhttp3.RequestBody
//import com.wms.superadmin.network.models.stocksummary.StockReportRequest
//import com.wms.superadmin.network.models.stocksummary.StockReportResponse
import retrofit2.http.*
import kotlin.String

public interface RetrofitServices {
    //  @POST("/apis/wmsmobile/api/login/CreatePassword/1/123")
    @POST("api/login/CreatePassword/{CustID}/{NewPassword}")
    public suspend fun create123(
        @Header("Authorization") authorization: String?,
        @Path("CustID") CustID: String?,
        @Path("NewPassword") NewPassword: String?
    ): Create123Response

    @POST("api/login/ApproveOTP/{MobileNumber}/{OTP}")
    public suspend fun create820469(
        @Header("Authorization")authorization: String?,
        @Path("MobileNumber")MobileNumber:String?,
        @Path("OTP")OTP:String?
    ): Create820469Response

    //  @POST("/apis/wmsmobile/api/login/MobileNumberRegistrationWithOTP/7411313141")
    @POST("api/login/MobileNumberRegistrationWithOTP/{MobileNumber}")
    public suspend fun create7411313141(
        @Header("Authorization") authorization: String?,
        @Path("MobileNumber")MobileNumber:String?
    ): Create7411313141Response


    @POST("api/login/CustomerLogin")
    public suspend fun Login(
        @Header("Authorization") authorization: String?,
        @Body fetchLoginRequest: FetchLoginRequest
    ): LoginResponse

    @FormUrlEncoded
    @POST("token")
    public suspend fun createToken(
        @Field("username")username:String?,
        @Field("password")password:String?,
        @Field("grant_type")grant_type:String?
    ): CreateTokenResponse

    @POST("api/SuperAdmin/BranchList")
    public suspend fun createBranchList(@Header("Authorization") authorization: String?, @Body
    createBranchListRequest: CreateBranchListRequest?): CreateBranchListResponse

    @POST("api/SuperAdmin/BankAccount")
    public suspend fun fetchBankBookList(@Header("Authorization") authorization: String?, @Body
    createBankAccountRequest: BankBookRequest?): BankBookResponse

    @POST("api/SuperAdmin/BankAccountVoucher")
    public suspend fun fetchvocherWiseBankBook(@Header("Authorization") authorization: String?, @Body
    createBankAccountVoucherRequest: VoucherRequest?): BankBookResponse

    @POST("api/SuperAdmin/CashinHand")
    public suspend fun fetcCashBookList(@Header("Authorization") authorization: String?, @Body
    createBankAccountRequest: BankBookRequest?): BankBookResponse

    @POST("api/SuperAdmin/BankAccountVoucher")
    public suspend fun fetchvocherWiseCashBook(@Header("Authorization") authorization: String?,
                                               @Body createBankAccountVoucherRequest: VoucherRequest?): BankBookResponse

    // Notification
    @GET("api/SuperAdmin/Notification1/{OrgId}/{BranchID}")
    public suspend fun fetchNotificationList(
        @Header("Authorization") authorization: String?,
        @Path("OrgId")OrgId: String?,
        @Path("BranchID")BranchID: String?
    ): NotificationResponse

//    @GET("api/SuperAdmin/GetApprovalOrderDetails/1/0/0522SMU010513")
    @GET("api/SuperAdmin/GetApprovalOrderDetails/{OrgId}/{BranchID}/{Salesordernumber}")
    public suspend fun fetchNotificationVerifyList(
    @Header("Authorization") authorization: String?,
    @Path("OrgId")OrgId: String?,
    @Path("BranchID")BranchID: String?,
    @Path("Salesordernumber")Salesordernumber: String?
    ): NotificationVerifySOListResponse


//    @POST("http://192.168.29.198/api/SuperAdmin/PostVerificationDetails/Approve")
    @POST("api/SuperAdmin/PostVerificationDetails/{RequestApprove}")
    public suspend fun approveSONotification(
    @Header("Authorization") authorization: String?,
    @Path("RequestApprove") RequestApprove: String?,
    @Body approveSONotificationRequest: NotificationApproveObject
    ): Notificationapproveresponse


//     Sales order
    @POST("api/SuperAdmin/salesOrderReport")
    public suspend fun createSalesOrderReport(
    @Header("Authorization") authorization: String?,
    @Body salesOrderReportRequest: SalesOrderReportRequest?): CreateSalesOrderReportResponse

//    Sales register
    @POST("api/SuperAdmin/SalesRegister")
    suspend fun createSalesRegister(@Header("Authorization") authorization: String?,
                                    @Body salesRegisterRequest: SalesRegisterRequest?): SalesRegisterResponse

//    Purchase order
    @POST("api/SuperAdmin/PurchaseOrder")
    public suspend fun createPurchaseOrderReport(
        @Header("Authorization") authorization: String?,
        @Body salesOrderReportRequest: SalesOrderReportRequest?): CreateSalesOrderReportResponse

//    Purchase register
    @POST("api/SuperAdmin/Purchase")
    suspend fun createPurchaseRegister(@Header("Authorization") authorization: String?,
                                    @Body salesRegisterRequest: SalesRegisterRequest?): SalesRegisterResponse


// receivable //
    @POST("api/SuperAdmin/AllBranchReceivables")
    suspend fun createAllBranchReceivables(
        @Header("Authorization") authorization: String?,
        @Body createAllBranchReceivablesRequest: TransactionRequest?
    ): CreateAllBranchReceivablesResponse

@POST("api/SuperAdmin/StockSummary")
suspend fun getStockReport(
    @Header("Authorization") authorization: String?,
    @Body stockReportRequest: StockReportRequest): StockReportResponse

    @GET("api/SuperAdmin/GetDashboardData/{UserID}/{OrgID}/{BranchID}")
    public suspend fun fetchDashboardDetails(
        @Header("Authorization") authorization: String?,
        @Path("OrgID")OrgId: String?,
        @Path("BranchID")BranchID: String?,
        @Path("UserID")UserId: String?
    ): DashboardResponse

    @GET("api/SuperAdmin/GetDashboardData/{UserID}/{OrgID}/{BranchID}/{FromDate}/{ToDate}")
    public suspend fun fetchDashboardDetails(
        @Header("Authorization") authorization: String?,
        @Path("OrgID")OrgId: String?,
        @Path("BranchID")BranchID: String?,
        @Path("UserID")UserId: String?,
        @Path("FromDate")FromDate: String?,
        @Path("ToDate")ToDate: String?
    ): DashboardResponse

     /**Api for save Attendance data**/
    @Multipart
    @JvmSuppressWildcards
    @POST("api/login/SaveAttendenceData")
    public suspend fun saveattendance(@Header("Authorization") authorization: String?,
                                      @PartMap savePayment:Map<String, CreateSaveAttendencetDataRequest>,
                                      @PartMap file: Map<String, RequestBody>?): CreateSaveAttendencetDataResponse

}
//public const val BASE_URL: String = "http://192.168.0.105/WMSMobileApi/"
public const val BASE_URL: String = "https://www.wbtechindia.com/apis/WMSMOBILEAPIS/"
