package com.example.virtualcameraliveassist.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.virtualcameraliveassist.MainViewModel

@Composable
fun SourceSelector(viewModel: MainViewModel, modifier: Modifier = Modifier) {
	val pickVideoLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent()
	) { uri: Uri? ->
		uri?.let { viewModel.selectLocalVideo(it) }
	}

	val pickImageLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent()
	) { uri: Uri? ->
		uri?.let { viewModel.selectImage(it) }
	}

	val url = remember { mutableStateOf("") }

	Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
		Button(onClick = { viewModel.selectCamera() }) { Text("Camera") }
		Button(onClick = { pickVideoLauncher.launch("video/*") }) { Text("Local Video") }
		Button(onClick = { pickImageLauncher.launch("image/*") }) { Text("Image") }
	}
	Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
		OutlinedTextField(value = url.value, onValueChange = { url.value = it }, label = { Text("RTSP/HLS/HTTP URL") }, modifier = Modifier.weight(1f))
		Button(onClick = { if (url.value.isNotBlank()) viewModel.selectNetwork(url.value.trim()) }) { Text("Play") }
	}
}
