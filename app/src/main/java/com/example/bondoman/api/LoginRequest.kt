package com.example.bondoman.api

import com.example.bondoman.models.Credential
import com.example.bondoman.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRequest {
    @POST("/api/auth/login")
    suspend fun login(@Body credential: Credential): Response<LoginResponse>
}