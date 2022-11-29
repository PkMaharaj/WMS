package com.wms.superadmin.network.models.bankbook

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BankBookRequest(

	@field:SerializedName("OrgID")
	val orgID: String? = null,

	@field:SerializedName("ScreenName")
	val screenName: String? = null,

	@field:SerializedName("FromDate")
	var fromDate: String? = null,

	@field:SerializedName("ToDate")
	var toDate: String? = null,

	@field:SerializedName("BranchID")
	val branchID: String? = null,

	@field:SerializedName("ReportType")
	val reportType: String? = null,

	@field:SerializedName("ListGroupData")
	val listGroupdata: List<GroupItem>?= null

):Serializable

data class VoucherRequest(

	@field:SerializedName("OrgID")
	val orgID: String? = null,

	@field:SerializedName("ScreenName")
	val screenName: String? = null,

	@field:SerializedName("FromDate")
	var fromDate: String? = null,

	@field:SerializedName("ToDate")
	var toDate: String? = null,

	@field:SerializedName("BranchID")
	val branchID: String? = null,

	@field:SerializedName("ReportType")
	val reportType: String? = null,

	@field:SerializedName("ListMonths")
	val listMonths: List<LedgerItem?>? = null
):Serializable

data class LedgerItem(

	@field:SerializedName("LedgerID")
	val ledgerID: String? = null
):Serializable

data class GroupItem(

	@field:SerializedName("ID")
	val groupID: String? = null
):Serializable

data class BankBookResponse(

	@field:SerializedName("StatusMessage")
	val status:Boolean = false,

	@field:SerializedName("TotalClosingBalance")
	val totalClosingBalance: String? = null,

	@field:SerializedName("OutputDataListObject")
	val outputDataListObject: List<BankBookDateListItem?>? = null,

	@field:SerializedName("BranchName")
	val branchName: Any? = null,

	@field:SerializedName("TotalOpeningBalance")
	val totalOpeningBalance: String? = null,

	@field:SerializedName("OpeningBalance")
	val openingBalance: String? = null,
)

data class BankBookDateListItem(

	@field:SerializedName("CreationDate")
	val creationDate: String? = null,

	@field:SerializedName("BranchID")
	val branchID: String? = null,

	@field:SerializedName("GroupID")
	val groupID: String? = null,

	@field:SerializedName("BranchName")
	val branchName: String? = null,

	@field:SerializedName("Debit")
	val debit: Double? = null,

	@field:SerializedName("Narration")
	val narration: String? = null,

	@field:SerializedName("LedgerID")
	val ledgerID: String? = null,

	@field:SerializedName("GroupName")
	val groupName: String? = null,

	@field:SerializedName("ParentGroupID")
	val parentGroupID: Any? = null,

	@field:SerializedName("VoucherID")
	val voucherID: String? = null,

	@field:SerializedName("openingBalance")
	val openingBalance: String? = null,

	@field:SerializedName("LedgerName")
	val ledgerName: String? = null,

	@field:SerializedName("ClosingBalance")
	val closingBalance: String? = null,

	@field:SerializedName("VoucherType")
	val voucherType: String? = null,

	@field:SerializedName("Credit")
	val credit: Double? = null
)

