package com.example.virtualcameraliveassist.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.virtualcameraliveassist.MainViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun CameraPreviewScreen(viewModel: MainViewModel) {
	val context = LocalContext.current
	val lifecycleOwner = LocalLifecycleOwner.current
	val cameraExecutor: ExecutorService = rememberExecutor()

	AndroidView(
		modifier = Modifier.fillMaxSize(),
		factory = { ctx ->
			PreviewView(ctx).apply {
				layoutParams = ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT
				)
				scaleType = PreviewView.ScaleType.FILL_CENTER
			}
		},
		update = { previewView ->
			val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
			cameraProviderFuture.addListener({
				try {
					val cameraProvider = cameraProviderFuture.get()
					val preview = Preview.Builder().build().also {
						it.setSurfaceProvider(previewView.surfaceProvider)
					}
					val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

					cameraProvider.unbindAll()
					cameraProvider.bindToLifecycle(
						lifecycleOwner,
						cameraSelector,
						preview
					)
					viewModel.setCameraReady(true)
				} catch (e: Exception) {
					Log.e("CameraPreview", "Binding failed", e)
				}
			}, ContextCompat.getMainExecutor(context))
		}
	)

	DisposableEffect(Unit) {
		onDispose {
			cameraExecutor.shutdown()
		}
	}
}

@Composable
private fun rememberExecutor(): ExecutorService {
	return Executors.newSingleThreadExecutor()
}
