package org.bigblackowl.lamp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lamp_multiplatform.composeapp.generated.resources.Res
import lamp_multiplatform.composeapp.generated.resources.on_button
import org.bigblackowl.lamp.data.util.parseHexColor
import org.bigblackowl.lamp.data.util.toHexString
import org.bigblackowl.lamp.ui.items.LedModeDropdown
import org.bigblackowl.lamp.ui.items.ProgressIndicator
import org.bigblackowl.lamp.ui.items.dialog.TimerDialog
import org.bigblackowl.lamp.ui.items.topAppBars.ControlScreenTopAppBar
import org.bigblackowl.lamp.ui.mode.ColorControlMode
import org.bigblackowl.lamp.ui.mode.CustomMode
import org.bigblackowl.lamp.ui.mode.FlagMode
import org.bigblackowl.lamp.ui.mode.RainbowMode
import org.bigblackowl.lamp.ui.mode.SetBrightlessMode
import org.bigblackowl.lamp.ui.viewmodel.LedControlViewModel
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt


@Composable
fun ControlScreen(
    ledControlViewModel: LedControlViewModel, navController: NavHostController, deviceName: String
) {
    LaunchedEffect(Unit) {
        if (deviceName.isNotEmpty()) ledControlViewModel.connect(deviceName)
        else navController.popBackStack()
    }

    val connectionState by ledControlViewModel.connectionState.collectAsState()
    val deviceStatus by ledControlViewModel.deviceStatus.collectAsState()
    var isTimerWindowOpen by remember { mutableStateOf(false) }
    var ledState by remember { mutableStateOf(false) }
    var color by remember { (mutableStateOf(Color.White)) }
    var flagSpeed by remember { mutableStateOf(1f) }
    var rainbowSpeed by remember { mutableStateOf(1f) }
    var commonBrightness by remember { mutableStateOf(255f) }
    LaunchedEffect(deviceStatus) {
        ledState = deviceStatus.ledState
        color = parseHexColor(deviceStatus.color)
        flagSpeed = deviceStatus.flagSpeed.toFloat()
        rainbowSpeed = deviceStatus.rainbowSpeed
        commonBrightness = deviceStatus.commonBrightness
    }

    val controller = rememberColorPickerController()
    controller.debounceDuration = 800L

    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ControlScreenTopAppBar(
                deviceName = deviceName,
                deviceStatus = deviceStatus,
                connectionState = connectionState,
                ledState = ledState,
                navController = navController,
                offLedClicked = {
                    ledControlViewModel.setLedState(false)
                },
                onRebootClicked = {
                    ledControlViewModel.reboot()
                }
            )
        }
    ) { newPadding ->

        if (connectionState.state) {
            AnimatedVisibility(
                visible = ledState,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Column(
                    Modifier.padding(newPadding).fillMaxSize().padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    LedModeDropdown(currentMode = deviceStatus.currentMode,
                        modes = deviceStatus.modes,
                        onModeSelected = { index ->
                            ledControlViewModel.setMode(index)
                        })
                    when (deviceStatus.currentMode) {
                        0, 2, 3, 4, 5, 8 -> {
                            key(color) {
                                ColorControlMode(onColorChanged = { colorEnvelope: ColorEnvelope ->
                                    ledControlViewModel.setColor(colorEnvelope.hexCode)
                                }, onColorSelected = { color ->
                                    ledControlViewModel.setColor(color.toHexString())
                                }, initialColor = color, controller = controller
                                )
                            }
                        }

                        1 -> {
                            RainbowMode(
                                speed = deviceStatus.rainbowSpeed,
                                rainbowIsStatic = deviceStatus.rainbowIsStatic,
                                onCheckedChange = { value ->
                                    ledControlViewModel.setRainbowStaticMode(value)
                                },
                                onValueChange = { value ->
                                    rainbowSpeed = value
                                },
                                onValueChangeFinished = {
                                    ledControlViewModel.setRainbowSpeed(rainbowSpeed)
                                },
                                commonBrightness = commonBrightness,
                                onBrightnessChange = { value ->
                                    commonBrightness = value
                                },
                                onBrightnessChangeFinished = {
                                    ledControlViewModel.setCommonBrightness(commonBrightness)
                                },
                            )
                        }

                        6 -> {
                            SetBrightlessMode(
                                commonBrightness = commonBrightness,
                                onBrightnessChange = { value ->
                                    commonBrightness = value
                                },
                                onBrightnessChangeFinished = {
                                    ledControlViewModel.setCommonBrightness(commonBrightness)
                                },
                            )
                        }

                        9 -> {
                            FlagMode(
                                deviceStatus.flagIsStatic,
                                onCheckedChange = { state ->
                                    ledControlViewModel.setFlagState(state)
                                },
                                onValueChange = { value ->
                                    flagSpeed = value
                                },
                                onValueChangeFinished = {
                                    ledControlViewModel.setFlagSpeed(flagSpeed.roundToInt())
                                },
                                commonBrightness = commonBrightness,
                                onBrightnessChange = { value ->
                                    commonBrightness = value
                                },
                                onBrightnessChangeFinished = {
                                    ledControlViewModel.setCommonBrightness(commonBrightness)
                                },
                                flagSpeed = flagSpeed,
                            )
                        }

                        10 -> {
                            CustomMode(
                                numLeds = deviceStatus.NUM_LEDS,
                                onValueChange = { updatedColors ->
//                                    println("ControlScreen Updated Colors: $updatedColors")
                                    ledControlViewModel.setCustomMode(updatedColors)
                                },
                                commonBrightness = commonBrightness,
                                onBrightnessChange = { value ->
                                    commonBrightness = value
                                },
                                onBrightnessChangeFinished = {
                                    ledControlViewModel.setCommonBrightness(commonBrightness)
                                },
                            )
                        }

                        else -> {
                            Text("This mode is in development. Coming soon :)", color = Color.Red)
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (deviceStatus.ledState) {
                            Button(onClick = {
                                isTimerWindowOpen = !isTimerWindowOpen
                            }) {
                                if (deviceStatus.timerIsActive) {
                                    Text(
                                        "Timer set on "
                                                +
                                                "${deviceStatus.timer.hour}".padStart(2, '0')
                                                +
                                                ":"
                                                +
                                                "${deviceStatus.timer.minute}".padStart(2, '0'),
                                        color = Color.Black
                                    )
                                } else {
                                    Text("Set up timer", color = Color.Black)
                                }
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            if (deviceStatus.temperature != 0.0f && deviceStatus.humidity != 0.0f) {
                                Text(
                                    "Temperature: ${deviceStatus.temperature} °C",
                                    color = Color.White
                                )
                                Spacer(Modifier.width(50.dp))
                                Text("Humidity: ${deviceStatus.humidity} %", color = Color.White)
                            } else {
                                Text(
                                    "Temperature and humidity sensor not connected!",
                                    color = Color.Gray,
                                    maxLines = 2,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(
                    visible = !ledState,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    IconButton(
                        onClick = {
                            ledControlViewModel.setLedState(true)
                        },
                        modifier = Modifier.size(160.dp),
                    ) {
                        Icon(
                            painterResource(Res.drawable.on_button),
                            contentDescription = null,
                            modifier = Modifier.size(150.dp),
                            tint = Color.Green,
                        )
                    }
                }
            }
        } else {
            ProgressIndicator(Modifier.fillMaxSize())
            scope.launch {
                delay(4000)
                if (!connectionState.state) navController.popBackStack()
            }
        }
    }

    if (isTimerWindowOpen) {
        TimerDialog(isTimerSetOn = deviceStatus.timerIsActive, onDismissRequest = {
            isTimerWindowOpen = false
        }, onSetTimerClicked = { timer ->
            ledControlViewModel.setTimer(timer)
            isTimerWindowOpen = false
        }, onCancelTimerClicked = {
            ledControlViewModel.cancelTimer()
            isTimerWindowOpen = false
        })
    }
}

