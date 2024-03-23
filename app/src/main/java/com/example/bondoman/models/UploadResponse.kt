package com.example.bondoman.models

data class ResItems(
    val name: String,
    val qty: Int,
    val price: Float
)

data class UploadResItem(
    val items: Array<ResItems>
)

data class UploadResponse(
    val items: UploadResItem
)
