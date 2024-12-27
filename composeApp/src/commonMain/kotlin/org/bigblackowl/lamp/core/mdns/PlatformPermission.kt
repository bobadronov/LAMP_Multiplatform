package org.bigblackowl.lamp.core.mdns

import androidx.compose.runtime.Composable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PlatformPermission() {
    @Composable
    fun RequestPermission()
}