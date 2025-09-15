package com.example.virtualcameraliveassist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import com.example.virtualcameraliveassist.MainViewModel
import kotlin.math.roundToInt

@Composable
fun TransformedBox(viewModel: MainViewModel, modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
	val t by viewModel.transform.collectAsState()
	Box(
		modifier = modifier
			.graphicsLayer {
				translationX = t.translation.x
				translationY = t.translation.y
				scaleX = t.scale * if (t.flipHorizontal) -1f else 1f
				scaleY = t.scale * if (t.flipVertical) -1f else 1f
				rotationZ = t.rotationDegrees
			},
		content = content
	)
}
