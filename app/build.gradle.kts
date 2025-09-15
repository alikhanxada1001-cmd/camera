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
		minSdk = 28
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		vectorDrawables.useSupportLibrary = true
	}

	buildTypes {
		release {
			isMinifyEnabled = true
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
		debug {
			isMinifyEnabled = false
		}
	}

	compileOptions {
		isCoreLibraryDesugaringEnabled = true
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	kotlinOptions {
		jvmTarget = "17"
		freeCompilerArgs += listOf("-Xjvm-default=all")
	}

	buildFeatures {
		compose = true
	}

	// composeOptions removed; Kotlin 2.0 uses the Compose Compiler Gradle plugin

	packaging {
		resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
	}
}

dependencies {
	implementation(platform("androidx.compose:compose-bom:2024.09.01"))
	implementation("androidx.core:core-ktx:1.13.1")
	implementation("androidx.activity:activity-compose:1.9.2")
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3:1.3.0")
	debugImplementation("androidx.compose.ui:ui-tooling")
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.5")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.5")
	implementation("androidx.navigation:navigation-compose:2.8.0")
	implementation("io.coil-kt:coil-compose:2.6.0")

	// CameraX
	val cameraxVersion = "1.3.4"
	implementation("androidx.camera:camera-core:$cameraxVersion")
	implementation("androidx.camera:camera-camera2:$cameraxVersion")
	implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
	implementation("androidx.camera:camera-view:$cameraxVersion")
	implementation("androidx.camera:camera-video:$cameraxVersion")

	// ExoPlayer
	implementation("androidx.media3:media3-exoplayer:1.4.1")
	implementation("androidx.media3:media3-ui:1.4.1")
	implementation("androidx.media3:media3-exoplayer-hls:1.4.1")
	implementation("androidx.media3:media3-exoplayer-rtsp:1.4.1")

	// Streaming/Recording (RTMP client with OpenGL)
	implementation("com.github.pedroSG94.rtmp-rtsp-stream-client-java:rtplibrary:2.4.1")

	// Coroutines & Flow
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

	// Accompanist for permissions
	implementation("com.google.accompanist:accompanist-permissions:0.36.0")

	// WorkManager (for stability/foreground later)
	implementation("androidx.work:work-runtime-ktx:2.9.1")

	// Desugaring for java.time
	coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.2")
}
