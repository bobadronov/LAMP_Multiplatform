package org.bigblackowl.lamp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import lamp_multiplatform.composeapp.generated.resources.Res
import lamp_multiplatform.composeapp.generated.resources.eye_off
import lamp_multiplatform.composeapp.generated.resources.eye_show
import org.bigblackowl.lamp.core.network.WiFiNetwork
import org.bigblackowl.lamp.data.ConnectionState
import org.bigblackowl.lamp.data.SetupEspCredential
import org.bigblackowl.lamp.ui.navigation.ScreensRoute
import org.bigblackowl.lamp.ui.theme.neonTextFieldColors
import org.bigblackowl.lamp.ui.viewmodel.LedControlViewModel
import org.bigblackowl.lamp.ui.viewmodel.WifiViewModel
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupESPScreen(
    ledControlViewModel: LedControlViewModel,
    wifiViewModel: WifiViewModel,
    navController: NavHostController,
) {
    var expanded by remember { mutableStateOf(false) }
    var ssid by remember { mutableStateOf(TextFieldValue("")) }
    var showPass by remember { mutableStateOf(false) }
    var pass by remember { mutableStateOf(TextFieldValue("")) }
    var ledCount by remember { mutableStateOf(TextFieldValue("10")) }
    var deviceName by remember { mutableStateOf(TextFieldValue("MAIN_LED")) }

    val networks by wifiViewModel.wifiNetworks.collectAsState()
    val connectionState by ledControlViewModel.connectionState.collectAsState()
    val isLedCountValid = remember(ledCount.text) { validateLedCount(ledCount.text) }

    LaunchedEffect(Unit) {
        wifiViewModel.loadNetworks()
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Setup device", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    Text(
                        text = if (connectionState.state) "Device is online" else "Device is offline",
                        color = if (connectionState.state) Color.Green else Color.Red,
                        modifier = Modifier.padding(end = 15.dp)
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.DarkGray)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
//                containerColor = Color.Gray,
                contentColor = Color.White
            ) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    modifier = Modifier.padding(vertical = 20.dp).fillMaxSize(),
                ) {
                    Text("Wi-Fi Setup")
                }
                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    modifier = Modifier.padding(vertical = 20.dp).fillMaxSize(),
                ) {
                    Text("LED Setup")
                }
            }

            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> {
                        WiFiSetupTab(
                            ssid = ssid,
                            onSsidChange = { ssid = it },
                            pass = pass,
                            onPassChange = { pass = it },
                            showPass = showPass,
                            onTogglePassVisibility = { showPass = !showPass },
                            networks = networks,
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                            onSsidSelected = { ssid = it; expanded = false },
                            focusManager = focusManager,
                            keyboardController = keyboardController,
                            deviceName = deviceName,
                            onDeviceNameChange = { deviceName = it },
                            connectionState = connectionState,
                            onSetup = {
                                ledControlViewModel.setupESP(
                                    SetupEspCredential(ssid.text, pass.text, deviceName.text)
                                )
                                navController.navigate(ScreensRoute.MdnsScreensRoute.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            },
                            navController = navController
                        )
                    }

                    1 -> {
                        LedSetupTab(
                            ledCount = ledCount,
                            onLedCountChange = { ledCount = it },
                            isLedCountValid = isLedCountValid,
                            onSetLedCount = {
                                ledControlViewModel.setLedCount(ledCount.text.toIntOrNull() ?: 10)
                                navController.navigate(ScreensRoute.MdnsScreensRoute.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LedSetupTab(
    ledCount: TextFieldValue,
    onLedCountChange: (TextFieldValue) -> Unit,
    isLedCountValid: Boolean,
    onSetLedCount: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = ledCount,
            onValueChange = { if (it.text.all { char -> char.isDigit() }) onLedCountChange(it) },
            label = { Text("Led count (1 to 1800)") },
            isError = !isLedCountValid,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            colors = neonTextFieldColors
        )

        Spacer(Modifier.height(10.dp))

        if (!isLedCountValid) {
            Text(
                text = "Please enter a valid number between 1 and 1800.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(20.dp))

        Button(onClick = onSetLedCount, enabled = isLedCountValid) {
            Text("Set LED Count")
        }
    }
}

private fun validateLedCount(input: String): Boolean {
    return input.toIntOrNull()?.let { it in 1..1800 } ?: false
}

@Composable
fun WiFiSetupTab(
    ssid: TextFieldValue,
    onSsidChange: (TextFieldValue) -> Unit,
    pass: TextFieldValue,
    onPassChange: (TextFieldValue) -> Unit,
    showPass: Boolean,
    onTogglePassVisibility: () -> Unit,
    networks: List<WiFiNetwork>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onSsidSelected: (TextFieldValue) -> Unit,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?,
    deviceName: TextFieldValue,
    onDeviceNameChange: (TextFieldValue) -> Unit,
    connectionState: ConnectionState,
    onSetup: () -> Unit,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // SSID Input Field
        TextField(
            value = ssid,
            onValueChange = onSsidChange,
            isError = ssid.text.isBlank() || ssid.text.length < 2,
            label = { Text("SSID") },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            trailingIcon = {
                IconButton(onClick = { onExpandedChange(true) }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Network")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
                    networks.forEach { network ->
                        DropdownMenuItem(
                            text = { Text(network.ssid.replace("\"", "")) },
                            onClick = {
                                onSsidSelected(TextFieldValue(network.ssid.replace("\"", "")))
                            }
                        )
                    }
                }
            },
            colors = neonTextFieldColors
        )
        Text("*use only 2.4G networks", color = Color.White, fontSize = 12.sp)

        Spacer(Modifier.height(20.dp))

        // Password Input Field
        TextField(
            value = pass,
            onValueChange = onPassChange,
            label = { Text("Password") },
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            ),
            trailingIcon = {
                IconButton(onClick = onTogglePassVisibility) {
                    Icon(
                        painterResource(if (showPass) Res.drawable.eye_off else Res.drawable.eye_show),
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
            isError = pass.text.isBlank() || pass.text.length < 8,
            colors = neonTextFieldColors
        )
        Text(
            "*password length must be more than 8 characters",
            color = Color.White,
            fontSize = 12.sp
        )

        Spacer(Modifier.height(20.dp))

        // Device Name Input Field
        TextField(
            value = deviceName,
            onValueChange = onDeviceNameChange,
            label = { Text("Device Name") },
            isError = deviceName.text.isBlank() || deviceName.text.length < 3,
            colors = neonTextFieldColors
        )

        Spacer(Modifier.height(100.dp))

        // Buttons
        if (connectionState.state && pass.text.isNotBlank() && pass.text.length >= 8 && ssid.text.isNotBlank() && ssid.text.length >= 2) {
            Button(onClick = onSetup) {
                Text("Setup Wi-Fi")
            }
        } else {
            Button(onClick = { navController.popBackStack() }) {
                Text("Go Back")
            }
        }
    }
}
