package com.example.virtualcameraliveassist.ui

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun PlayerScreen(uri: Uri? = null, url: String? = null) {
	val context = LocalContext.current
	val player = ExoPlayer.Builder(context).build()

	LaunchedEffect(uri, url) {
		val item = when {
			uri != null -> MediaItem.fromUri(uri)
			url != null -> MediaItem.fromUri(url)
			else -> null
		}
		player.playWhenReady = true
		player.repeatMode = ExoPlayer.REPEAT_MODE_ALL
		player.setMediaItem(item ?: return@LaunchedEffect)
		player.prepare()
	}

	AndroidView(
		modifier = Modifier.fillMaxSize(),
		factory = { ctx ->
			PlayerView(ctx).apply {
				this.player = player
			}
		}
	)

	DisposableEffect(Unit) {
		onDispose {
			player.release()
		}
	}
}
