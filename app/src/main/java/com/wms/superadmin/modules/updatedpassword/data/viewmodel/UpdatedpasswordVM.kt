package com.wms.superadmin.modules.updatedpassword.`data`.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wms.superadmin.modules.updatedpassword.`data`.model.UpdatedpasswordModel

public class UpdatedpasswordVM : ViewModel() {
  public val updatedpasswordModel: MutableLiveData<UpdatedpasswordModel> =
      MutableLiveData(UpdatedpasswordModel())
}
