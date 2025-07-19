package com.m4isper.myfinances.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerLauncher(
    showDialog: Boolean,
    initialTime: LocalTime,
    onDismiss: () -> Unit,
    onTimeSelected: (LocalTime) -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = true
    )

    if (showDialog) {
        AlertDialog(

            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    val selectedTime = LocalTime.of(state.hour, state.minute)
                    onTimeSelected(selectedTime)
                    onDismiss()
                }) {
                    Text("ОК", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Отмена", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            title = { Text("Выберите время", color = MaterialTheme.colorScheme.onSurface) },
            text = {
                TimePicker(
                    state = state,
                    colors = TimePickerDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.secondary,

                    )
                )
            }
        )
    }
}

