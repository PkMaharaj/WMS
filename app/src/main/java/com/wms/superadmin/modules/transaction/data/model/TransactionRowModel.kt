package com.wms.superadmin.modules.transaction.data.model


import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import kotlin.String

data class TransactionRowModel(

  var BranchName: String? = MyApp.getInstance().resources.getString(R.string.lbl_branch1),
  var BranchId: String? = "0",
  var BillAmount: String? = "0",
  var TotalBalance: String? = "0",
  var AgingBalance1: String? = "0",
  var AgingBalance2: String? = "0"
)
