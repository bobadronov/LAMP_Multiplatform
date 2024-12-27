package org.bigblackowl.lamp.core.mdns

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PlatformPermission actual constructor() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    actual fun RequestPermission() {
        val activity = LocalContext.current as? Activity ?: return
        // Check if permission is already granted
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.NEARBY_WIFI_DEVICES
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.NEARBY_WIFI_DEVICES
                ),
                100
            )
        }
    }
}