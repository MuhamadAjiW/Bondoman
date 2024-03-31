package com.example.bondoman.api

import com.example.bondoman.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthRequest {
    @POST("/api/auth/token")
    suspend fun authToken(@Header("Authorization") authHeader: String): Response<LoginResponse>
}