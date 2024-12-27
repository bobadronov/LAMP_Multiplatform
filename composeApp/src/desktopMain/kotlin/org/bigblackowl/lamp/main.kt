package org.bigblackowl.lamp

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

fun main() = application {
    initKoin()
    val state = rememberWindowState(size = DpSize(700.dp, 950.dp))
    val icon = painterResource(Res.drawable.on_button)
    Tray(
        icon = icon,
        tooltip= "ESP_LAMP_Multiplatform",
        menu = {
            Item(
                text = "Quit App",
//                icon = Icons.AutoMirrored.Filled.ExitToApp,
                onClick = ::exitApplication
            )
        },
        onAction ={
            state.isMinimized = !state.isMinimized
        }
    )
    Window(
        onCloseRequest = ::exitApplication,
        state = state,
        title = "ESP_LAMP_Multiplatform",
        resizable = false,
        icon = icon,
    ) {
        App()
    }
}