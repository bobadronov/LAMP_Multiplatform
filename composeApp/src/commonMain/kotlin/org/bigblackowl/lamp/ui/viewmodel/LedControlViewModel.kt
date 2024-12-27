package org.bigblackowl.lamp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.bigblackowl.lamp.core.network.WebSocketClient
import org.bigblackowl.lamp.data.ConnectionState
import org.bigblackowl.lamp.data.DeviceStatus
import org.bigblackowl.lamp.data.SetupEspCredential
import org.bigblackowl.lamp.data.Timer

class LedControlViewModel(
    private val webSocketClient: WebSocketClient,
) : ViewModel() {
    private val _connectionConnectionState = MutableStateFlow(ConnectionState(state = false))
    val connectionState: StateFlow<ConnectionState> = _connectionConnectionState

    private val _deviceStatus = MutableStateFlow(DeviceStatus())
    val deviceStatus: StateFlow<DeviceStatus> = _deviceStatus


    fun connect(deviceName: String) {
        viewModelScope.launch {
            webSocketClient.connect(host = deviceName, incomingListener = { newStatus ->
                println("LedStripViewModel newStatus: $newStatus")
                _deviceStatus.value = newStatus
//                println("LedStripViewModel _deviceStatus: ${_deviceStatus.value}")
            }, connectionStateListener = { state ->
//                println("LedStripViewModel connectionStateListener: $state")
                _connectionConnectionState.value = state
            })
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
        println("LedStripViewModel setColor hexCode: $hexCode")
        viewModelScope.launch {
            try {
                webSocketClient.sendColor(hexCode)
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

    fun setupESP(setupEspCredential: SetupEspCredential) {
//        println("LedStripViewModel setupWifi: $setupEspCredential")
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

    fun setFlagSpeed(flagSpeed: Int) {
        viewModelScope.launch {
            try {
                webSocketClient.setFlagSpeed(flagSpeed)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setRainbowSpeed(rainbowSpeed: Float) {
        viewModelScope.launch {
            try {
                webSocketClient.setRainbowSpeed(rainbowSpeed)
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
}