plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("org.jetbrains.kotlin.plugin.compose")
}

android {
	namespace = "com.example.virtualcameraliveassist"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.example.virtualcameraliveassist"
		minSdk = 24
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"
	}

	buildFeatures {
		compose = true
	}

	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.3"
	}
}

dependencies {
	implementation("androidx.core:core-ktx:1.13.1")
	implementation("androidx.activity:activity-compose:1.9.2")
	implementation("androidx.compose.ui:ui:1.7.0")
	implementation("androidx.compose.material3:material3:1.3.0")
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
}
