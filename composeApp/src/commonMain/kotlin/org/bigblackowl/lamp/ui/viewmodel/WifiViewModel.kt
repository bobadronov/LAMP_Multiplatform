package org.bigblackowl.lamp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.bigblackowl.lamp.core.network.WiFiNetwork
import org.bigblackowl.lamp.core.network.WiFiServiceImpl

class WifiViewModel(
    private val wifiService: WiFiServiceImpl
) : ViewModel() {

    private val _wifiNetworks = MutableStateFlow<List<WiFiNetwork>>(emptyList())
    val wifiNetworks: StateFlow<List<WiFiNetwork>> = _wifiNetworks

    fun loadNetworks() {
        viewModelScope.launch {
            try {
                wifiService.getAvailableNetworks() // Fetch networks from WiFiService
                _wifiNetworks.value = wifiService.availableNetworks.value // Update ViewModel with networks
            } catch (e: Exception) {
                println("Error loading networks: ${e.message}")
                _wifiNetworks.value = emptyList() // Reset to empty list in case of error
            }
        }
    }
}