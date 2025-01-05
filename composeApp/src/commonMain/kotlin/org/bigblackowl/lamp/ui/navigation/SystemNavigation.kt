package org.bigblackowl.lamp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.bigblackowl.lamp.data.Platform
import org.bigblackowl.lamp.ui.screens.ControlScreen
import org.bigblackowl.lamp.ui.screens.MdnsScreen
import org.bigblackowl.lamp.ui.screens.SetupESPScreen
import org.bigblackowl.lamp.ui.viewmodel.LedControlViewModel
import org.bigblackowl.lamp.ui.viewmodel.MdnsViewModel
import org.bigblackowl.lamp.ui.viewmodel.WifiViewModel

@Composable
fun SystemNavigation(
    ledControlViewModel: LedControlViewModel,
    mdnsViewModel: MdnsViewModel,
    wifiViewModel: WifiViewModel,
    paddingValues: PaddingValues
) {

    val navController = rememberNavController()
    val currentPlatformName = remember { Platform().getPlatformName() }
    NavHost(
        navController,
        startDestination = ScreensRoute.MdnsScreensRoute.route,
        modifier = Modifier.fillMaxSize().padding(paddingValues).consumeWindowInsets(paddingValues)
    ) {
        //HOME
        composable(route = ScreensRoute.MdnsScreensRoute.route) {
            MdnsScreen(mdnsViewModel, navController)
        }

        composable(
            route = ScreensRoute.ControlScreensRoute.route,
            arguments = listOf(navArgument("deviceName") { type = NavType.StringType })
        ) { backStackEntry ->
            val deviceName = backStackEntry.arguments?.getString("deviceName") ?: ""
            ControlScreen(ledControlViewModel, navController, deviceName)
        }

        composable(route = ScreensRoute.SetupESPScreen.route) {
            SetupESPScreen(ledControlViewModel, wifiViewModel, mdnsViewModel, navController)
        }
    }
}