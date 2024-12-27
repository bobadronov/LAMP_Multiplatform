package org.bigblackowl.lamp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavHostController
import org.bigblackowl.lamp.core.mdns.PlatformPermission
import org.bigblackowl.lamp.ui.items.FoundDeviceItem
import org.bigblackowl.lamp.ui.items.topAppBars.MdnsScreenTopAppBar
import org.bigblackowl.lamp.ui.navigation.ScreensRoute
import org.bigblackowl.lamp.ui.theme.SurfaceColor
import org.bigblackowl.lamp.ui.viewmodel.MdnsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MdnsScreen(
    mdnsViewModel: MdnsViewModel,
    navController: NavHostController,
) {
    PlatformPermission().RequestPermission()
    LaunchedEffect(Unit) {
        mdnsViewModel.startDiscovery()
        println(Locale.current.language)
    }
    val devices by mdnsViewModel.devices.collectAsState()
    val state = rememberPullToRefreshState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .onKeyEvent {
                if (it.key == Key.F5 && it.type == KeyEventType.KeyDown) {
                    mdnsViewModel.startDiscovery()
                    true
                } else false
            },
        topBar = {
            MdnsScreenTopAppBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    mdnsViewModel.startDiscovery()
                },
                modifier = Modifier.padding(bottom = 40.dp, end = 10.dp),
                containerColor = SurfaceColor
            ) {
                Icon(Icons.Outlined.Refresh, null)
            }
        },
        floatingActionButtonPosition = FabPosition.EndOverlay
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = devices.isEmpty(),
            onRefresh = {
                mdnsViewModel.startDiscovery()
            },
            modifier = Modifier.padding(padding).fillMaxSize(),
            state = state,
            contentAlignment = Alignment.TopCenter,
            indicator = {
                Indicator(
                    state = state,
                    isRefreshing = devices.isEmpty(),
                    modifier = Modifier.size(50.dp),
                    containerColor = Color.Transparent,
                    color = Color.Cyan,
                    threshold = 60.dp
                )
            },
        ) {
            LazyColumn(
                Modifier.padding(horizontal = 20.dp).fillMaxSize()
            ) {
                if (devices.isNotEmpty()) {
                    items(devices.size) { index ->
                        FoundDeviceItem(
                            deviceName = devices[index].name,
                            ip = devices[index].ipAddress,
                            onClick = {
                                println("MdnsScreen FoundDeviceItem clicked -> ${devices[index].name}")
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
            mdnsViewModel.stopDiscovery()
        }
    }
}

