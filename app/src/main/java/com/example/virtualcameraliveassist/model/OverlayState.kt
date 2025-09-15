package com.example.virtualcameraliveassist.model

import android.net.Uri
import androidx.compose.ui.geometry.Offset

data class OverlayState(
	val logoUri: Uri? = null,
	val logoPosition: Offset = Offset(40f, 40f),
	val logoOpacity: Float = 1f,
	val text: String = "",
	val textPosition: Offset = Offset(40f, 120f),
	val textOpacity: Float = 1f,
	val showTimestamp: Boolean = false,
	val timestampPosition: Offset = Offset(40f, 200f),
	val timestampOpacity: Float = 1f,
	val pip: PipState = PipState()
)

data class PipState(
	val enabled: Boolean = false,
	val source: Source? = null,
	val position: Offset = Offset(40f, 280f),
	val widthDp: Float = 160f,
	val heightDp: Float = 90f,
	val opacity: Float = 1f
)
