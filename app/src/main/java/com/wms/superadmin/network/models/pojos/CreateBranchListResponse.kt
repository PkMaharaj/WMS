package com.wms.superadmin.network.models.pojos

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateBranchListResponse(

	@field:SerializedName("Status")
	val status: Boolean? = null,

	@field:SerializedName("branchList")
	val branchList: List<BranchListItem?>? = null,

	@field:SerializedName("warehouseList")
	val warehouselist: List<WarehouseItem?>? = null,

	@field:SerializedName("SubcatList")
	val catSubcatList: List<CatSubCatItem?>? = null,

	@field:SerializedName("Error")
	val error: Any? = null
)

data class BranchListItem(

	@field:SerializedName("OrgID")
	val orgID: String? = null,

	@field:SerializedName("BranchID")
	val branchID: String? = null,

	@field:SerializedName("BranchName")
	val branchName: String? = null
)
data class WarehouseItem(

	@field:SerializedName("OrgID")
	val orgID: String? = null,

	@field:SerializedName("WarehouseID")
	val warehouseID: Int? = null,

	@field:SerializedName("BranchID")
	val branchID: String? = null,

	@field:SerializedName("WarehouseName")
	val warehouseName: String? = null, ): Serializable

data class CatSubCatItem(
	@field:SerializedName("OrgID")
	val orgID: String? = "0",

	@field:SerializedName("BranchID")
	val branchID: String? = "0",

	@field:SerializedName("CategoryName")
	val categoryName : String? = "a",

	@field:SerializedName("CategoryID")
	val categoryID : Int? = 0,

	@field:SerializedName("SubCategoryName")
	val subCategoryName  : String? = "a",

	@field:SerializedName("SubCategoryID")
	val subCategoryID  : Int? = 0,
	): Serializable