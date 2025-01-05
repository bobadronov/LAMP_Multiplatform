package org.bigblackowl.lamp.data

import kotlinx.serialization.Serializable

@Serializable
data class DeviceStatus(
    val version: String = "",
    val ledState: Boolean = false,
    val commonBrightness: Float = 255f,
    val animationSpeed: Float = 255f,
    val REAL_NUM_LEDS: Int = 10,
    val currentMode: Int = 0,
    val color: String = "",
    val gradientStart: String = "",
    val gradientEnd: String = "",
    val customFadeColor1: String = "",
    val customFadeColor2: String = "",
    val temperature: Float = 0.0f,
    val humidity: Float = 0.0f,
    val modes: List<String> = emptyList(),
    val customColorsArray: List<String> = emptyList(),
    val rainbowIsStatic: Boolean = false,
    val flagIsStatic: Boolean = true,
    val timerIsActive: Boolean = false,
    val timer: Timer = Timer(),
)

@Serializable
data class ErrorStatus(
    val message: String? = null
)

data class WiFiState(
    val isOn: Boolean = false,
    val isConnected: Boolean = false
)

//    val version: String = "",
//    //=======================
//
//    //=======================
//    val breathSpeed: Float = 0f,
//    val isBreathModeRainbow: Boolean = false,
//    //=======================
//    val snakeSpeed: Float = 0f,
//    val snakeLength: Int = 0,
//    //=======================
//    val policeOnPatrol: Boolean = true,
//    //=======================