package com.example.virtualcameraliveassist.model

import android.net.Uri

sealed class Source {
	data object Camera : Source()
	data class LocalVideo(val uri: Uri) : Source()
	data class NetworkStream(val url: String) : Source()
	data class Image(val uri: Uri) : Source()
}
