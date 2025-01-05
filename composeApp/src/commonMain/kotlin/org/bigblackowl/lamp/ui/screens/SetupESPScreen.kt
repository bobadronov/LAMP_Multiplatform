package org.bigblackowl.lamp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
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
import org.bigblackowl.lamp.ui.viewmodel.MdnsViewModel
import org.bigblackowl.lamp.ui.viewmodel.WifiViewModel
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupESPScreen(
    ledControlViewModel: LedControlViewModel,
    wifiViewModel: WifiViewModel,
    mdnsViewModel: MdnsViewModel,
    navController: NavHostController,
) {
    val deviceStatus by ledControlViewModel.deviceStatus.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var ssid by remember { mutableStateOf(TextFieldValue("")) }
    var showPass by remember { mutableStateOf(false) }
    var pass by remember { mutableStateOf(TextFieldValue("")) }
    var ledCount by remember { mutableStateOf(TextFieldValue(deviceStatus.REAL_NUM_LEDS.toString())) }
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
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
//                containerColor = Color.Gray,
                contentColor = Color.White,
                divider = { VerticalDivider() }
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
                    Text("Wi-Fi Setup", modifier = Modifier.fillMaxSize().wrapContentSize())
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
                    Text("LED Setup", modifier = Modifier.fillMaxSize().wrapContentSize())
                }
            }

            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { page ->
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
                            setupEsp = {
                                ledControlViewModel.setupESP(
                                    SetupEspCredential(ssid.text, pass.text, deviceName.text)
                                )
                                mdnsViewModel.setFilter(deviceName.text)
                                navController.navigate(ScreensRoute.MdnsScreensRoute.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }

                    1 -> {
                        LedSetupTab(
                            ledCount = ledCount,
                            onLedCountChange = { ledCount = it },
                            isLedCountValid = isLedCountValid,
                            onSetLedCount = {
                                ledControlViewModel.setLedCount(ledCount.text.toIntOrNull() ?: 10)
//                                navController.navigate(ScreensRoute.MdnsScreensRoute.route) {
//                                    popUpTo(0) { inclusive = true }
//                                }
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(30.dp))

            // Buttons
            Button(onClick = { navController.popBackStack() }) {
                Text("Go Back")
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
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = ledCount,
            onValueChange = { if (it.text.all { char -> char.isDigit() }) onLedCountChange(it) },
            modifier = Modifier.wrapContentSize(),
            label = { Text("Led count (10 to 1800)") },
            isError = !isLedCountValid,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            colors = neonTextFieldColors
        )
        ledCount.text.toIntOrNull()?.toFloat()?.let {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally)
            ) {
                Text("10")
                Slider(
                    value = it,
                    onValueChange = { value ->
                        onLedCountChange(TextFieldValue("${value.toInt()}"))
                    },
                    modifier = Modifier.fillMaxWidth(.85f),
                    valueRange = 10f..1800f, // Значения диапазона
                    onValueChangeFinished = {},
                )
                Text("1800")
            }
        }

        if (!isLedCountValid) {
            Text(
                text = "Please enter a valid number between 1 and 1800.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = onSetLedCount,
            modifier = Modifier.padding(bottom = 30.dp),
            enabled = isLedCountValid
        ) {
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
    setupEsp: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
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
        Button(
            onClick = setupEsp,
            enabled = connectionState.state && pass.text.isNotBlank() && pass.text.length >= 8 && ssid.text.isNotBlank() && ssid.text.length >= 2
        ) {
            Text("Setup Wi-Fi")
        }
    }
}
