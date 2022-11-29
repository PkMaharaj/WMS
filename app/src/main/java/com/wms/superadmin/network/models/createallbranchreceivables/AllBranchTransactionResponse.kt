package com.wms.superadmin.network.models.createallbranchreceivables

import com.google.gson.annotations.SerializedName

data class CreateAllBranchReceivablesResponse(
	@field:SerializedName("OutputDataListObject")
	val outputDataListObject: List<AllBranchTransactionItem?>? = null
)

data class AllBranchTransactionItem(
	@field:SerializedName("BranchID")
	val branchID: String? = null,

	@field:SerializedName("GroupName")
	val groupName: String? = null,

	@field:SerializedName("CreationDate")
	val creationDate: String? = null,

	@field:SerializedName("BranchName")
	val branchName: String? = null,

	@field:SerializedName("BillAmount")
	val billAmount: String? = "0",

	@field:SerializedName("OutstandingBalance")
	val outstandingBalance: String? = "0",

	@field:SerializedName("outstandingBalance1")
	val outstandingBalance1: String? = null,

	@field:SerializedName("outstandingBalance2")
	val outstandingBalance2: String? = null,

	@field:SerializedName("DueDays")
	val dueDays: String? = null,

	@field:SerializedName("BillNo")
	val billNo: String? = null,

	@field:SerializedName("LedgerName")
	val ledgerName: String? = null,

	@field:SerializedName("DueDate")
	val dueDate: String? = null,

	@field:SerializedName("VoucherType")
	val voucherType: String? = null,

	@field:SerializedName("MergedDetails")
	val mergedDetails: String? = null,
)

