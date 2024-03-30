package com.example.bondoman.api

import com.example.bondoman.models.UploadResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadRequest {
    @Multipart
    @POST("/api/bill/upload")
    suspend fun uploadImage(
        @Part partFile: MultipartBody.Part, @Header("Authorization") authHeader: String
    ): Response<UploadResponse>
}