package com.wms.superadmin.modules.transaction.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp


data class BranchTXNRowModel(

    var BranchName: String? = MyApp.getInstance().resources.getString(R.string.lbl_branch1),

    var BillDate: String? = "",

    var BillAmount: String? = "0",

    var TotalBalance: String? = "0",

    var PartyName: String? = "",

    var BillDueDays: String? = "0",
    var BillDueDate: String? = "0",
    var VoucherType: String? = "0",
    var GroupName: String? = "0",

    var ReferenceNo: String? = "",

    var Aging1: String? = "",

    var Aging2: String? = "",
    var Details: String? = "",
    )