package com.example.virtualcameraliveassist.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.virtualcameraliveassist.R

class StreamingService : Service() {
	companion object {
		const val CHANNEL_ID = "streaming_channel"
		const val NOTIF_ID = 1001
		const val ACTION_START = "com.example.virtualcameraliveassist.action.START"
		const val ACTION_STOP = "com.example.virtualcameraliveassist.action.STOP"
	}

	private var wakeLock: PowerManager.WakeLock? = null
	private var wifiLock: WifiManager.WifiLock? = null

	override fun onCreate() {
		super.onCreate()
		createChannel()
		acquireLocks()
		startForeground(NOTIF_ID, buildNotification("Ready"))
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		when (intent?.action) {
			ACTION_START -> updateNotification("Streaming/Recording active")
			ACTION_STOP -> {
				updateNotification("Stopped")
				stopSelf()
			}
		}
		return START_STICKY
	}

	private fun buildNotification(text: String): Notification {
		return NotificationCompat.Builder(this, CHANNEL_ID)
			.setSmallIcon(R.drawable.ic_notification)
			.setContentTitle("Virtual Camera: Live Assist")
			.setContentText(text)
			.setOngoing(true)
			.build()
	}

	private fun updateNotification(text: String) {
		val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		nm.notify(NOTIF_ID, buildNotification(text))
	}

	private fun createChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			val channel = NotificationChannel(CHANNEL_ID, "Streaming", NotificationManager.IMPORTANCE_LOW)
			nm.createNotificationChannel(channel)
		}
	}

	private fun acquireLocks() {
		val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "VCLA:Wake").apply { acquire() }
		val wm = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
		wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "VCLA:Wifi").apply { acquire() }
	}

	private fun releaseLocks() {
		wakeLock?.let { if (it.isHeld) it.release() }
		wifiLock?.let { if (it.isHeld) it.release() }
	}

	override fun onDestroy() {
		releaseLocks()
		super.onDestroy()
	}

	override fun onBind(intent: Intent?): IBinder? = null
}
