package com.wms.superadmin.network.models.Login

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginResponse(

	@field:SerializedName("Email")
	val email: String? = null,

	@field:SerializedName("Address")
	val address: Any? = null,

	@field:SerializedName("FirmName")
	val firmName: Any? = null,

	@field:SerializedName("BranchID")
	val branchID: String? = null,

	@field:SerializedName("Mobile")
	val mobile: String? = null,

	@field:SerializedName("RoleID")
	val roleID: Int? = null,

	@field:SerializedName("OrgID")
	val orgID: String? = null,

	@field:SerializedName("RoleName")
	val roleName: String? = null,

	@field:SerializedName("Username")
	val username: String? = null,

	@field:SerializedName("CustID")
	val custID: Any? = null,

	@field:SerializedName("UserID")
	val userID: Int? = null,

	@field:SerializedName("WareHouseName")
	val wareHouseName: Any? = null,

	@field:SerializedName("Password")
	val password: String? = null,

	@field:SerializedName("lstOfAccessRightsDetails")
	val lstofAccessRights: List<AccessRightItem>? = null,

	@field:SerializedName("ScreenSettings")
	var screenSettings: ScreenAccessibility= ScreenAccessibility(),
):Serializable

data class ScreenAccessibility(

	@field:SerializedName("SalesOrder")
	var sales_order: Boolean = false,

	@field:SerializedName("SalesRegister")
	var sales_register: Boolean = false,

	@field:SerializedName("PurchaseOrder")
	var purchase_order: Boolean = false,

	@field:SerializedName("PurchaseRegister")
	var purchase_register: Boolean= false,

	@field:SerializedName("CaskBook")
	var caskBook: Boolean= false,

	@field:SerializedName("BankBook")
	var bankBook: Boolean = false,

	@field:SerializedName("Payable")
	var payable: Boolean= false,

	@field:SerializedName("Receivable")
	var receivable: Boolean= false,

	@field:SerializedName("Notification")
	var notification: Boolean= false,

	@field:SerializedName("Stocks")
	var stocks: Boolean = false
):Serializable

data class AccessRightItem(

	@field:SerializedName("Activity")
	val activity: Boolean? = false,

	@field:SerializedName("SubMenuName")
	val subMenuName: String? = null)

