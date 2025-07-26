package com.m4isper.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TimePickerListItem(
    modifier: Modifier = Modifier,
    title: String,
    initialTime: LocalTime = LocalTime.now(),
    onTimeChange: (LocalTime) -> Unit,
    timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedTime by rememberSaveable { mutableStateOf(initialTime) }

    LaunchedEffect(initialTime) {
        selectedTime = initialTime
    }

    TimePickerLauncher(
        showDialog = showDialog,
        initialTime = selectedTime,
        onDismiss = { showDialog = false },
        onTimeSelected = {
            selectedTime = it
            onTimeChange(it)
        }
    )

    CustomListItem(
        modifier = modifier,
        title = title,
        trail = {
            Box(modifier = Modifier.clickable { showDialog = true }) {
                Text(
                    text = selectedTime.format(timeFormatter),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}
