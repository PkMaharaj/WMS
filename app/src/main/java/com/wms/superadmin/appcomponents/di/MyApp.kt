package com.wms.superadmin.appcomponents.di

import android.app.Application
import com.wms.superadmin.appcomponents.network.RetrofitProvider
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.network.repository.NetworkRepository
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

public class MyApp : Application() {
  private fun prepareNetworkModules(): List<Module> {
    val repoModules =
    module{
    single{
    	NetworkRepository()
    }
    }
    val serviceModules =
    module{
    single{
    RetrofitProvider().getService()
    }
    }
    return repoModules + serviceModules
  }

  private fun preferenceModule(): Module {
    val prefsModule = module {
                single {
                    PreferenceHelper()
                }
            }
    return prefsModule
  }

  private fun getKoinModules(): MutableList<Module> {
    val koinModules = mutableListOf<Module>()
    koinModules.add(preferenceModule())
    koinModules.addAll(prepareNetworkModules())
    return koinModules
  }

  public override fun onCreate(): Unit {
    super.onCreate()
    instance = this
    startKoin {
                androidLogger()
                androidContext(this@MyApp)
                loadKoinModules(getKoinModules())
            }
  }

  public companion object {
    private lateinit var instance: MyApp

    public fun getInstance(): MyApp = instance
  }
}
