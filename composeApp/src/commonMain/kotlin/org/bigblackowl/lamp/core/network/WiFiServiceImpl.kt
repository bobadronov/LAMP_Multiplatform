package org.bigblackowl.lamp.core.network

import kotlinx.coroutines.flow.StateFlow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class WiFiServiceImpl : WiFiService{
    override val availableNetworks: StateFlow<List<WiFiNetwork>>
    override suspend fun getAvailableNetworks()
}
