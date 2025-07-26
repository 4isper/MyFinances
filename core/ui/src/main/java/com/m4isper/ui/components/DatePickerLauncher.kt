package com.m4isper.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.m4isper.resources.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerLauncher(
    showDialog: Boolean,
    initialDate: LocalDate,
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    val zoneId = ZoneId.systemDefault()
    val millis = remember(initialDate) {
        initialDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
    }
    val state = rememberDatePickerState(initialSelectedDateMillis = millis)

    if (showDialog) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let {
                        val selected = Instant.ofEpochMilli(it)
                            .atZone(zoneId)
                            .toLocalDate()
                        onDateSelected(selected)
                    }
                    onDismiss()
                }) {
                    Text("ОК", color = MaterialTheme.colorScheme.onSurface)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.onSurface)
                }
            }
        ) {
            DatePicker(
                state = state,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}

