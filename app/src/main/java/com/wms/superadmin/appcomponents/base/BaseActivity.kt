package com.wms.superadmin.appcomponents.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.wms.superadmin.network.models.pojos.BranchListItem

abstract class BaseActivity<T : ViewDataBinding>(@LayoutRes val layoutId: Int) :
    AppCompatActivity(), BaseControllerFunctionsImpl {
    lateinit var binding: T
    var BranchList= arrayListOf<BranchListItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@BaseActivity, layoutId) as T
        binding.lifecycleOwner = this
        addObservers()
        setUpClicks()
        onInitialized()
    }
}
