package org.bigblackowl.lamp.data

import kotlinx.serialization.Serializable

@Serializable
data class DeviceStatus(
    val ledState: Boolean = false,
    val NUM_LEDS: Int = 0,
    val currentMode: Int = 0,
    val color: String = "",
    val temperature: Float = 0.0f,
    val humidity: Float = 0.0f,
    val modes: List<String> = emptyList(),
    val rainbowSpeed: Float = 0f,
    val flagSpeed: Int = 1,
    val flagIsStatic: Boolean = true,
    val timerIsActive : Boolean = false,
    val timer: Timer = Timer(),
)

data class WiFiState(
    val isOn: Boolean = false,
    val isConnected: Boolean = false
)

//    val version: String = "",
//    //=======================
//    val rainbowStatic: Boolean = false,
//    //=======================
//    val breathSpeed: Float = 0f,
//    val isBreathModeRainbow: Boolean = false,
//    //=======================
//    val snakeSpeed: Float = 0f,
//    val snakeLength: Int = 0,
//    //=======================
//    val policeOnPatrol: Boolean = true,
//    //=======================