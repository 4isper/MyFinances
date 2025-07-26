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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DatePickerListItem(
    modifier: Modifier = Modifier,
    title: String,
    initialDate: LocalDate = LocalDate.now(),
    onDateChange: (LocalDate) -> Unit,
    dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy"),
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedDate by rememberSaveable { mutableStateOf(initialDate) }

    LaunchedEffect(initialDate) {
        selectedDate = initialDate
    }

    DatePickerLauncher(
        showDialog = showDialog,
        initialDate = selectedDate,
        onDismiss = { showDialog = false },
        onDateSelected = {
            selectedDate = it
            onDateChange(it)
        }
    )

    CustomListItem(
        modifier = modifier,
        title = title,
        trail = {
            Box(
                modifier = Modifier
                    .clickable { showDialog = true }
            ) {
                Text(
                    text = selectedDate.format(dateFormatter)
                        .replaceFirstChar { it.titlecase() },
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}