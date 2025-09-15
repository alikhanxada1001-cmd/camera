package com.example.virtualcameraliveassist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.virtualcameraliveassist.MainViewModel
import com.example.virtualcameraliveassist.model.EncoderType

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
	val s = viewModel.settings.value
	val bitrate = remember { mutableStateOf(s.bitrateKbps.toString()) }
	val width = remember { mutableStateOf(s.width.toString()) }
	val height = remember { mutableStateOf(s.height.toString()) }
	val fps = remember { mutableStateOf(s.fps.toString()) }
	val storage = remember { mutableStateOf(s.storageDir) }
	val retention = remember { mutableStateOf(s.retentionFiles.toString()) }

	Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
		Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			OutlinedTextField(bitrate.value, { bitrate.value = it }, label = { Text("Bitrate kbps") }, modifier = Modifier.weight(1f))
			OutlinedTextField(fps.value, { fps.value = it }, label = { Text("FPS") }, modifier = Modifier.weight(1f))
		}
		Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			OutlinedTextField(width.value, { width.value = it }, label = { Text("Width") }, modifier = Modifier.weight(1f))
			OutlinedTextField(height.value, { height.value = it }, label = { Text("Height") }, modifier = Modifier.weight(1f))
		}
		SegmentedButtonRow {
			SegmentedButton(selected = s.encoder == EncoderType.Hardware, onClick = { viewModel.updateSettings { copy(encoder = EncoderType.Hardware) } }) { Text("Hardware") }
			SegmentedButton(selected = s.encoder == EncoderType.Software, onClick = { viewModel.updateSettings { copy(encoder = EncoderType.Software) } }) { Text("Software") }
		}
		Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			OutlinedTextField(storage.value, { storage.value = it }, label = { Text("Storage dir") }, modifier = Modifier.weight(1f))
			OutlinedTextField(retention.value, { retention.value = it }, label = { Text("Retention files") }, modifier = Modifier.weight(1f))
		}
		Button(onClick = {
			val kbps = bitrate.value.toIntOrNull() ?: s.bitrateKbps
			val w = width.value.toIntOrNull() ?: s.width
			val h = height.value.toIntOrNull() ?: s.height
			val f = fps.value.toIntOrNull() ?: s.fps
			val r = retention.value.toIntOrNull() ?: s.retentionFiles
			viewModel.updateSettings { copy(bitrateKbps = kbps, width = w, height = h, fps = f, storageDir = storage.value, retentionFiles = r) }
		}) { Text("Save Settings") }
	}
}
