package com.example.githubuserappbagas.retrofit

import com.example.githubuserappbagas.data.api.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//token baru
//token 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
//token 3
//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

//token pertama
//token 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
class ApiConfig {
    companion object{
        fun getApiService(): ApiService {
            val authInterceptor = Interceptor { chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader("Authorization", "Bearer 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")
                    .build()
                chain.proceed(requestHeaders)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }

}