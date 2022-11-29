package com.wms.superadmin.network.models.Salesregister

import com.google.gson.annotations.SerializedName

data class SalesRegisterResponse(

	@field:SerializedName("SalesList")
	val salesList: CreateSalesRegisterResponseSalesList? = null,

	@field:SerializedName("PurchaseList")
	val purchaseList: CreateSalesRegisterResponseSalesList? = null
)

data class CreateSalesRegisterResponseSalesList(
	@field:SerializedName("OutputDataListObject")
	val outputDataListObject: ArrayList<ResponseSalesListOutputDataListObjectItem?>? = null
)


data class ResponseSalesListOutputDataListObjectItem(
	@field:SerializedName("CreationDate")
	val creationDate: String? = null,

	@field:SerializedName("BranchID")
	val branchID: String? = null,

	@field:SerializedName("BranchName")
	val branchName: String? = null,

	@field:SerializedName("LedgerName")
	val ledgerName: String? = null,

	@field:SerializedName("Debit")
	val debit: Double? = null,

	@field:SerializedName("ClosingBalance")
	val closingBalance: String? = null,

	@field:SerializedName("MergedDetails")
	val mergedDetails: String? = null,

	@field:SerializedName("Credit")
	val credit: Double? = null,

	@field:SerializedName("MonthName")
	val monthName: Any? = null,

	@field:SerializedName("MonthID")
	val monthID: Int? = 0,

	@field:SerializedName("VoucherID")
	val voucherid: String? = null,

	@field:SerializedName("VoucherType")
	val voucherType: String? = null,
)


