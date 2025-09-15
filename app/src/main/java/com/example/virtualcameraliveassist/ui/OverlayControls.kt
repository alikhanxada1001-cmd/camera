package com.example.virtualcameraliveassist.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.virtualcameraliveassist.MainViewModel
import com.example.virtualcameraliveassist.model.Source

@Composable
fun OverlayControls(viewModel: MainViewModel) {
	Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
		val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
			viewModel.setLogo(uri)
		}
		Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			Button(onClick = { imagePicker.launch("image/*") }) { Text("Pick Logo") }
			Slider(value = viewModel.overlay.value.logoOpacity, onValueChange = { viewModel.setLogoOpacity(it) }, valueRange = 0f..1f)
		}

		val textState = remember { mutableStateOf(viewModel.overlay.value.text) }
		Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			OutlinedTextField(value = textState.value, onValueChange = { textState.value = it }, label = { Text("Text overlay") }, modifier = Modifier.weight(1f))
			Button(onClick = { viewModel.setText(textState.value) }) { Text("Set") }
		}

		Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			Checkbox(checked = viewModel.overlay.value.showTimestamp, onCheckedChange = { viewModel.setShowTimestamp(it) })
			Text("Show timestamp")
			Slider(value = viewModel.overlay.value.timestampOpacity, onValueChange = { viewModel.setTimestampOpacity(it) }, valueRange = 0f..1f)
		}

		Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			Checkbox(checked = viewModel.overlay.value.pip.enabled, onCheckedChange = { viewModel.setPipEnabled(it) })
			Text("Enable PiP")
		}
		Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			Button(onClick = { viewModel.setPipSource(Source.Camera) }) { Text("PiP: Camera") }
		}
	}
}
