package org.bigblackowl.lamp.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FoundDeviceItem(deviceName: String, ip: String?, onClick: () -> Unit) {

    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardColors(
            containerColor = Color.Gray,
            contentColor = Color.Unspecified,
            disabledContainerColor = Color.Unspecified,
            disabledContentColor = Color.Unspecified
        ),
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        onClick = onClick
    ) {
        Row(
            Modifier.fillMaxWidth().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                if (ip.isNullOrBlank()) {
                    Text("Name:  $deviceName")
                } else {
                    Text("Name:  $deviceName")
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("IP:        $ip")
                }
            }
            Icon(
                Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}