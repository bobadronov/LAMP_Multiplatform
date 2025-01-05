package org.bigblackowl.lamp.core.network

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class WiFiServiceImpl(private val context: Context) : WiFiService {

    // StateFlow to hold available networks
    private val _availableNetworks = MutableStateFlow<List<WiFiNetwork>>(emptyList())
    actual override val availableNetworks: StateFlow<List<WiFiNetwork>> = _availableNetworks

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    actual override suspend fun getAvailableNetworks() {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        println("WiFiServiceImpl wifiManager: $wifiManager")

        // Check for required permissions
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.NEARBY_WIFI_DEVICES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("WiFiServiceImpl: Permissions for accessing Wi-Fi networks are not granted")
            throw SecurityException("Permissions for accessing Wi-Fi networks are not granted")
        }

        // Get scan results asynchronously
        val scanResults = withContext(Dispatchers.IO) {
            wifiManager.scanResults
        }
        println("WiFiServiceImpl scanResults: $scanResults")

        // Map results to WiFiNetwork and emit to StateFlow
        _availableNetworks.value = scanResults.map {
            WiFiNetwork(ssid = it.wifiSsid.toString()) // Use SSID as the network name
        }
    }

}
