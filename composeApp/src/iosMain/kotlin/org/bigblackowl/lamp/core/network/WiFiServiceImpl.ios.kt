package org.bigblackowl.lamp.core.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import platform.Foundation.NSDictionary
import platform.Foundation.valueForKey
import platform.SystemConfiguration.CNCopySupportedInterfaces
import platform.SystemConfiguration.CNCopyCurrentNetworkInfo

actual class WiFiServiceImpl() : WiFiService {
    private val _availableNetworks = MutableStateFlow<List<WiFiNetwork>>(emptyList())
    actual override val availableNetworks: StateFlow<List<WiFiNetwork>> = _availableNetworks

    actual suspend fun getAvailableNetworks(): List<WiFiNetwork> {
        val interfaces = CNCopySupportedInterfaces() as? List<String> ?: return emptyList()
        val networks = mutableListOf<WiFiNetwork>()

        for (interfaceName in interfaces) {
            val networkInfo = CNCopyCurrentNetworkInfo(interfaceName as CFString) as NSDictionary?
            val ssid = networkInfo?.valueForKey("SSID") as? String ?: continue
            networks.add(WiFiNetwork(ssid = ssid)) // Уровень сигнала не доступен на iOS
        }

        return networks
    }
}