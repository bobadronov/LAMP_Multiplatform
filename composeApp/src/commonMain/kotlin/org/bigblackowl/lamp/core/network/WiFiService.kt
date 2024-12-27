package org.bigblackowl.lamp.core.network

import kotlinx.coroutines.flow.StateFlow

interface WiFiService {
    val availableNetworks: StateFlow<List<WiFiNetwork>>
    suspend fun getAvailableNetworks()
}