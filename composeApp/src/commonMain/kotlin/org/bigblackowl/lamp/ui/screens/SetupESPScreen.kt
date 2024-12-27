package org.bigblackowl.lamp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import lamp_multiplatform.composeapp.generated.resources.Res
import lamp_multiplatform.composeapp.generated.resources.eye_off
import lamp_multiplatform.composeapp.generated.resources.eye_show
import org.bigblackowl.lamp.data.SetupEspCredential
import org.bigblackowl.lamp.ui.navigation.ScreensRoute
import org.bigblackowl.lamp.ui.theme.neonTextFieldColors
import org.bigblackowl.lamp.ui.viewmodel.LedControlViewModel
import org.bigblackowl.lamp.ui.viewmodel.SetupDeviceViewModel
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupESPScreen(
    ledControlViewModel: LedControlViewModel,
    setupDeviceViewModel: SetupDeviceViewModel,
    navController: NavHostController,
) {
    // State to hold SSID and Password values
    var ssid by remember { mutableStateOf(TextFieldValue("")) }
    var pass by remember { mutableStateOf(TextFieldValue("")) }
    var deviceName by remember { mutableStateOf(TextFieldValue("ESP_LED")) }
    var showPass by remember { mutableStateOf(false) }
    val networks by setupDeviceViewModel.wifiNetworks.collectAsState()
    val connectionState by ledControlViewModel.connectionState.collectAsState()
    var expanded by remember { mutableStateOf(false) } // State for DropdownMenu
    LaunchedEffect(Unit) {
        setupDeviceViewModel.loadNetworks()
    }
    val focusManager = LocalFocusManager.current // To manage focus transitions
    val keyboardController = LocalSoftwareKeyboardController.current // For controlling the keyboard

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(colors = TopAppBarColors(
                containerColor = Color.DarkGray,
                scrolledContainerColor = Color.Unspecified,
                navigationIconContentColor = Color.Unspecified,
                titleContentColor = Color.Unspecified,
                actionIconContentColor = Color.Unspecified
            ), title = {
                Text(
                    "Setup device", color = Color.White
                )
            }, navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        Icons.AutoMirrored.Rounded.KeyboardArrowLeft, contentDescription = null
                    )
                }
            }, actions = {
                if (connectionState.state) {
                    Text(
                        "Device is " + if (connectionState.state) "online" else "offline",
                        modifier = Modifier.padding(end = 15.dp),
                        color = if (connectionState.state) Color.Green else Color.Red,
//                        fontSize = 16.sp
                    )
                }
            })
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // SSID Input Field
            TextField(
                value = ssid,
                onValueChange = {
                    ssid = it
                },
                isError = ssid.text.isBlank() || ssid.text.length < 2,
                label = { Text("SSID") },
                readOnly = false, // Prevent manual input
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Network")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        networks.forEach { network ->
                            DropdownMenuItem(
                                text = { Text(network.ssid.replace("\"", "")) },
                                onClick = {
                                    ssid = TextFieldValue(
                                        network.ssid.replace(
                                            "\"",
                                            ""
                                        )
                                    ) // Set selected SSID
                                    expanded = false
                                })
                        }
                    }
                },
                colors = neonTextFieldColors
            )
            Text("*use ony 2.4G networks", color = Color.White, fontSize = 12.sp)
            Spacer(Modifier.height(20.dp))
            // Password Input Field
            TextField(
                value = pass,
                onValueChange = { pass = it }, // Update pass when text changes
                label = { Text("Password") },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus() // Clear focus to hide the keyboard
                        keyboardController?.hide() // Explicitly hide the keyboard
                    }
                ),
                trailingIcon = {
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(
                            painterResource(if (showPass) Res.drawable.eye_off else Res.drawable.eye_show),
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(), // Optional, for password masking
                isError = pass.text.isBlank() || pass.text.length < 8,
                colors = neonTextFieldColors
            )
            Text(
                "*password length must be more than 8 characters",
                color = Color.White,
                fontSize = 12.sp
            )
            Spacer(Modifier.height(20.dp))

            TextField(
                value = deviceName,
                onValueChange = { deviceName = it }, // Update pass when text changes
                label = { Text("Device Name") },
                isError = deviceName.text.isBlank() || deviceName.text.length < 3,
                colors = neonTextFieldColors
            )
            Spacer(Modifier.height(100.dp))
            if (connectionState.state && pass.text.isNotBlank() && pass.text.length >= 8 && ssid.text.isNotBlank() && ssid.text.length >= 2) {
                Button(
                    onClick = {
                        ledControlViewModel.setupESP(
                            SetupEspCredential(
                                ssid = ssid.text, password = pass.text, deviceName = deviceName.text
                            )
                        )
                        navController.navigate(ScreensRoute.MdnsScreensRoute.route) {
                            popUpTo(0) { inclusive = true } // Очищает весь back stack
                        }
                    }
                ) {
                    Text("Setup Wifi")
                }
            } else {
                Button(onClick = {
                    navController.popBackStack()
                }) {
                    Text("Go Back")
                }
            }
        }
    }
}
