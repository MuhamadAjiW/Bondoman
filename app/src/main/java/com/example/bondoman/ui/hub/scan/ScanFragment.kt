package com.example.bondoman.ui.hub.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.R
import com.example.bondoman.database.AppDatabase
import com.example.bondoman.database.repository.TransactionRepository
import com.example.bondoman.databinding.FragmentScanBinding
import com.example.bondoman.viewmodel.scan.ScanViewModel
import com.example.bondoman.viewmodel.scan.ScanViewModelFactory
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanFragment : Fragment() {
    // TODO: Added preview for transaction response data from server
    private lateinit var binding: FragmentScanBinding
    private lateinit var scanViewModel: ScanViewModel

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    private val scanDialog: ScanDialogFragment = ScanDialogFragment()

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            scanViewModel.isCameraPermissionGranted.value = true
        }
    }

    // Observe change in camera permission
    private val cameraPermissionObserver = Observer<Boolean> {
        if (it) {
            startCamera()
            binding.imageCaptureButton.visibility = View.VISIBLE
            binding.selectPhotoButton.visibility = View.VISIBLE
            binding.viewFinder.visibility = View.VISIBLE

            binding.textEnableCamera.visibility = View.INVISIBLE
        } else {
            binding.imageCaptureButton.visibility = View.INVISIBLE
            binding.selectPhotoButton.visibility = View.INVISIBLE
            binding.viewFinder.visibility = View.INVISIBLE

            binding.textEnableCamera.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val TAG = "ScanFragment"
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanBinding.inflate(inflater, container, false)

        val database = AppDatabase.getInstance(requireActivity())
        val transactionRepo = TransactionRepository(database.transactionDao)
        val scanViewModelFactory = ScanViewModelFactory(transactionRepo)
        scanViewModel = ViewModelProvider(this, scanViewModelFactory)[ScanViewModel::class.java]

        scanViewModel.isCameraPermissionGranted.observe(
            viewLifecycleOwner, cameraPermissionObserver
        )

        binding.imageCaptureButton.setOnClickListener(::onImageCaptureClick)
        binding.selectPhotoButton.setOnClickListener(::onSelectPhotoClick)

        cameraExecutor = Executors.newSingleThreadExecutor()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val header = requireActivity().findViewById<TextView>(R.id.nav_title)
            header.text = getString(R.string.hub_nav_scan)

            // Check camera permission
            if (!checkCameraPermission()) {
                requestCameraPermission()
            } else {
                scanViewModel.isCameraPermissionGranted.value = true
            }
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

    private fun requestCameraPermission() {
        activityResultLauncher.launch(CAMERA_PERMISSION)
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity().baseContext, CAMERA_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireActivity())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview =
                Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9).build().also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture =
                ImageCapture.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9).build()

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

    private fun onImageCaptureClick(view: View) {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(ContextCompat.getMainExecutor(requireActivity()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onCaptureSuccess(image: ImageProxy) {
                    uploadImage(image.toBitmap())
                }
            })
    }

    private fun uploadImage(bitmap: Bitmap) {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.WEBP, 80, stream)
        val byteArray = stream.toByteArray()

        val imageReqBody = RequestBody.create(MediaType.parse("image/*"), byteArray)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val scannedTransactions = scanViewModel.uploadNota(imageReqBody)

            if (scannedTransactions.isNotEmpty()) {
                scanDialog.scannedTransactions.value = scannedTransactions
                scanDialog.show(parentFragmentManager, "scan")
            } else {
                Toast.makeText(
                    requireActivity(), getString(R.string.scan_add_toast_failed), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun onSelectPhotoClick(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }
}