package com.example.bondoman.models

data class ResItems(
    val name: String,
    val qty: Int,
    val price: Float
)

data class UploadResItem(
    val items: Array<ResItems>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UploadResItem

        return items.contentEquals(other.items)
    }

    override fun hashCode(): Int {
        return items.contentHashCode()
    }
}

data class UploadResponse(
    val items: UploadResItem
)
