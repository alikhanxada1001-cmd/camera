package com.example.virtualcameraliveassist.ui

import android.content.Intent
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.virtualcameraliveassist.MainViewModel
import com.example.virtualcameraliveassist.service.StreamingService
import com.example.virtualcameraliveassist.stream.StreamingController
import com.pedro.rtplibrary.view.OpenGlView

@Composable
fun StreamControls(viewModel: MainViewModel) {
	val urlState = remember { mutableStateOf(viewModel.rtmpUrl.value) }
	val controllerState = remember { mutableStateOf<StreamingController?>(null) }
	val context = LocalContext.current
	val s = viewModel.settings.value
	Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
		Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			OutlinedTextField(value = urlState.value, onValueChange = { urlState.value = it }, label = { Text("RTMP URL") }, modifier = Modifier.weight(1f))
			Button(onClick = { viewModel.setRtmpUrl(urlState.value) }) { Text("Set URL") }
		}

		Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			Button(onClick = {
				controllerState.value?.let {
					if (!it.isStreaming()) {
						context.startForegroundService(Intent(context, StreamingService::class.java).setAction(StreamingService.ACTION_START))
						it.startStream(viewModel.rtmpUrl.value, s.width, s.height, s.fps, s.bitrateKbps * 1000)
					}
				}
			}) { Text("Start Stream") }
			Button(onClick = {
				controllerState.value?.stopStream()
				context.startForegroundService(Intent(context, StreamingService::class.java).setAction(StreamingService.ACTION_STOP))
			}) { Text("Stop Stream") }
			Button(onClick = {
				controllerState.value?.startRecord(s.width, s.height, s.fps, s.bitrateKbps * 1000, s.storageDir, s.retentionFiles)
				context.startForegroundService(Intent(context, StreamingService::class.java).setAction(StreamingService.ACTION_START))
			}) { Text("Start Record") }
			Button(onClick = {
				controllerState.value?.stopRecord()
				context.startForegroundService(Intent(context, StreamingService::class.java).setAction(StreamingService.ACTION_STOP))
			}) { Text("Stop Record") }
		}

		AndroidView(factory = { ctx ->
			OpenGlView(ctx).apply {
				layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1)
			}
		}, update = { glView ->
			if (controllerState.value == null) {
				controllerState.value = StreamingController(glView.context, glView).also { it.startPreview() }
			}
		})
	}
}
