package com.example.virtualcameraliveassist.model

enum class EncoderType { Hardware, Software }

data class SettingsState(
	val bitrateKbps: Int = 4000,
	val width: Int = 1280,
	val height: Int = 720,
	val fps: Int = 30,
	val encoder: EncoderType = EncoderType.Hardware,
	val storageDir: String = "Movies/VirtualCamera",
	val retentionFiles: Int = 20
)
