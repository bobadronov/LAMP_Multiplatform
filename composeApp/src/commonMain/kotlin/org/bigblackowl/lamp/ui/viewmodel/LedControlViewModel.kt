package org.bigblackowl.lamp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.bigblackowl.lamp.core.network.WebSocketClient
import org.bigblackowl.lamp.data.ConnectionState
import org.bigblackowl.lamp.data.DeviceStatus
import org.bigblackowl.lamp.data.ErrorStatus
import org.bigblackowl.lamp.data.SetupEspCredential
import org.bigblackowl.lamp.data.Timer

class LedControlViewModel(
    private val webSocketClient: WebSocketClient,
) : ViewModel() {

    private val _connectionConnectionState = MutableStateFlow(ConnectionState(state = false))
    val connectionState: StateFlow<ConnectionState> = _connectionConnectionState

    private val _errorMessage = MutableStateFlow(ErrorStatus())
    val errorMessage: StateFlow<ErrorStatus> = _errorMessage


    private val _deviceStatus = MutableStateFlow(DeviceStatus())
    val deviceStatus: StateFlow<DeviceStatus> = _deviceStatus

    private val _update = MutableStateFlow(false)  // Исправлено, теперь начальное значение false

    private fun checkConnection() {
        viewModelScope.launch {
            while (_update.value) {
                webSocketClient.checkConnection()
                delay(5000)
            }
        }
    }

    fun connect(deviceName: String) {
        viewModelScope.launch {
            webSocketClient.connect(
                host = deviceName,
                incomingListener = { newStatus ->
                    val updatedStatus = parseAndMergeStatus(newStatus, _deviceStatus.value)
                    _deviceStatus.value = updatedStatus
                    _update.value = true
                },
                connectionStateListener = { state ->
                    _connectionConnectionState.value = state
                    _update.value = state.state
                },
                errorListener = { status ->
                    _errorMessage.value = status
                    _update.value = false
                }
            )
            checkConnection()
        }
    }

    fun clearErrorMessage() {
        viewModelScope.launch {
            delay(5000)
            _errorMessage.value = ErrorStatus(message = null)
        }
    }

    fun setLedState(ledState: Boolean) {
        viewModelScope.launch {
            try {
                webSocketClient.sendLedState(ledState)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setColor(hexCode: String) {
        viewModelScope.launch {
            try {
                webSocketClient.sendColor(hexCode)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setupESP(setupEspCredential: SetupEspCredential) {
        viewModelScope.launch {
            try {
                webSocketClient.setupESP(setupEspCredential)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setMode(index: Int) {
        viewModelScope.launch {
            try {
                webSocketClient.setMode(index)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setFlagState(state: Boolean) {
        viewModelScope.launch {
            try {
                webSocketClient.setFlagState(state)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun setAnimationSpeed(rainbowSpeed: Float) {
        viewModelScope.launch {
            try {
                webSocketClient.setAnimationSpeed(rainbowSpeed)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setTimer(time: Timer) {
        viewModelScope.launch {
            try {
                webSocketClient.setupTimer(time)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cancelTimer() {
        viewModelScope.launch {
            try {
                webSocketClient.cancelTimer()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setCustomMode(updatedColors: List<String>) {
        viewModelScope.launch {
            try {
                webSocketClient.setCustomMode(updatedColors)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setRainbowStaticMode(value: Boolean) {
        viewModelScope.launch {
            try {
                webSocketClient.setRainbowStaticMode(value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setCommonBrightness(commonBrightness: Float) {
        viewModelScope.launch {
            try {
                webSocketClient.setCommonBrightness(commonBrightness)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun reboot() {
        viewModelScope.launch {
            try {
                webSocketClient.reboot()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setLedCount(value: Int) {
        viewModelScope.launch {
            try {
                webSocketClient.setLedCount(value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setGradient(list: List<String>) {
        viewModelScope.launch {
            try {
                webSocketClient.setGradient(list)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setCustomFadeColor(color1: String, color2: String) {
        viewModelScope.launch {
            try {
                webSocketClient.setCustomFadeColor(color1, color2)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            webSocketClient.close(connectionStateListener = { state ->
                _connectionConnectionState.value = state
            })
        }
    }
}

private fun parseAndMergeStatus(
    newStatus: DeviceStatus,
    deviceStatus: DeviceStatus,
): DeviceStatus {
    // Получение текущего статуса
    val currentStatus = deviceStatus

    // Объединяем старые данные с новыми
    return currentStatus.copy(
        version = newStatus.version.ifEmpty { currentStatus.version },
        ledState = newStatus.ledState,
        commonBrightness = newStatus.commonBrightness,
        animationSpeed = newStatus.animationSpeed,
        REAL_NUM_LEDS = newStatus.REAL_NUM_LEDS,
        currentMode = newStatus.currentMode,
        color = newStatus.color.ifEmpty { currentStatus.color },
        gradientStart = newStatus.gradientStart.ifEmpty { currentStatus.gradientStart },
        gradientEnd = newStatus.gradientEnd.ifEmpty { currentStatus.gradientEnd },
        customFadeColor1 = newStatus.customFadeColor1.ifEmpty { currentStatus.customFadeColor1 },
        customFadeColor2 = newStatus.customFadeColor2.ifEmpty { currentStatus.customFadeColor2 },
        temperature = if (newStatus.temperature != 0.0f) newStatus.temperature else currentStatus.temperature,
        humidity = if (newStatus.humidity != 0.0f) newStatus.humidity else currentStatus.humidity,
        modes = newStatus.modes.ifEmpty { currentStatus.modes },
        customColorsArray = newStatus.customColorsArray.ifEmpty { currentStatus.customColorsArray },
        rainbowIsStatic = newStatus.rainbowIsStatic,
        flagIsStatic = newStatus.flagIsStatic,
        timerIsActive = newStatus.timerIsActive,
        timer = newStatus.timer
    )
}
