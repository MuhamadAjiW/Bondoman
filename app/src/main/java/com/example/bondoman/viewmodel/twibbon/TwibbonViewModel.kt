package com.example.bondoman.viewmodel.twibbon

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import androidx.camera.core.ImageProxy
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TwibbonViewModel : ViewModel() {
    val isCameraPermissionGranted = MutableLiveData(false)

    companion object {
        const val TWIBBON_WIDTH = 280
        const val TWIBBON_HEIGHT = 375
        const val TWIBBON_OFFSET_TOP = 300f
        const val TWIBBON_OFFSET_LEFT = 0F
    }

    fun saveImageWithOverlay(image: ImageProxy, twibbon: Bitmap, context: Context): Boolean {
        val imageBitmap = image.toBitmap().copy(Bitmap.Config.ARGB_8888, true)

        val matrix = Matrix()
        matrix.postRotate(image.imageInfo.rotationDegrees.toFloat())

        val newBitmap =
            Bitmap.createBitmap(imageBitmap, 0, 0, image.width, image.height, matrix, true)

        val canvas = Canvas(newBitmap)
        val twibbonResized = Bitmap.createScaledBitmap(
            twibbon, TWIBBON_WIDTH, TWIBBON_HEIGHT, true
        )

        canvas.drawBitmap(twibbonResized, TWIBBON_OFFSET_LEFT, TWIBBON_OFFSET_TOP, null)

        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(
            MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000
        )
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/bondoman")
        values.put(MediaStore.Images.Media.IS_PENDING, true)

        val uri: Uri? = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        if (uri != null) {
            val outputStream = context.contentResolver.openOutputStream(uri)
            if (outputStream != null) {
                try {
                    newBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                    return false
                }
            }
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(
                uri, values, null, null
            )
        }

        return true
    }
}