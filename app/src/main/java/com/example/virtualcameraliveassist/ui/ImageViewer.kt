package com.example.virtualcameraliveassist.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImageViewer(uri: Uri) {
	Image(
		painter = rememberAsyncImagePainter(model = uri),
		contentDescription = null,
		modifier = Modifier.fillMaxSize(),
		contentScale = ContentScale.Fit
	)
}
