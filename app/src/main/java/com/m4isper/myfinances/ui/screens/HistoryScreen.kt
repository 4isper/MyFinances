package com.m4isper.myfinances.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.m4isper.myfinances.R
import com.m4isper.myfinances.domain.incomeDemo
import com.m4isper.myfinances.domain.models.TransactionModel
import com.m4isper.myfinances.ui.CustomListItem
import com.m4isper.myfinances.ui.DatePickerLauncher
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    dataDemo: List<TransactionModel>
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                title = { Text("Моя история", color = MaterialTheme.colorScheme.onSurface) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_analysis),
                            contentDescription = "Period",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
            )

            val today = remember { LocalDate.now() }
            val monthStart = remember { today.withDayOfMonth(1) }

            var fromDate by rememberSaveable { mutableStateOf(monthStart) }
            var toDate by rememberSaveable { mutableStateOf(today) }

            DatePickerListItem(
                title = "Начало",
                initialDate = fromDate,
                onDateChange = { fromDate = it }
            )
            DatePickerListItem(
                title = "Конец",
                initialDate = toDate,
                onDateChange = { toDate = it }
            )
//            CustomListItem(
//                modifier = Modifier
//                    .background(MaterialTheme.colorScheme.secondary),
//                title = "Начало",
//                trail = {
//                    Text("Февраль 2025", color = MaterialTheme.colorScheme.onSurface)
//                }
//            )
//
//            CustomListItem(
//                modifier = Modifier
//                    .background(MaterialTheme.colorScheme.secondary),
//                title = "Конец",
//                trail = { Text("23:41", color = MaterialTheme.colorScheme.onSurface) }
//            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary),
                title = "Сумма",
                trail = { Text("125 868 ₽", color = MaterialTheme.colorScheme.onSurface) }
            )

            LazyColumn {
                items (dataDemo) { item ->
                    CustomListItem(
                        lead = {
                            if (item.category.emoji != ""){
                                Box(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
                                        .size(24.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(text = item.category.emoji)
                                }
                            }
                        },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(vertical = 10.dp),
                        title = item.category.name,
                        subtitle = item.comment,
                        trail = {
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(text = item.amount + " " + item.account.currency, color = MaterialTheme.colorScheme.onSurface)
                                Text(text = "22:01", color = MaterialTheme.colorScheme.onSurface)
                            }
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowRight,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DatePickerListItem(
    title: String,
    initialDate: LocalDate = LocalDate.now(),
    onDateChange: (LocalDate) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedDate by rememberSaveable { mutableStateOf(initialDate) }

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
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary),
        title = title,
        trail = {
            Box(
                modifier = Modifier
                    .clickable { showDialog = true }
            ) {
                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
                        .replaceFirstChar { it.titlecase() },
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}
