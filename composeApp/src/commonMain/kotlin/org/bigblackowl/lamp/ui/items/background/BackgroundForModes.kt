package org.bigblackowl.lamp.ui.items.background

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import lamp_multiplatform.composeapp.generated.resources.Res
import lamp_multiplatform.composeapp.generated.resources.fire
import lamp_multiplatform.composeapp.generated.resources.meteor
import lamp_multiplatform.composeapp.generated.resources.microscheme
import lamp_multiplatform.composeapp.generated.resources.rainbow
import lamp_multiplatform.composeapp.generated.resources.snake
import lamp_multiplatform.composeapp.generated.resources.snow
import lamp_multiplatform.composeapp.generated.resources.ukrain_country_map
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun BackgroundForModes(currentMode: Int, ledState: Boolean) {
    if (!ledState) {
        return
    }
    val drawable: DrawableResource
    val alpha: Float
    var colorFilter: ColorFilter? = null
    when (currentMode) {
        1 -> {
            drawable = Res.drawable.rainbow
            alpha = 0.15f
        }

        3 -> {
            drawable = Res.drawable.snow
            alpha = 0.15f
            colorFilter = ColorFilter.tint(Color.White)
        }

        4 -> {
            drawable = Res.drawable.snake
            alpha = 0.3f
            colorFilter = ColorFilter.tint(Color.White)
        }

        5 -> {
            drawable = Res.drawable.fire
            alpha = 0.15f
        }

        8 -> {
            drawable = Res.drawable.meteor
            alpha = 0.15f
            colorFilter = ColorFilter.tint(Color.White)
        }

        9 -> {
            drawable = Res.drawable.ukrain_country_map
            alpha = 0.15f
        }

        else -> {
            drawable = Res.drawable.microscheme
            alpha = 0.15f
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(drawable),
                colorFilter = colorFilter,
                contentScale = ContentScale.FillBounds,
                alpha = alpha,
            )
    )
}
