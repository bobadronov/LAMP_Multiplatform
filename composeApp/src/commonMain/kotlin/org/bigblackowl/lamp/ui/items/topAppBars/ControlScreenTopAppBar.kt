package org.bigblackowl.lamp.ui.items.topAppBars

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import lamp_multiplatform.composeapp.generated.resources.Res
import lamp_multiplatform.composeapp.generated.resources.on_button
import lamp_multiplatform.composeapp.generated.resources.wifi
import org.bigblackowl.lamp.data.ConnectionState
import org.bigblackowl.lamp.data.DeviceStatus
import org.bigblackowl.lamp.ui.items.dialog.InfoDialog
import org.bigblackowl.lamp.ui.navigation.ScreensRoute
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlScreenTopAppBar(
    deviceName: String,
    deviceStatus: DeviceStatus,
    connectionState: ConnectionState,
    ledState: Boolean,
    navController: NavHostController,
    offLedClicked: () -> Unit,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    var openInfo by remember { mutableStateOf(false) }
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = Color.DarkGray,
            scrolledContainerColor = Color.Unspecified,
            navigationIconContentColor = Color.Unspecified,
            titleContentColor = Color.Unspecified,
            actionIconContentColor = Color.Unspecified
        ),
        title = {
            Text(
                deviceName.uppercase(),
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = null,
                    modifier = Modifier.size(35.dp),
                )
            }
        },
        actions = {
            if (connectionState.state && ledState) {
                IconButton(
                    onClick = offLedClicked,
                    modifier = Modifier.padding(end = 15.dp)
                ) {
                    Icon(
                        painterResource(Res.drawable.on_button),
                        contentDescription = null,
                        modifier = Modifier.size(35.dp),
                        tint = Color.Red,
                    )
                }
            }
            Text(
                if (connectionState.state) "Online" else "Offline",
                modifier = Modifier.padding(end = 15.dp),
                color = if (connectionState.state) Color.Green else Color.Red,
            )
            if (connectionState.state) {
                IconButton(
                    onClick = {
                        isMenuExpanded = !isMenuExpanded
                    },
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp),
                        tint = Color.Gray,
                    )
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = {
                            isMenuExpanded = false
                            openInfo = false
                        }
                    ) {

                        DropdownMenuItem(
                            text = {
                                Text("Setup WiFi")
                            },
                            onClick = {
                                navController.navigate(ScreensRoute.SetupESPScreen.route)
                            },
                            trailingIcon = {
                                Icon(
                                    painterResource(Res.drawable.wifi),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp),
                                    tint = Color.Gray,
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text("Info ")
                            },
                            onClick = {
                                openInfo = !openInfo
                                isMenuExpanded = false
                            },
                            trailingIcon = {
                                Icon(
                                    painterResource(Res.drawable.wifi),
                                    contentDescription = null,
                                    modifier = Modifier.size(35.dp),
                                    tint = Color.Gray,
                                )
                            }
                        )
                    }
                }
            }
            if (openInfo) {
                InfoDialog(
                    deviceStatus = deviceStatus,
                    onDismiss = {
                        openInfo = false
                        isMenuExpanded = true
                    }
                )

            }
        }
    )
}

