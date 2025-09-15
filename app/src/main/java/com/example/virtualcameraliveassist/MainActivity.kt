package com.example.virtualcameraliveassist

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.virtualcameraliveassist.ui.HomeScreen
import com.example.virtualcameraliveassist.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
	private val requestPermissions =
		registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			AppTheme {
				Surface(color = MaterialTheme.colorScheme.background) {
					val vm: MainViewModel = viewModel()
					requestPermissions.launch(arrayOf(
						Manifest.permission.CAMERA,
						Manifest.permission.RECORD_AUDIO
					))
					vm.startPrivacyMonitor(this)
					HomeScreen(viewModel = vm)
				}
			}
		}
	}
}
