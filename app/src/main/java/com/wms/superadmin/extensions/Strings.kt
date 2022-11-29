package com.wms.superadmin.extensions

import android.os.Environment
import android.util.Patterns
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*
import java.util.regex.Pattern
import kotlin.Exception

//Minimum 8 characters, at least one uppercase letter, one lowercase letter, one number and one special character
private val PASSWORD_PATTERN =
    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[`~#@$!%^*?&()+-_=<>,./';:{}|])[A-Za-z\\d`~#@$!%^*?&()+-_=<>,./';:{}|]{8,}$"

fun String.isEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isPassword(): Boolean {
    return Pattern.compile(PASSWORD_PATTERN).matcher(this).matches()
}

fun String.isJSONObject():Boolean{
    return try {
        JSONObject(this)
        true
    }catch (e:Exception){
        false
    }
}

public object  ReportTypes {
    const val  BANKBOOK = "BankBook"
    const val CASHBOOK = "CashBook"
    const val PAYBLES = "OutstandingPayables"
    const val RECEIVABLES = "OutstandingRecievaibles"

    const val SALESORDER = "SalesOrder"
    const val SALESREGISTER = "SalesRegister"
    const val PURCHASEORDER = "PurchaseOrder"
    const val PURCHASEREGISTER = "PurchaseRegister"
}


public object  ScreenNames {
    const val  TXN_ALL_BRANCH = "OutstandingBranch"
    const val  TXN_ALL_BRANCH_AGING = "Branchwiseaging"
    const val  TXN_VOUCHER = "OutstandingVoucher"
    const val  TXN_VOUCHER_AGING = "VoucherAgeing"
    const val  SR_BRANCHWISE = "Branch"
    const val  SR_WAREHOUSEWISE = "Warehouse"
    const val  SR_GROUPWISE = "Group"
    const val  SR_SUBCATWISE = "Category"
    const val  SR_ITEMWISE = "Itemwise"
    const val  SR_MONTHWISE = "Month"
    const val  SR_VOUCHERWISE = "Voucher"

}
public object  MenuNames {
    const val  Stock = "Stocks"
    const val  SalesRegister = "Sales Register"
    const val  SalesOrder = "SalesOrder"
    const val  PurchaseOrder = "Purchase Order"
    const val  PurchaseRegister = "Purchase Register"
    const val  Payable = "Payables"
    const val  Receivables = "Receivables"
    const val  CaskBook = "Cash In Hand"
    const val  BankBook = "Bank Account"
    const val  Notification = "Notification"
}


public object  IntentParams {
    const val  SCREEN_NAME = "ScreenName"
    const val  TRANSACTION_REQUEST = "TransactionRequest"
    const val  STOCKREPORT_REQUEST = "StockReportRequest"
    const val  REPORT_TYPE = "ReportType"
    const val  FROM_DATE = "FromDate"
    const val  To_DATE = "ToDate"
    const val  BRANCH_ID = "BranchId"
    const val ORDERSCREEN = "orderscreen"
    const val REGISTERSCREEN = "registerscreen"
    const val ITEM_NAME = "ItemName"
    const val PERIOD = "Period"
}

public object  Sessions {
    const val  FIRST_HALF = "First Half"
    const val SECOND_HALF = "Second half"
}

fun appendLog(text: String?) {
    try {
        val logFolder= File(Environment.getExternalStorageDirectory(),"Manager")
        val logFile=File(logFolder,"logFile.txt")
        if (!logFolder.exists())
            logFolder.mkdir()
        if(!logFile.exists())
            logFile.createNewFile()
        val buf = BufferedWriter(FileWriter(logFile, true))
        buf.append("${Date()} : $text")
        buf.newLine()
        buf.close()
    } catch (e: IOException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    }
}