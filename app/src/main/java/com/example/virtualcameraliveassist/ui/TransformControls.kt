package com.example.virtualcameraliveassist.ui

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Flip
import androidx.compose.material3.icons.filled.Rotate90DegreesCcw
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.virtualcameraliveassist.MainViewModel
import kotlin.math.PI

@Composable
fun TransformControls(viewModel: MainViewModel) {
	Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
		Button(onClick = { viewModel.resetTransform() }) { Text("Reset") }
		IconButton(onClick = { viewModel.toggleFlipH() }) { Icon(Icons.Default.Flip, contentDescription = "Flip H") }
		IconButton(onClick = { viewModel.toggleFlipV() }) { Icon(Icons.Default.Flip, contentDescription = "Flip V") }
		IconButton(onClick = { viewModel.setRotation(90f) }) { Icon(Icons.Default.Rotate90DegreesCcw, contentDescription = "Rotate 90") }
	}

	val scaleState = remember { mutableStateOf(1f) }
	Slider(value = scaleState.value, onValueChange = {
		scaleState.value = it
		viewModel.setScale(it)
	}, valueRange = 0.2f..4f)
}

fun Modifier.transformGestures(viewModel: MainViewModel): Modifier = this.pointerInput(Unit) {
	detectTransformGestures { _, pan, zoom, rotation ->
		viewModel.setScale((viewModel.transform.value.scale * zoom).coerceIn(0.2f, 8f))
		val current = viewModel.transform.value.translation
		viewModel.setTranslation(Offset(current.x + pan.x, current.y + pan.y))
		viewModel.setRotation(viewModel.transform.value.rotationDegrees + rotation * 180f / Math.PI.toFloat())
	}
}
