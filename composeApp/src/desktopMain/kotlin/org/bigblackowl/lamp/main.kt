package org.bigblackowl.lamp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import lamp_multiplatform.composeapp.generated.resources.Res
import lamp_multiplatform.composeapp.generated.resources.on_button
import org.bigblackowl.lamp.di.initKoin
import org.jetbrains.compose.resources.painterResource
import org.koin.core.context.GlobalContext


fun main() = application(exitProcessOnExit = false) {
    if (GlobalContext.getOrNull() == null) {
        initKoin()
    }
    val state = rememberWindowState(size = DpSize(600.dp, 800.dp))
    var isWindowVisible by remember { mutableStateOf(true) }
    var closeState by remember { mutableStateOf(false) }

    Tray(
        icon = painterResource(Res.drawable.on_button),
        tooltip = "ESP_LAMP_Multiplatform",
        menu = {

            Item(
                text = "Show",
                onClick = {
                    isWindowVisible = true
                    state.isMinimized = false
                }
            )

            Separator()

            CheckboxItem(
                text = "Stay in tray",
                checked = closeState,
                onCheckedChange = { closeState = it }
            )

            Separator()

            Item(
                text = "Exit",
//                icon = painterResource(Res.drawable.on_button),
//                enabled = true,
                onClick = ::exitApplication
            )
        },
        onAction = {
            if (state.isMinimized) {
                state.isMinimized = false
                isWindowVisible = true
            } else {
                isWindowVisible = !isWindowVisible
                if (isWindowVisible) {
                    state.isMinimized = false
                }
            }
        }

    )
    Window(
        onCloseRequest = {
            if (closeState) {
                isWindowVisible = false
            } else {
                exitApplication()
            }
        },
        state = state,
        visible = isWindowVisible,
        title = "ESP_LAMP_Multiplatform",
        resizable = false,
        icon = painterResource(Res.drawable.on_button),
    ) {

        MaterialTheme {
            App()
        }
    }
}
