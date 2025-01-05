package org.bigblackowl.lamp.core.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import platform.Foundation.NSDictionary
import platform.Foundation.valueForKey
import platform.SystemConfiguration.CNCopyCurrentNetworkInfo
import platform.SystemConfiguration.CNCopySupportedInterfaces

actual class WiFiServiceImpl : WiFiService {

    private val _availableNetworks = MutableStateFlow<List<WiFiNetwork>>(emptyList())
    actual override val availableNetworks: StateFlow<List<WiFiNetwork>> = _availableNetworks

    actual override suspend fun getAvailableNetworks() {
        return withContext(Dispatchers.IO) {
            val interfaces = CNCopySupportedInterfaces() as? List<String> ?: return emptyList()
            val networks = mutableListOf<WiFiNetwork>()

            for (interfaceName in interfaces) {
                val networkInfo =
                    CNCopyCurrentNetworkInfo(interfaceName as CFString) as NSDictionary?
                val ssid = networkInfo?.valueForKey("SSID") as? String ?: continue
                networks.add(WiFiNetwork(ssid = ssid)) // Уровень сигнала не доступен на iOS
            }

        }
    }
}