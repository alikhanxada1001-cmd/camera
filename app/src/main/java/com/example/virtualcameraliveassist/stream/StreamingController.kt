package com.example.virtualcameraliveassist.stream

import android.content.Context
import android.os.Environment
import android.util.Log
import com.pedro.rtplibrary.rtmp.RtmpCamera2
import com.pedro.encoder.input.video.CameraOpenException
import com.pedro.rtplibrary.view.OpenGlView
import net.ossrs.rtmp.ConnectCheckerRtmp
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StreamingController(
	private val context: Context,
	private val openGlView: OpenGlView
) : ConnectCheckerRtmp {
	private val rtmpCamera2 = RtmpCamera2(openGlView, this)
	private var currentRecordPath: String? = null

	fun isStreaming(): Boolean = rtmpCamera2.isStreaming
	fun isRecording(): Boolean = rtmpCamera2.isRecording

	fun startPreview() {
		if (!rtmpCamera2.isOnPreview) {
			try {
				rtmpCamera2.startPreview()
			} catch (e: CameraOpenException) {
				Log.e("StreamingController", "Preview start failed", e)
			}
		}
	}

	fun stopPreview() {
		if (rtmpCamera2.isOnPreview) rtmpCamera2.stopPreview()
	}

	fun startStream(url: String, width: Int = 1280, height: Int = 720, fps: Int = 30, bitrate: Int = 4_000_000) {
		if (rtmpCamera2.isStreaming) return
		if (rtmpCamera2.prepareAudio() && rtmpCamera2.prepareVideo(width, height, fps, bitrate, false, 90)) {
			rtmpCamera2.startStream(url)
		} else {
			Log.e("StreamingController", "Prepare audio/video failed")
		}
	}

	fun stopStream() {
		if (rtmpCamera2.isStreaming) rtmpCamera2.stopStream()
	}

	fun startRecord(width: Int = 1280, height: Int = 720, fps: Int = 30, bitrate: Int = 4_000_000, storageDir: String = "Movies/VirtualCamera", retention: Int = 20) {
		if (rtmpCamera2.isRecording) return
		if (!rtmpCamera2.isOnPreview) {
			rtmpCamera2.startPreview()
		}
		if (!rtmpCamera2.isStreaming) {
			rtmpCamera2.prepareAudio()
			rtmpCamera2.prepareVideo(width, height, fps, bitrate, false, 90)
		}
		val path = generateOutputFilePath(storageDir)
		currentRecordPath = path
		rtmpCamera2.startRecord(path)
		applyRetention(storageDir, retention)
	}

	fun stopRecord(): String? {
		if (rtmpCamera2.isRecording) rtmpCamera2.stopRecord()
		return currentRecordPath
	}

	private fun generateOutputFilePath(storage: String): String {
		val base = if (storage.startsWith("/")) File(storage) else File(Environment.getExternalStorageDirectory(), storage)
		if (!base.exists()) base.mkdirs()
		val name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date()) + ".mp4"
		return File(base, name).absolutePath
	}

	private fun applyRetention(storage: String, retention: Int) {
		val base = if (storage.startsWith("/")) File(storage) else File(Environment.getExternalStorageDirectory(), storage)
		if (!base.exists()) return
		val files = base.listFiles { f -> f.extension.equals("mp4", ignoreCase = true) }?.sortedByDescending { it.lastModified() } ?: return
		files.drop(retention).forEach { it.delete() }
	}

	override fun onConnectionStartedRtmp(rtmpUrl: String) { Log.i("RTMP", "Connection started: $rtmpUrl") }
	override fun onConnectionSuccessRtmp() { Log.i("RTMP", "Connection success") }
	override fun onConnectionFailedRtmp(reason: String) { Log.e("RTMP", "Connection failed: $reason"); stopStream() }
	override fun onNewBitrateRtmp(bitrate: Long) {}
	override fun onDisconnectRtmp() { Log.i("RTMP", "Disconnected") }
	override fun onAuthErrorRtmp() { Log.e("RTMP", "Auth error") }
	override fun onAuthSuccessRtmp() { Log.i("RTMP", "Auth success") }
}
