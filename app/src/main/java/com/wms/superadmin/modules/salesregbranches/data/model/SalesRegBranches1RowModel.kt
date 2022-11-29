package com.wms.superadmin.modules.salesregbranches.data.model

import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import java.math.BigDecimal
import kotlin.String

data class SalesRegBranches1RowModel(
    var txtBrachnameBranch: String? = MyApp.getInstance().resources.getString(R.string.lbl_hubli),
    var txtDebitBranch: String = "",
    var txtCreditBranch: String = "",
    var txtNetsalesBranch: String? = "",
    var branchid: Int? = 0,
    var branchname: String? = ""
)
