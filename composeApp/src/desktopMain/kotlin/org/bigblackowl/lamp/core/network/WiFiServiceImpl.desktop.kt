package org.bigblackowl.lamp.core.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class WiFiServiceImpl() : WiFiService {
    // StateFlow to hold available networks
    private val _availableNetworks = MutableStateFlow<List<WiFiNetwork>>(emptyList())
    override val availableNetworks: StateFlow<List<WiFiNetwork>> = _availableNetworks
    override suspend fun getAvailableNetworks() {
        return withContext(Dispatchers.IO) {
            val process = ProcessBuilder("netsh", "wlan", "show", "profiles").start()
            val output = BufferedReader(InputStreamReader(process.inputStream, Charsets.UTF_8)).readText()
            println("WiFiServiceImpl Desktop output: $output")
            _availableNetworks.value = parseNetworks(output)
        }
    }

    private fun parseNetworks(output: String): List<WiFiNetwork> {
        val ssidRegex = Regex("All User Profile\\s+:\\s+(.+)")
        val networks = mutableListOf<WiFiNetwork>()

        val lines = output.lines()
        for (line in lines) {
            val ssidMatch = ssidRegex.find(line)
            if (ssidMatch != null) {
                val ssid = ssidMatch.groupValues[1].trim()
                networks.add(WiFiNetwork(ssid = ssid)) // Signal strength unavailable
            }
        }

        return networks
    }
}