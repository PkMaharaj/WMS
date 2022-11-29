package com.wms.superadmin.appcomponents.network

import com.wms.superadmin.BuildConfig
import com.wms.superadmin.network.BASE_URL
import com.wms.superadmin.network.RetrofitServices
import java.util.concurrent.TimeUnit
import kotlin.String
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

public class RetrofitProvider {
  private fun provideRetrofit(serviceBaseUrl: String, okHttpClient: OkHttpClient): Retrofit =
      Retrofit.Builder()
              .baseUrl(serviceBaseUrl)
              .client(okHttpClient)
              .addConverterFactory(GsonConverterFactory.create())
              .build()

  public fun getService(): RetrofitServices =
      provideRetrofit(BASE_URL,provideOkHttpClient()).create(RetrofitServices::class.java)

  private fun provideOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .callTimeout(5, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
    if (BuildConfig.DEBUG) {
                builder.addInterceptor(HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                })
            }
    return builder.build()
  }
}
