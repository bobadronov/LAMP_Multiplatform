package org.bigblackowl.lamp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.bigblackowl.lamp.core.mdns.PlatformPermission
import org.bigblackowl.lamp.ui.items.FoundDeviceItem
import org.bigblackowl.lamp.ui.navigation.ScreensRoute
import org.bigblackowl.lamp.ui.viewmodel.LedControlViewModel
import org.bigblackowl.lamp.ui.viewmodel.MdnsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MdnsScreen(
    mdnsViewModel: MdnsViewModel,
    ledControlViewModel: LedControlViewModel,
    navController: NavHostController,
    paddingValues: PaddingValues,
) {
    PlatformPermission().RequestPermission()
    LaunchedEffect(Unit) {
        mdnsViewModel.startDiscovery()
        println(Locale.current.language)
    }
    val devices by mdnsViewModel.devices.collectAsState()
    val state = rememberPullToRefreshState()

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp).padding(paddingValues)
            .consumeWindowInsets(paddingValues).onKeyEvent {
                if (it.key == Key.F5 && it.type == KeyEventType.KeyDown) {
                    mdnsViewModel.startDiscovery()
                    true
                } else false
            }, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Devices on the local network:",
            modifier = Modifier.padding(10.dp),
            color = Color.White,
            fontSize = 22.sp
        )
        PullToRefreshBox(
            isRefreshing = devices.isEmpty(),
            onRefresh = {
                mdnsViewModel.startDiscovery()
            },
            modifier = Modifier.fillMaxSize(),
            state = state,
            contentAlignment = Alignment.TopCenter,
            indicator = {
                Indicator(
                    state = state,
                    isRefreshing = devices.isEmpty(),
                    containerColor = Color.Transparent,
                    color = Color.Cyan,
                    threshold = 60.dp
                )
            },
        ) {
            LazyColumn(
                Modifier.fillMaxSize()
            ) {
                if (devices.isNotEmpty()) {
                    items(devices.size) { index ->
                        FoundDeviceItem(
                            deviceName = devices[index].name,
                            ip = devices[index].ipAddress,
                            onClick = {
                                navController.navigate(
                                    ScreensRoute.ControlScreensRoute.createRoute(
                                        devices[index].name
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    DisposableEffect(navController.currentDestination?.route) {
        onDispose {
            println("mDNS DisposableEffect discovery stopped")
            mdnsViewModel.stopDiscovery() }
    }
}

