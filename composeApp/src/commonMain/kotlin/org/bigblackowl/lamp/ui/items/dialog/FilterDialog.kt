package org.bigblackowl.lamp.ui.items.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun FilterDialog(
    filterValue: String,
    onDismissRequest: () -> Unit,
    onFilter: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {

        var filter by remember {
            mutableStateOf(
                TextFieldValue(
                    filterValue.ifEmpty { "-LED" },
                    selection = TextRange(filterValue.length)
                )
            )
        }

        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Set filter for search devices",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TextField(
                    value = filter,
                    onValueChange = {
                        filter = TextFieldValue(
                            text = it.text.uppercase(),
                            selection = TextRange(it.text.length) // Set cursor to end
                        )
                    },
                    label = {
                        Text("Filter")
                    },
                    placeholder = {
                        Text("Enter filter")
                    },
                    isError = filter.text.isEmpty(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (filter.text.isNotEmpty()) {
                                onFilter(filter.text)
                                onDismissRequest()
                            }
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onKeyEvent {
                            if (it.key == Key.Enter && filter.text.isNotEmpty()) {
                                onFilter(filter.text)
                                onDismissRequest()
                                true
                            } else {
                                false
                            }
                        },
                )
                if (filter.text.isEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Filter cannot be empty",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    TextButton(onClick = {
                        onFilter(filter.text)
                        onDismissRequest()
                    }) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}
