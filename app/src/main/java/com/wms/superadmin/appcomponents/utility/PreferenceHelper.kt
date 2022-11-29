package com.wms.superadmin.appcomponents.utility

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.wms.superadmin.network.models.pojos.BranchListItem
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.di.MyApp
import com.wms.superadmin.network.models.pojos.CatSubCatItem
import com.wms.superadmin.network.models.pojos.WarehouseItem
import kotlin.Int
import kotlin.String
import kotlin.Unit

public class PreferenceHelper {
  private val masterKeyAlias: String = createGetMasterKey()

  public val sharedPreference: SharedPreferences = EncryptedSharedPreferences.create(
                                      MyApp.getInstance().resources.getString(R.string.app_name),
                                      masterKeyAlias,
                                      MyApp.getInstance().applicationContext,
                                      EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
      EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
  )

  private fun createGetMasterKey(): String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val loginEditor=sharedPreference.edit()
  public fun setAccess_token(paramValue: String?): Unit {
    with(sharedPreference.edit()) {
                this.putString("access_token", paramValue!!)
                apply()
                }
  }

  public fun getAccess_token(): String? {
    with(sharedPreference) {
                return getString("access_token", null)
                }
    return null
  }

  public fun setToken_type(paramValue: String?): Unit {
    with(sharedPreference.edit()) {
                this.putString("token_type", paramValue!!)
                apply()
                }
  }

  public fun getToken_type(): String? {
    with(sharedPreference) {
                return getString("token_type", null)
                }
    return null
  }

  public fun setExpires_in(paramValue: Int?): Unit {
    with(sharedPreference.edit()) {
                this.putInt("expires_in", paramValue!!)
                apply()
                }
  }

  public fun getExpires_in(): Int? {
    with(sharedPreference) {
                return getInt("expires_in", 0)
                }
    return null
  }

  public fun setCustID(paramValue: Int?): Unit {
    with(sharedPreference.edit()) {
            this.putInt("CustID", paramValue!!)
            apply()
            }
  }

  public fun getCustID(): Int? {
    with(sharedPreference) {
            return getInt("CustID", 0)
            }
    return null
  }

  public fun setUserid(paramValue: Int?): Unit {
    with(sharedPreference.edit()) {
    this.putInt("userid", paramValue!!)
    apply()
    }
  }

  public fun getUserid(): Int? {
    with(sharedPreference) {
    return getInt("userid", 0)
    }
  }
    public fun setBookType(paramValue: String?): Unit {
        with(sharedPreference.edit()) {
            this.putString("bookid", paramValue!!)
            apply()
        }
    }

    public fun getBookType(): String? {
        with(sharedPreference) {
            return getString("bookid", null)
        }
    }


  public fun setMobile(paramValue: String?): Unit {
    with(sharedPreference.edit()) {
    this.putString("mobile", paramValue!!)
    apply()
    }
  }

  public fun getMobile(): String? {
    with(sharedPreference) {
    return getString("mobile", null)
    }
  }

  public fun setPassword(paramValue: String?): Unit {
    with(sharedPreference.edit()) {
    this.putString("password", paramValue!!)
    apply()
    }
  }

  public fun getPassword(): String? {
    with(sharedPreference) {
    return getString("password", null)
    }
  }

  public fun setCustid(paramValue: String?): Unit {
    with(sharedPreference.edit()) {
    this.putString("custid", paramValue!!)
    apply()
    }
  }

  public fun getCustid(): String? {
    with(sharedPreference) {
    return getString("custid", null)
    }
  }

  public fun setOrgID(paramValue: String?): Unit {
    with(sharedPreference.edit()) {
    this.putString("OrgID", paramValue!!)
    apply()
    }
  }

  public fun getOrgID(): String? {
    with(sharedPreference) {
    return getString("OrgID", null)
    }
  }

    public fun setBranchID(paramValue: String?): Unit {
        with(sharedPreference.edit()) {
            this.putString("BranchID", paramValue?:"")
            apply()
        }
    }

    public fun getBranchID(): String? {
        with(sharedPreference) {
            return getString("BranchID", null)
        }
    }

    fun <T> setSADetails(`object`: T) {
        val jsonString = GsonBuilder().create().toJson(`object`)
        loginEditor.putString("SADetails", jsonString).apply()
    }
    inline fun <reified T> getSADetails(): T? {
        val value = sharedPreference.getString("SADetails", null)
        return GsonBuilder().create().fromJson(value, T::class.java)
    }
    public fun setCheckState(paramValue: Boolean?) {
        with(sharedPreference.edit()){
            this.putBoolean("chkstate",paramValue!!)
            apply()
        }
    }

    public fun getCheckState(): Boolean? {
        with(sharedPreference) {
            return getBoolean("chkstate", false)
        }
        return null
    }

    fun <T> setBranchlist(`object`: T) {
        val jsonString = GsonBuilder().create().toJson(`object`)
        loginEditor.putString("branchlist", jsonString).apply()
    }
    inline fun <reified T> getBranchlist(): T? {
        val value = sharedPreference.getString("branchlist", null)
        val typeToken = object : TypeToken<ArrayList<BranchListItem?>>(){}.type
        return GsonBuilder().create().fromJson(value, typeToken)
    }

    fun <T> setWarehouseList(`object`: T) {
        val jsonString = GsonBuilder().create().toJson(`object`)
        loginEditor.putString("warehouselist", jsonString).apply()
    }
    inline fun <reified T> getWarehouseList(): T? {
        val value = sharedPreference.getString("warehouselist", null)
        val typeToken = object : TypeToken<ArrayList<WarehouseItem?>>(){}.type
        return GsonBuilder().create().fromJson(value, typeToken)
    }

    fun <T> setCatList(`object`: T) {
        val jsonString = GsonBuilder().create().toJson(`object`)
        loginEditor.putString("catSubCatList", jsonString).apply()
    }
    inline fun <reified T> getCatList(): T? {
        val value = sharedPreference.getString("catSubCatList", null)
        val typeToken = object : TypeToken<ArrayList<CatSubCatItem?>>(){}.type
        return GsonBuilder().create().fromJson(value, typeToken)
    }
    public fun getaddresse(): String? {
        with(sharedPreference) {
            return getString("address", null)
        }
        return null
    }

    public fun setlongitude(paramValue: String?): Unit {
        with(sharedPreference.edit()) {
            this.putString("longitude", paramValue!!)
            apply()
        }
    }
    public fun getlongitude(): String? {
        with(sharedPreference) {
            return getString("longitude", null)
        }
        return null
    }

    public fun setlattitude(paramValue: String?): Unit {
        with(sharedPreference.edit()) {
            this.putString("lattitude", paramValue!!)
            apply()
        }
    }
    public fun getlattitude(): String? {
        with(sharedPreference) {
            return getString("lattitude", null)
        }
        return null
    }
    public fun setaddress(paramValue: String?): Unit {
        with(sharedPreference.edit()) {
            this.putString("address", paramValue!!)
            apply()
        }
    }

}
