package com.example.bondoman.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://pbd-backend-2024.vercel.app/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val uploadInstance: UploadRequest by lazy {
        retrofit.create(UploadRequest::class.java)
    }

    val loginInstance: LoginRequest by lazy {
        retrofit.create(LoginRequest::class.java)
    }

    val authInstance: AuthRequest by lazy {
        retrofit.create(AuthRequest::class.java)
    }
}