package org.bigblackowl.lamp.ui.items.dialog

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.bigblackowl.lamp.ui.theme.neonTextFieldColors

@Composable
fun LedCountDialog(onSetLedCountClicked: (Int) -> Unit) {
    var ledCount by remember { mutableStateOf(TextFieldValue("10")) }


    TextField(
        value = ledCount,
        onValueChange = {
            // Обновляем значение, проверяем числовой ввод
            if (it.text.all { char -> char.isDigit() }) {
                ledCount = it
            }
        },
        label = { Text("Led count (up to 1800 LEDs)") },
        isError = ledCount.text.toIntOrNull()?.let { it < 1 || it > 1800 } ?: true,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        colors = neonTextFieldColors
    )
    Spacer(Modifier.height(20.dp))
    if (ledCount.text.toIntOrNull()?.let { it < 1 || it > 1800 } == true) {
        Text(
            text = "Please enter a number between 1 and 1800.",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
    Spacer(Modifier.height(20.dp))
    Button(onClick = { onSetLedCountClicked(ledCount.text.toIntOrNull() ?: 10) }) {
        Text("Set Led count")
    }
}