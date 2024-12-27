package org.bigblackowl.lamp.core.mdns

import com.appstractive.dnssd.DiscoveryEvent
import com.appstractive.dnssd.discoverServices

class MdnsServiceFactory {
    fun createMdnsService(): MdnsService = MultiplatformMdnsService()
}

class MultiplatformMdnsService : MdnsService {
    private var isDiscovering = false

    override suspend fun startDiscovery(serviceType: String, onDiscover: (DiscoveryEvent) -> Unit) {
        if (isDiscovering) return
        isDiscovering = true

        discoverServices(serviceType).collect { event ->
            onDiscover(event)
        }
    }

    override fun stopDiscovery() {
        isDiscovering = false
        // discoverServices поток будет завершен автоматически
    }
}

interface MdnsService {
    suspend fun startDiscovery(serviceType: String, onDiscover: (DiscoveryEvent) -> Unit)
    fun stopDiscovery()
}

data class MdnsDevice(val name: String, val ipAddress: String)
