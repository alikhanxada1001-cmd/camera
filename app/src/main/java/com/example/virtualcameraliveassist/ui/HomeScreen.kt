package com.example.virtualcameraliveassist.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.virtualcameraliveassist.MainViewModel
import com.example.virtualcameraliveassist.model.Source

@Composable
fun HomeScreen(viewModel: MainViewModel) {
	val showSettings = remember { mutableStateOf(false) }
	Column(Modifier.fillMaxSize().padding(12.dp)) {
		val camInUse by viewModel.cameraInUseByOthers.collectAsState()
		if (camInUse) {
			Text(
				"Warning: Another app may be using the camera",
				modifier = Modifier
					.fillMaxSize()
					.background(Color(0x66FF0000))
					.padding(8.dp),
				color = Color.White
			)
		}
		Button(onClick = { showSettings.value = !showSettings.value }) { Text(if (showSettings.value) "Close Settings" else "Open Settings") }
		if (showSettings.value) {
			SettingsScreen(viewModel)
			return@Column
		}
		SourceSelector(viewModel)
		TransformControls(viewModel)
		OverlayControls(viewModel)
		Divider(modifier = Modifier.padding(vertical = 8.dp))
		Box(Modifier.weight(1f)) {
			TransformedBox(viewModel = viewModel, modifier = Modifier.transformGestures(viewModel)) {
				val src by viewModel.selectedSource.collectAsState()
				when (val s = src) {
					is Source.Camera -> CameraPreviewScreen(viewModel)
					is Source.LocalVideo -> PlayerScreen(uri = s.uri)
					is Source.NetworkStream -> PlayerScreen(url = s.url)
					is Source.Image -> ImageViewer(uri = s.uri)
				}
			}
			OverlaysLayer(viewModel)
		}
		Divider(modifier = Modifier.padding(vertical = 8.dp))
		StreamControls(viewModel)
	}
}
