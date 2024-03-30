package com.example.bondoman.ui.hub.twibbon

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
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
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.R
import com.example.bondoman.databinding.FragmentTwibbonBinding
import com.example.bondoman.viewmodel.twibbon.TwibbonViewModel
import com.example.bondoman.viewmodel.twibbon.TwibbonViewModelFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TwibbonFragment : Fragment() {
    private lateinit var binding: FragmentTwibbonBinding
    private lateinit var twibbonViewModel: TwibbonViewModel

    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            twibbonViewModel.isCameraPermissionGranted.value = true
        }
    }

    // Observe change in camera permission
    private val cameraPermissionObserver = Observer<Boolean> {
        if (it) {
            startCamera()
            binding.imageCaptureButton.visibility = View.VISIBLE
            binding.viewFinder.visibility = View.VISIBLE
            binding.ivTwibbon.visibility = View.VISIBLE

            binding.textEnableCamera.visibility = View.INVISIBLE
        } else {
            binding.imageCaptureButton.visibility = View.INVISIBLE
            binding.viewFinder.visibility = View.INVISIBLE
            binding.ivTwibbon.visibility = View.INVISIBLE

            binding.textEnableCamera.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val TAG = "TwibbonFragment"
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTwibbonBinding.inflate(inflater, container, false)

        val twibbonViewModelFactory = TwibbonViewModelFactory()
        twibbonViewModel =
            ViewModelProvider(this, twibbonViewModelFactory)[TwibbonViewModel::class.java]

        twibbonViewModel.isCameraPermissionGranted.observe(
            viewLifecycleOwner, cameraPermissionObserver
        )

        binding.imageCaptureButton.setOnClickListener {
            takePhoto()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val header = requireActivity().findViewById<TextView>(R.id.nav_title)
            header.text = getString(R.string.hub_nav_twibbon)

            // Check camera permission
            if (!checkCameraPermission()) {
                requestCameraPermission()
            } else {
                twibbonViewModel.isCameraPermissionGranted.value = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
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

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(ContextCompat.getMainExecutor(requireActivity()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onCaptureSuccess(image: ImageProxy) {
                    val twibbon = binding.ivTwibbon.drawable.toBitmap()
                    val success = twibbonViewModel.saveImageWithOverlay(
                        image, twibbon, requireActivity().baseContext
                    )

                    if (success) {
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.twibbon_add_toast_success),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireActivity(), getString(R.string.scan_add_toast_failed), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }
}