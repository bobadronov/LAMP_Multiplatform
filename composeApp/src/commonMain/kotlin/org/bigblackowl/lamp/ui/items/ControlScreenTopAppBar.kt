package org.bigblackowl.lamp.ui.items

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import lamp_multiplatform.composeapp.generated.resources.Res
import lamp_multiplatform.composeapp.generated.resources.on_button
import org.bigblackowl.lamp.data.ConnectionState
import org.bigblackowl.lamp.ui.navigation.ScreensRoute
import org.bigblackowl.lamp.ui.viewmodel.LedControlViewModel
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlScreenTopAppBar(
    viewModel: LedControlViewModel,
    connectionState: ConnectionState,
    deviceName: String,
    ledState: Boolean,
    navController: NavHostController
) {
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
                    contentDescription = null
                )
            }
        },
        actions = {
            if (connectionState.state && ledState) {
                IconButton(onClick = {
                    viewModel.setLedState(false)
                }, modifier = Modifier.padding(end = 20.dp)) {
                    Icon(
                        painterResource(Res.drawable.on_button),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        tint = Color.Red,
                    )
                }
            }
            Text(
                if (connectionState.state) "Online" else "Offline",
                color = if (connectionState.state) Color.Green else Color.Red
            )
            if (connectionState.state) {
                IconButton(onClick = {
                    navController.navigate(ScreensRoute.SetupESPScreen.route)
                }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        },
    )
}