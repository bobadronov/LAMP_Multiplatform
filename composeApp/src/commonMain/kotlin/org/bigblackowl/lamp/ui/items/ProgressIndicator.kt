package org.bigblackowl.lamp.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import lamp_multiplatform.composeapp.generated.resources.Res
import lamp_multiplatform.composeapp.generated.resources.microscheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProgressIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(Res.drawable.microscheme),
                colorFilter = ColorFilter.tint(Color.White),
                contentScale = ContentScale.FillBounds,
                alpha = .15f,
            )
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(80.dp),
            color = Color.Magenta,
            strokeWidth = 2.dp,
            strokeCap = StrokeCap.Round
        )
    }
}