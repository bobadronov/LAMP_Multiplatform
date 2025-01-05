package org.bigblackowl.lamp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appstractive.dnssd.DiscoveryEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.bigblackowl.lamp.core.mdns.MdnsDevice
import org.bigblackowl.lamp.core.mdns.MdnsService
import org.bigblackowl.lamp.data.DataKey
import org.bigblackowl.lamp.datastorage.DataStorage

class MdnsViewModel(
    private val mdnsService: MdnsService,
    private val dataStorage: DataStorage
) : ViewModel() {

    private val _deviceFilter = MutableStateFlow("-LED")
    val deviceFilter: StateFlow<String> = _deviceFilter

    private val _devices = MutableStateFlow<List<MdnsDevice>>(emptyList())
    val devices: StateFlow<List<MdnsDevice>> = _devices

    init {
        val filter = getValue(DataKey.KEY.keyName) ?: "-LED"
        _deviceFilter.value = filter
    }

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

    fun setFilter(filter: String) {
        _deviceFilter.value = filter
        saveValue(DataKey.KEY.keyName, filter)
        startDiscovery()
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
        filterDevices()
    }

    private fun removeDevice(device: MdnsDevice) {
        _devices.value = _devices.value.toMutableList().apply {
            removeAll { it.name == device.name }
        }
        filterDevices()
    }

    private fun filterDevices() {
        _devices.value = _devices.value.filter { it.name.contains(_deviceFilter.value) }
    }

    private fun saveValue(key: String, value: String) {
        dataStorage.save(key, value)
    }

    private fun getValue(key: String): String? {
        return dataStorage.get(key)
    }
}
