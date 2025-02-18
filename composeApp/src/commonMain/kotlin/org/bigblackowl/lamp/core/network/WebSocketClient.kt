package org.bigblackowl.lamp.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bigblackowl.lamp.data.ConnectionState
import org.bigblackowl.lamp.data.DeviceStatus
import org.bigblackowl.lamp.data.ErrorStatus
import org.bigblackowl.lamp.data.SetupEspCredential
import org.bigblackowl.lamp.data.Timer
import kotlin.time.Duration

class WebSocketClient {
    private val client: HttpClient = HttpClient(CIO) {
        engine {
            maxConnectionsCount = 1000
            endpoint {
                keepAliveTime = 5000
                connectTimeout = 5000
                connectAttempts = 5
            }
        }
        install(WebSockets) {
            pingInterval = Duration.ZERO
        }
    }

    private var session: DefaultClientWebSocketSession? = null
    private val json = Json { ignoreUnknownKeys = true }


    suspend fun connect(
        host: String,
        incomingListener: (DeviceStatus) -> Unit,
        errorListener: (ErrorStatus) -> Unit,
        connectionStateListener: (ConnectionState) -> Unit
    ) {
//        println("Attempting to connect to WebSocket server at host: $host")
        try {
            client.webSocket(
                method = HttpMethod.Get, host = "$host.local", port = 80, path = "/ws"
            ) {
                println("Successfully connected to WebSocket server at host: $host")
                session = this
                connectionStateListener(ConnectionState(true))
                try {
                    while (true) { // Убедитесь, что входящие сообщения обрабатываются в цикле
                        val message = incoming.receive() as? Frame.Text
                        message?.let {
                            val text = it.readText()
//                            println("Received message: $text")
                            // if start with ("STATUS:") trim STATUS: then next
                            when {
                                text.startsWith("STATUS:") -> {
                                    val statusText = text.removePrefix("STATUS:")
                                    try {
                                        val deviceStatus =
                                            json.decodeFromString<DeviceStatus>(statusText)
                                        connectionStateListener(ConnectionState(true))
                                        incomingListener(deviceStatus)
                                    } catch (e: Exception) {
                                        println("Error decoding STATUS message: ${e.message}")
                                        connectionStateListener(ConnectionState(false))
                                    }
                                }

                                text.startsWith("ERROR:") -> {
                                    val errorText = text.removePrefix("ERROR:")
                                    println("Received error: $errorText")
                                    try {
                                        val errorStatus =
                                            json.decodeFromString<ErrorStatus>(errorText)
                                        errorListener(errorStatus)
                                        // Дополнительная логика обработки ошибок
                                    } catch (e: Exception) {
                                        println("Error decoding STATUS message: ${e.message}")
                                        connectionStateListener(ConnectionState(false))
                                    }
                                }

                                else -> {
                                    println("Unknown message format: $text")
                                    // Обработка других случаев, если нужно
                                }
                            }
                        }
                    }
                } catch (e: ClosedReceiveChannelException) {
                    println("WebSocket connection closed: ${e.message}")
                    connectionStateListener(ConnectionState(false))
                } catch (e: Exception) {
                    println("Error during WebSocket communication: ${e.message}")
                    connectionStateListener(ConnectionState(false))
                }
            }
        } catch (e: Exception) {
            println("Failed to connect to WebSocket server: ${e.message}")
            connectionStateListener(ConnectionState(false))
        } finally {
            println("Cleaning up WebSocket session.")
            session = null
            connectionStateListener(ConnectionState(false))
        }
    }

    private suspend fun sendSetupMessage(value: String) {
        try {
            val message = "SETUP:$value"
            session?.send(Frame.Text(message))
        } catch (e: Exception) {
            println("Error sending message: ${e.message}")
        }
    }

    suspend fun close(connectionStateListener: (ConnectionState) -> Unit) {
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Client closed"))
        connectionStateListener(ConnectionState(false))
    }

    suspend fun sendLedState(ledState: Boolean) {
        val message = json.encodeToString(mapOf("ledState" to ledState))
        sendSetupMessage(message)
    }

    suspend fun setMode(index: Int) {
        val message = json.encodeToString(mapOf("currentMode" to index))
        sendSetupMessage(message)
    }

    suspend fun sendColor(hexCode: String) {
        val message = json.encodeToString(mapOf("color" to hexCode))
        sendSetupMessage(message)
    }

    suspend fun setFlagState(state: Boolean) {
        val message = json.encodeToString(mapOf("flagIsStatic" to state))
        sendSetupMessage(message)
    }

    suspend fun setAnimationSpeed(animationSpeed: Float) {
        val message = json.encodeToString(mapOf("animationSpeed" to animationSpeed))
        sendSetupMessage(message)
    }

    suspend fun setupESP(setupEspCredential: SetupEspCredential) {
        // Создание карты с двумя полями ssid и password
        val jsonContent = json.encodeToString(
            mapOf(
                "ssid" to setupEspCredential.ssid,
                "password" to setupEspCredential.password,
                "deviceName" to setupEspCredential.deviceName,
            )
        )
        // Добавление префикса "wifi" перед JSON-строкой
        val message = "WIFI:$jsonContent"
        // Отправка сообщения
        try {
            session?.send(Frame.Text(message))
        } catch (e: Exception) {
            println("Error sending message: ${e.message}")
        }
    }

    suspend fun setupTimer(timer: Timer) {
        val jsonContent = json.encodeToString(
            mapOf(
                "day" to timer.day,
                "month" to timer.month,
                "year" to timer.year,
                "hour" to timer.hour,
                "minute" to timer.minute
            )
        )
        val message = "TIME:${jsonContent}"
        println("TIME: $message")
        try {
            session?.send(Frame.Text(message))
        } catch (e: Exception) {
            println("Error sending message: ${e.message}")
        }
    }

    suspend fun cancelTimer() {
        try {
            session?.send(Frame.Text("CANCEL_TIMER"))
        } catch (e: Exception) {
            println("Error sending message: ${e.message}")
        }
    }

    suspend fun setCustomMode(updatedColors: List<String>) {
        val message = json.encodeToString(mapOf("customColors" to updatedColors))
        sendSetupMessage(message)
    }

    suspend fun setRainbowStaticMode(value: Boolean) {
        val message = json.encodeToString(mapOf("rainbowIsStatic" to value))
        sendSetupMessage(message)
    }

    suspend fun setCommonBrightness(commonBrightness: Float) {
        val message = json.encodeToString(mapOf("commonBrightness" to commonBrightness.toInt()))
        sendSetupMessage(message)
    }

    suspend fun reboot() {
        try {
            session?.send(Frame.Text("REBOOT"))
        } catch (e: Exception) {
            println("Error sending message: ${e.message}")
        }
    }

    suspend fun setLedCount(value: Int) {
        val message = json.encodeToString(mapOf("REAL_NUM_LEDS" to value))
//        println("setLedCount: $message")
        sendSetupMessage(message)
    }

    suspend fun setGradient(list: List<String>) {
        val message = json.encodeToString(mapOf("customGradient" to list))
//        println("setGradient: $message")
        sendSetupMessage(message)
    }

    suspend fun checkConnection() {
        try {
            session?.send(Frame.Text("____"))
        } catch (e: Exception) {
            println("Error sending message: ${e.message}")
        }
    }

    suspend fun setCustomFadeColor(color1: String, color2: String) {
        val message = json.encodeToString(
            mapOf(
                "customFadeColor1" to color1,
                "customFadeColor2" to color2
            )
        )
        println("setCustomFadeColor: $message")
        sendSetupMessage(message)
    }
}