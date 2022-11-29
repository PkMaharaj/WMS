package com.wms.superadmin.modules.stocksummary.stockreport.data.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wms.superadmin.modules.stocksummary.stockreport.data.model.StockreportModel
import com.wms.superadmin.network.models.pojos.BranchListItem
import com.wms.superadmin.network.models.pojos.CatSubCatItem
import com.wms.superadmin.network.models.pojos.WarehouseItem
import org.koin.core.KoinComponent

class StockreportVM : ViewModel(), KoinComponent {
  val stockreportModel: MutableLiveData<StockreportModel> = MutableLiveData(StockreportModel())

  var navArguments: Bundle? = null

  val spinnerBranchList: MutableLiveData<MutableList<BranchListItem>> = MutableLiveData()

  val spinnerWarehouseList: MutableLiveData<MutableList<WarehouseItem>> = MutableLiveData()

  val spinnerGroupList: MutableLiveData<MutableList<CatSubCatItem>> = MutableLiveData()

  val spinnerBtnprimary3List: MutableLiveData<MutableList<CatSubCatItem>> = MutableLiveData()

}
