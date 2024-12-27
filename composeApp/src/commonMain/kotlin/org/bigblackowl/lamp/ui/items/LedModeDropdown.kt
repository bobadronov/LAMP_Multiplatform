package org.bigblackowl.lamp.ui.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LedModeDropdown(
    currentMode: Int, modes: List<String>, onModeSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedModeIndex by remember { mutableStateOf(currentMode) }
    Button(
        onClick = {
            expanded = true
        },
        modifier = Modifier.fillMaxWidth(.7f)
    ) {
        Text(text = "Mode: ${modes[selectedModeIndex]}")
        // Выпадающее меню
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(.4f)
        ) {
            modes.forEachIndexed { index, mode ->
                DropdownMenuItem(text = {
                    Text(text = mode, modifier = Modifier.wrapContentSize(Alignment.Center))
                }, onClick = {
                    selectedModeIndex = index
                    onModeSelected(index)
                    expanded = false
                }, modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}