package com.example.virtualcameraliveassist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.virtualcameraliveassist.MainViewModel
import com.example.virtualcameraliveassist.model.Source
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BoxScope.OverlaysLayer(viewModel: MainViewModel) {
	val overlay = viewModel.overlay.value
	val density = LocalDensity.current

	// Logo
	overlay.logoUri?.let { uri ->
		val pos = overlay.logoPosition
		Box(
			modifier = Modifier
				.offset(pos.x.dp, pos.y.dp)
				.alpha(overlay.logoOpacity)
				.pointerInput(Unit) {
					detectDragGestures { change, drag ->
						viewModel.setLogoPosition(Offset(pos.x + drag.x, pos.y + drag.y))
					}
				}
		) {
			Image(painter = rememberAsyncImagePainter(model = uri), contentDescription = null, modifier = Modifier.size(96.dp))
		}
	}

	// Text
	if (overlay.text.isNotEmpty()) {
		val pos = overlay.textPosition
		Text(
			text = overlay.text,
			modifier = Modifier
				.offset(pos.x.dp, pos.y.dp)
				.alpha(overlay.textOpacity)
				.pointerInput(Unit) {
					detectDragGestures { _, drag ->
						viewModel.setTextPosition(Offset(pos.x + drag.x, pos.y + drag.y))
					}
				},
			color = Color.White
		)
	}

	// Timestamp
	if (overlay.showTimestamp) {
		val pos = overlay.timestampPosition
		val now = remember { mutableStateOf(System.currentTimeMillis()) }
		Text(
			text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(now.value)),
			modifier = Modifier
				.offset(pos.x.dp, pos.y.dp)
				.alpha(overlay.timestampOpacity),
			color = Color.Yellow
		)
	}

	// PiP
	if (overlay.pip.enabled && overlay.pip.source != null) {
		val pip = overlay.pip
		val pos = pip.position
		Box(
			modifier = Modifier
				.offset(pos.x.dp, pos.y.dp)
				.size(pip.widthDp.dp, pip.heightDp.dp)
				.alpha(pip.opacity)
				.background(Color.Black.copy(alpha = 0.2f))
				.pointerInput(Unit) {
					detectDragGestures { _, drag ->
						viewModel.setPipPosition(Offset(pos.x + drag.x, pos.y + drag.y))
					}
				},
			contentAlignment = Alignment.Center
		) {
			when (val s = pip.source) {
				is Source.Camera -> CameraPreviewScreen(viewModel)
				is Source.LocalVideo -> PlayerScreen(uri = s.uri)
				is Source.NetworkStream -> PlayerScreen(url = s.url)
				is Source.Image -> ImageViewer(uri = s.uri)
				null -> {}
			}
		}
	}
}
