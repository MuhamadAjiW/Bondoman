package com.example.bondoman.ui.hub.scan

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.R
import com.example.bondoman.api.RetrofitClient
import com.example.bondoman.database.AppDatabase
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.database.repository.TransactionRepository
import com.example.bondoman.databinding.FragmentScanBinding
import com.example.bondoman.viewmodel.scan.ScanViewModel
import com.example.bondoman.viewmodel.scan.ScanViewModelFactory
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanFragment : Fragment() {
    // TODO: Added preview for transaction response data from server
    private lateinit var binding: FragmentScanBinding
    private lateinit var scanViewModel: ScanViewModel

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanBinding.inflate(inflater, container, false)

        val database = AppDatabase.getInstance(requireActivity())
        val transactionRepo = TransactionRepository(database.transactionDao)
        val scanViewModelFactory = ScanViewModelFactory(transactionRepo)
        scanViewModel = ViewModelProvider(this, scanViewModelFactory)[ScanViewModel::class.java]

        binding.imageCaptureButton.setOnClickListener {
            takePhoto()
        }
        binding.selectPhotoButton.setOnClickListener {
            selectPhoto()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val header = requireActivity().findViewById<TextView>(R.id.nav_title)
            header.text = getString(R.string.hub_nav_scan)

            startCamera()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            val image = data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, image)

            uploadImage(bitmap)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireActivity()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onCaptureSuccess(image: ImageProxy) {
                    uploadImage(image.toBitmap())
                }
            }
        )
    }

    private fun uploadImage(bitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.WEBP, 80, stream)
        val byteArray = stream.toByteArray()

        val imageReqBody = RequestBody.create(MediaType.parse("image/*"), byteArray)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            try {
                // TODO: Get stored auth token
                val authToken =
                    "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuaW0iOiIxMzUyMTE0OSIsImlhdCI6MTcxMTIyMzk3NSwiZXhwIjoxNzExMjI0Mjc1fQ.TLB_xIVzsoWntIf-sqrWTWSW75AhlFKPV9MZ6SxC-T8"
                val response = RetrofitClient.uploadInstance.uploadImage(
                    MultipartBody.Part.createFormData(
                        "file",
                        "test",
                        imageReqBody
                    ),
                    authToken
                )

                if (response.isSuccessful) {
                    for (item in response.body()!!.items.items) {
                        Log.d(TAG, item.name)
                        scanViewModel.insertUploaded(
                            TransactionEntity(
                                id = 0,
                                title = item.name,
                                // TODO: Category
                                category = "scanned",
                                amount = item.qty * item.price.toInt(),
                                // TODO: Location
                                location = "lokasi",
                                timestamp = SimpleDateFormat(
                                    "yyyy-MM-dd HH:mm:ss",
                                    Locale.getDefault()
                                ).format(
                                    Date()
                                )
                            )
                        )
                    }

                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.scan_add_toast_success),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }


    companion object {
        private const val TAG = "ScanFragment"
    }
}