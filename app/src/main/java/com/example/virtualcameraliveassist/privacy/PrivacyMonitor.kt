package com.example.virtualcameraliveassist.privacy

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PrivacyMonitor(private val context: Context) {
	fun cameraAccessEvents(): Flow<Boolean> = callbackFlow {
		val cm = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
		val callback = object : CameraManager.AvailabilityCallback() {
			override fun onCameraAvailable(cameraId: String) {
				trySend(false)
			}

			override fun onCameraUnavailable(cameraId: String) {
				trySend(true)
			}
		}
		cm.registerAvailabilityCallback(context.mainExecutor, callback)
		awaitClose { cm.unregisterAvailabilityCallback(callback) }
	}
}
