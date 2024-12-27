package org.bigblackowl.lamp.ui.navigation

sealed class ScreensRoute(val route: String) {
    data object MdnsScreensRoute : ScreensRoute("MdnsScreen")
    data object ControlScreensRoute : ScreensRoute("ControlScreen/{deviceName}") {
        fun createRoute(deviceName: String) = "ControlScreen/$deviceName"
    }
    data object SetupESPScreen: ScreensRoute("WifiSetupScreen")

}