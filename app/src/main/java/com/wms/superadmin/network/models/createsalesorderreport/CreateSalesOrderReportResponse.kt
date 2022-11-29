package com.wms.superadmin.network.models.createsalesorderreport

import com.google.gson.annotations.SerializedName

data class CreateSalesOrderReportResponse(

	@field:SerializedName("salesorderlist")
	var salesorderlist: ArrayList<SalesOrderReportResponselistItem?>? = null,

	@field:SerializedName("PurchaseList")
	var purchaselist: ArrayList<SalesOrderReportResponselistItem?>? = null,

	@field:SerializedName("status")
	var status: Boolean? = null
)

data class SalesOrderReportResponselistItem(

	@field:SerializedName("ItemStatus")
	var itemStatus: Any? = null,

	@field:SerializedName("CreationDate")
	var creationDate: String? = null,

	@field:SerializedName("SalesOrderItemWarehouseMapResult")
	var salesOrderItemWarehouseMapResult: Any? = null,

	@field:SerializedName("SOOrgID")
	var sOOrgID: Any? = null,

	@field:SerializedName("AlteredDate")
	var alteredDate: Any? = null,

	@field:SerializedName("CreditTypevarue")
	var creditTypevarue: Any? = null,

	@field:SerializedName("isSCMSO_MCMSO")
	var isSCMSOMCMSO: Boolean? = null,

	@field:SerializedName("TotalItemCount")
	var totalItemCount: Int? = null,

	@field:SerializedName("DLCustomerVendorDetail")
	var dLCustomerVendorDetail: Any? = null,

	@field:SerializedName("OrgName")
	var orgName: Any? = null,

	@field:SerializedName("BranchID")
	var SO_branchID: String? = null,

	@field:SerializedName("BranchId")
	var PO_branchID: String? = null,

	@field:SerializedName("itemlist")
	var itemlist: Any? = null,

	@field:SerializedName("ApprovarStatus")
	var approvarStatus: Any? = null,

	@field:SerializedName("DLCreditTypes")
	var dLCreditTypes: List<Any?>? = null,

	@field:SerializedName("Photo2")
	var photo2: Any? = null,

	@field:SerializedName("UpdateDate")
	var updateDate: Any? = null,

	@field:SerializedName("Retailer_Request")
	var retailerRequest: Boolean? = null,

	@field:SerializedName("GetImageSourceEdit")
	var getImageSourceEdit: Any? = null,

	@field:SerializedName("OrgDetails")
	var orgDetails: Any? = null,

	@field:SerializedName("CreatedType")
	var createdType: Any? = null,

	@field:SerializedName("ID")
	var iD: Int? = null,

	@field:SerializedName("BranchName")
	var branchName: String? = null,

	@field:SerializedName("SourceOfUpdate")
	var sourceOfUpdate: Any? = null,

	@field:SerializedName("Status")
	var status: Any? = null,

	@field:SerializedName("ConvertToPO")
	var convertToPO: Any? = null,

	@field:SerializedName("RetMobileNumber")
	var retMobileNumber: Any? = null,

	@field:SerializedName("Photo1")
	var photo1: Any? = null,

	@field:SerializedName("BrokerName")
	var brokerName: Any? = null,

	@field:SerializedName("SalesOrderNumber")
	var salesOrderNumber: String? = null,

	@field:SerializedName("PurchaseOrderNumber")
	var purchaseOrderNumber: String? = null,

	@field:SerializedName("CustID")
	var custID: Any? = null,

	@field:SerializedName("CmpBankName")
	var cmpBankName: Any? = null,

	@field:SerializedName("IsDirectSale")
	var isDirectSale: Any? = null,

	@field:SerializedName("FromDate")
	var fromDate: Any? = null,

	@field:SerializedName("CmpCity")
	var cmpCity: Any? = null,

	@field:SerializedName("SalesType")
	var salesType: Any? = null,

	@field:SerializedName("TotalQtyAllocatedItems")
	var totalQtyAllocatedItems: Int? = null,

	@field:SerializedName("ModifiedByID")
	var modifiedByID: Any? = null,

	@field:SerializedName("Narration")
	var narration: Any? = null,

	@field:SerializedName("DLBusinessTypes")
	var dLBusinessTypes: List<Any?>? = null,

	@field:SerializedName("SalesDatetime")
	var salesDatetime: Any? = null,

	@field:SerializedName("ModifiedDate")
	var modifiedDate: String? = null,

	@field:SerializedName("OrgID")
	var orgID: Any? = null,

	@field:SerializedName("TotalVouchersCount")
	var totalVouchersCount: Int? = null,

	@field:SerializedName("PANNumber")
	var pANNumber: Any? = null,

	@field:SerializedName("VoucherTypeID")
	var voucherTypeID: Any? = null,

	@field:SerializedName("ItemName")
	var itemName: Any? = null,

	@field:SerializedName("ViewSO")
	var viewSO: Any? = null,

	@field:SerializedName("EnteredBy")
	var enteredBy: Any? = null,

	@field:SerializedName("IsDirectSO")
	var isDirectSO: Any? = null,

	@field:SerializedName("UsedBillCount")
	var usedBillCount: Int? = null,

	@field:SerializedName("IsGatePassEntered")
	var isGatePassEntered: Any? = null,

	@field:SerializedName("SearchText")
	var searchText: Any? = null,

	@field:SerializedName("IsDiscountRangeExceeded")
	var isDiscountRangeExceeded: Any? = null,

	@field:SerializedName("ParentVoucherTypeName")
	var parentVoucherTypeName: Any? = null,

	@field:SerializedName("PROrgID")
	var pROrgID: Any? = null,

	@field:SerializedName("Year")
	var year: Int? = null,

	@field:SerializedName("SalesmanID")
	var salesmanID: Any? = null,

	@field:SerializedName("DiscountPercentage")
	var discountPercentage: Any? = null,

	@field:SerializedName("CmpIFSCODE")
	var cmpIFSCODE: Any? = null,

	@field:SerializedName("GetImageSourceView")
	var getImageSourceView: Any? = null,

	@field:SerializedName("ToDate")
	var toDate: Any? = null,

	@field:SerializedName("GSTPercentage")
	var gSTPercentage: Int? = null,

	@field:SerializedName("CmpDeclaration")
	var cmpDeclaration: Any? = null,

	@field:SerializedName("VisitedDate")
	var visitedDate: String? = null,

	@field:SerializedName("DLBranchList")
	var dLBranchList: List<Any?>? = null,

	@field:SerializedName("IsDelivaryNote")
	var isDelivaryNote: Any? = null,

	@field:SerializedName("CreditTypeId")
	var creditTypeId: Any? = null,

	@field:SerializedName("IsSelected")
	var isSelected: Boolean? = null,

	@field:SerializedName("WarehouseID")
	var warehouseID: Any? = null,

	@field:SerializedName("IsTallyUpdated")
	var isTallyUpdated: Boolean? = null,

//	@field:SerializedName("TotalValue")
//	var totalvalue: Int? = null,

	@field:SerializedName("TotalValue")
	var totalvalue: Double? = null,

	@field:SerializedName("MonthID")
	var monthID: Int? = null,

	@field:SerializedName("SignatureImage")
	var signatureImage: Any? = null,

	@field:SerializedName("OrderNumberCustName")
	var orderNumberCustName: Any? = null,

	@field:SerializedName("SalesDateInDateFormat")
	var salesDateInDateFormat: Any? = null,

	@field:SerializedName("ItemFullName")
	var itemFullName: Any? = null,

	@field:SerializedName("CmpAccNumber")
	var cmpAccNumber: Any? = null,

	@field:SerializedName("CategoryID")
	var categoryID: Int? = null,

	@field:SerializedName("DLBrokerList")
	var dLBrokerList: List<Any?>? = null,

	@field:SerializedName("ReferenceNumber")
	var referenceNumber: Any? = null,

	@field:SerializedName("ParentVoucherTypeID")
	var parentVoucherTypeID: Any? = null,

	@field:SerializedName("BusinessTypevarue")
	var businessTypevarue: Any? = null,

	@field:SerializedName("IsBulkSale")
	var isBulkSale: Any? = null,

	@field:SerializedName("CmpPANNumber")
	var cmpPANNumber: Any? = null,

	@field:SerializedName("DLVoucherTypesList")
	var dLVoucherTypesList: List<Any?>? = null,

	@field:SerializedName("CreatedByID")
	var createdByID: Int? = null,

	@field:SerializedName("IsDetailedVoucher")
	var isDetailedVoucher: Boolean? = null,

	@field:SerializedName("RSalesDatetime")
	var rSalesDatetime: Any? = null,

	@field:SerializedName("SalesmanName")
	var salesmanName: Any? = null,

	@field:SerializedName("MergedDetails")
	var mergedDetails: Any? = null,

	@field:SerializedName("DeliverycenterID")
	var deliverycenterID: Any? = null,

	@field:SerializedName("ItemCode")
	var itemCode: Any? = null,

	@field:SerializedName("IsBillsExceeded")
	var isBillsExceeded: Any? = null,

	@field:SerializedName("TotalQTY")
	var totalQTY: Int? = null,

	@field:SerializedName("BrokerID")
	var brokerID: Any? = null,

	@field:SerializedName("IsActive")
	var isActive: Any? = null,

	@field:SerializedName("DiscountAmt")
	var discountAmt: Any? = null,

	@field:SerializedName("CmpTermsAndConditions")
	var cmpTermsAndConditions: Any? = null,

	@field:SerializedName("TransType")
	var transType: Any? = null,

	@field:SerializedName("VoucherTypeName")
	var voucherTypeName: String? = null,

	@field:SerializedName("AlteredBy")
	var alteredBy: String? = null,

	@field:SerializedName("URDNumber")
	var uRDNumber: Any? = null,

	@field:SerializedName("RequisitionNumber")
	var requisitionNumber: Any? = null,

	@field:SerializedName("DLSalesOrderWithItemCreations")
	var dLSalesOrderWithItemCreations: Any? = null,

	@field:SerializedName("UserID")
	var userID: Int? = null,

	@field:SerializedName("RegistrationType")
	var registrationType: Any? = null,

	@field:SerializedName("ScreenName")
	var screenName: Int? = null,

	@field:SerializedName("SOSelected")
	var sOSelected: Boolean? = null,

	@field:SerializedName("CustomerName")
	var customerName: Any? = null,

	@field:SerializedName("DueDate")
	var dueDate: Any? = null,

	@field:SerializedName("RejectionRemark")
	var rejectionRemark: Any? = null,

	@field:SerializedName("SOWarehouseID")
	var sOWarehouseID: Any? = null,

	@field:SerializedName("RetSOSignature")
	var retSOSignature: Any? = null,

	@field:SerializedName("DLSalesPersonList")
	var dLSalesPersonList: List<Any?>? = null,

	@field:SerializedName("isDCSO")
	var isDCSO: Boolean? = null,

	@field:SerializedName("CorrectedList")
	var correctedList: Any? = null,

	@field:SerializedName("GSTPR")
	var gSTPR: Any? = null,

	@field:SerializedName("CashRegistrationType")
	var cashRegistrationType: Any? = null,

	@field:SerializedName("Comments")
	var comments: Any? = null,

	@field:SerializedName("IsCreditLimitExceeded")
	var isCreditLimitExceeded: Any? = null,

	@field:SerializedName("ShippingAdddress")
	var shippingAdddress: Any? = null,

	@field:SerializedName("Quantity")
	var quantity: Int? = null,

	@field:SerializedName("emailID")
	var emailID: Any? = null,

	@field:SerializedName("TotalAmount")
	var totalAmount: Int? = null,

	@field:SerializedName("SCMSO_MCMSONumber")
	var sCMSOMCMSONumber: Any? = null,

	@field:SerializedName("BusinessTypeName")
	var businessTypeName: Any? = null,

	@field:SerializedName("dcid")
	var dcid: Any? = null,

	@field:SerializedName("BusinessTypeId")
	var businessTypeId: Any? = null,

	@field:SerializedName("BranchList")
	var branchList: List<Any?>? = null,

	@field:SerializedName("CategoryName")
	var categoryName: Any? = null,

	@field:SerializedName("AvgBillAmount")
	var avgBillAmount: Int? = null,

	@field:SerializedName("RetSOName")
	var retSOName: Any? = null,

	@field:SerializedName("SOBranchID")
	var sOBranchID: Any? = null,

	@field:SerializedName("MonthName")
	var monthName: Any? = null,

	@field:SerializedName("IsCreditDaysExceeded")
	var isCreditDaysExceeded: Any? = null,

	@field:SerializedName("Location")
	var location: Any? = null,

	@field:SerializedName("PurchaseDateInDateFormat")
	var purchaseDateInDateFormat: String? = "",

	@field:SerializedName("PurchaseType")
	var purchasetype: String? = "",

	@field:SerializedName("PurchaseDate")
	var purchaseDate: String? = "",

//	@field:SerializedName("SalesOrderNumber")
//	val salesorderNumber: String? = null,
//
//	@field:SerializedName("PurchaseOrderNumber")
//	val purchaseorderNumber: String? = null
)
