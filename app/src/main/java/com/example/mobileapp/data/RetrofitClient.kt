package com.example.mobileapp.data

import SupabaseApi
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:54321/rest/v1/"

    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request()
                Log.d("Retrofit", "Запрос: ${request.url}")
                Log.d("Retrofit", "Заголовки: ${request.headers}")
                chain.proceed(request)
            }
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: SupabaseApi by lazy {
        retrofit.create(SupabaseApi::class.java)
    }
    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

}