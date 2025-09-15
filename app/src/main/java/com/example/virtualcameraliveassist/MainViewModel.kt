package com.example.virtualcameraliveassist

import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virtualcameraliveassist.model.Source
import com.example.virtualcameraliveassist.model.TransformState
import com.example.virtualcameraliveassist.model.OverlayState
import com.example.virtualcameraliveassist.model.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
	private val _cameraReady = MutableStateFlow(false)
	val cameraReady: StateFlow<Boolean> = _cameraReady

	private val _selectedSource = MutableStateFlow<Source>(Source.Camera)
	val selectedSource: StateFlow<Source> = _selectedSource

	private val _transform = MutableStateFlow(TransformState())
	val transform: StateFlow<TransformState> = _transform

	private val _overlay = MutableStateFlow(OverlayState())
	val overlay: StateFlow<OverlayState> = _overlay

	private val _settings = MutableStateFlow(SettingsState())
	val settings: StateFlow<SettingsState> = _settings

	private val _rtmpUrl = MutableStateFlow("")
	val rtmpUrl: StateFlow<String> = _rtmpUrl

	private val _isStreaming = MutableStateFlow(false)
	val isStreaming: StateFlow<Boolean> = _isStreaming

	private val _isRecording = MutableStateFlow(false)
	val isRecording: StateFlow<Boolean> = _isRecording

	private val _cameraInUseByOthers = MutableStateFlow(false)
	val cameraInUseByOthers: StateFlow<Boolean> = _cameraInUseByOthers

	fun setCameraReady(ready: Boolean) { _cameraReady.value = ready }

	fun selectCamera() { _selectedSource.value = Source.Camera }
	fun selectLocalVideo(uri: Uri) { _selectedSource.value = Source.LocalVideo(uri) }
	fun selectNetwork(url: String) { _selectedSource.value = Source.NetworkStream(url) }
	fun selectImage(uri: Uri) { _selectedSource.value = Source.Image(uri) }

	fun resetTransform() { _transform.value = TransformState() }
	fun setScale(scale: Float) { _transform.value = _transform.value.copy(scale = scale.coerceIn(0.2f, 8f)) }
	fun setRotation(deg: Float) { _transform.value = _transform.value.copy(rotationDegrees = deg) }
	fun setTranslation(offset: Offset) { _transform.value = _transform.value.copy(translation = offset) }
	fun toggleFlipH() { _transform.value = _transform.value.copy(flipHorizontal = !_transform.value.flipHorizontal) }
	fun toggleFlipV() { _transform.value = _transform.value.copy(flipVertical = !_transform.value.flipVertical) }

	fun setLogo(uri: Uri?) { _overlay.value = _overlay.value.copy(logoUri = uri) }
	fun setLogoPosition(pos: Offset) { _overlay.value = _overlay.value.copy(logoPosition = pos) }
	fun setLogoOpacity(alpha: Float) { _overlay.value = _overlay.value.copy(logoOpacity = alpha.coerceIn(0f,1f)) }
	fun setText(text: String) { _overlay.value = _overlay.value.copy(text = text) }
	fun setTextPosition(pos: Offset) { _overlay.value = _overlay.value.copy(textPosition = pos) }
	fun setTextOpacity(alpha: Float) { _overlay.value = _overlay.value.copy(textOpacity = alpha.coerceIn(0f,1f)) }
	fun setShowTimestamp(show: Boolean) { _overlay.value = _overlay.value.copy(showTimestamp = show) }
	fun setTimestampPosition(pos: Offset) { _overlay.value = _overlay.value.copy(timestampPosition = pos) }
	fun setTimestampOpacity(alpha: Float) { _overlay.value = _overlay.value.copy(timestampOpacity = alpha.coerceIn(0f,1f)) }
	fun setPipEnabled(enabled: Boolean) { _overlay.value = _overlay.value.copy(pip = _overlay.value.pip.copy(enabled = enabled)) }
	fun setPipSource(source: Source?) { _overlay.value = _overlay.value.copy(pip = _overlay.value.pip.copy(source = source)) }
	fun setPipPosition(pos: Offset) { _overlay.value = _overlay.value.copy(pip = _overlay.value.pip.copy(position = pos)) }
	fun setPipSize(widthDp: Float, heightDp: Float) { _overlay.value = _overlay.value.copy(pip = _overlay.value.pip.copy(widthDp = widthDp, heightDp = heightDp)) }
	fun setPipOpacity(alpha: Float) { _overlay.value = _overlay.value.copy(pip = _overlay.value.pip.copy(opacity = alpha.coerceIn(0f,1f))) }

	fun setRtmpUrl(url: String) { _rtmpUrl.value = url }
	fun setStreaming(active: Boolean) { _isStreaming.value = active }
	fun setRecording(active: Boolean) { _isRecording.value = active }

	fun updateSettings(transform: SettingsState.() -> SettingsState) { _settings.value = _settings.value.transform() }

	fun startPrivacyMonitor(context: android.content.Context) {
		val monitor = com.example.virtualcameraliveassist.privacy.PrivacyMonitor(context)
		viewModelScope.launch {
			monitor.cameraAccessEvents().collect { inUse ->
				_cameraInUseByOthers.value = inUse
			}
		}
	}
}
