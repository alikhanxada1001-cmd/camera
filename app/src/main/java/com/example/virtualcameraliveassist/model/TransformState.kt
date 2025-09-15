package com.example.virtualcameraliveassist.model

import androidx.compose.ui.geometry.Offset

data class TransformState(
	val scale: Float = 1f,
	val rotationDegrees: Float = 0f,
	val translation: Offset = Offset.Zero,
	val flipHorizontal: Boolean = false,
	val flipVertical: Boolean = false
)
