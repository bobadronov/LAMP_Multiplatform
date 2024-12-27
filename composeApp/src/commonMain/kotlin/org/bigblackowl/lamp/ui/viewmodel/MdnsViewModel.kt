package org.bigblackowl.lamp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstractive.dnssd.DiscoveryEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.bigblackowl.lamp.core.mdns.MdnsDevice
import org.bigblackowl.lamp.core.mdns.MdnsService

class MdnsViewModel(
    private val mdnsService: MdnsService
) : ViewModel() {

    private val _devices = MutableStateFlow<List<MdnsDevice>>(emptyList())
    val devices: StateFlow<List<MdnsDevice>> = _devices

    fun startDiscovery() {
        stopDiscovery()
        viewModelScope.launch {
            try {
//                println("Starting mDNS discovery...")
                mdnsService.startDiscovery(serviceType = "_http._tcp") { event ->
                    when (event) {
                        is DiscoveryEvent.Discovered, is DiscoveryEvent.Resolved -> {
                            val device = MdnsDevice(
                                name = event.service.name,
                                ipAddress = event.service.host,
                            )
//                            println("MdnsViewModel Discovered $device")
                            addOrUpdateDevice(device)
                        }

                        is DiscoveryEvent.Removed -> {
                            val device = MdnsDevice(
                                name = event.service.name,
                                ipAddress = event.service.host,
                            )
//                            println("MdnsViewModel Removed $device")
                            removeDevice(device)
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error starting mDNS discovery: ${e.message}")
            }
        }
    }

    fun stopDiscovery() {
        viewModelScope.launch {
            try {
                _devices.value = emptyList()
                mdnsService.stopDiscovery()
//                println("mDNS discovery stopped")
            } catch (e: Exception) {
                println("Error stopping mDNS discovery: ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopDiscovery()
    }

    private fun addOrUpdateDevice(device: MdnsDevice) {
        _devices.value = _devices.value.toMutableList().apply {
            val index = indexOfFirst { it.name == device.name }
            if (index != -1) {
                this[index] = device // Обновляем устройство
            } else {
                add(device) // Добавляем новое устройство
            }
        }
    }

    private fun removeDevice(device: MdnsDevice) {
        _devices.value = _devices.value.toMutableList().apply {
            removeAll { it.name == device.name }
        }
    }
}
