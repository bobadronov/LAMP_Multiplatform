package org.bigblackowl.lamp.ui.items.topAppBars

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MdnsScreenTopAppBar() {

    TopAppBar(title = {
        Text(
            "Devices on the local network:",
            modifier = Modifier.padding(10.dp),
            color = Color.White,
            fontSize = 22.sp
        )
    })
}