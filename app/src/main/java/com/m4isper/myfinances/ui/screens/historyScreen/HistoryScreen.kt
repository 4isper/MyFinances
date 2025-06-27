package com.m4isper.myfinances.ui.screens.historyScreen

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.m4isper.myfinances.BuildConfig
import com.m4isper.myfinances.R
import com.m4isper.myfinances.domain.utils.DateUtils.extractTime
import com.m4isper.myfinances.domain.utils.calculateSumOfTransactions
import com.m4isper.myfinances.domain.utils.formatWithSpaces
import com.m4isper.myfinances.domain.utils.toCleanDecimal
import com.m4isper.myfinances.ui.components.CustomListItem
import com.m4isper.myfinances.ui.components.DatePickerListItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    type: String,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val transactions by viewModel.transactions.collectAsState()
    val error by viewModel.error.collectAsState()

    val today = remember { LocalDate.now() }
    val monthStart = remember { today.withDayOfMonth(1) }

    var fromDate by rememberSaveable { mutableStateOf(monthStart) }
    var toDate by rememberSaveable { mutableStateOf(today) }

    LaunchedEffect(type, fromDate, toDate) {
        val start = fromDate.format(DateTimeFormatter.ISO_DATE)
        val end = toDate.format(DateTimeFormatter.ISO_DATE)

        when (type) {
            "income" -> viewModel.loadIncome(accountId = BuildConfig.ID_ACCOUNT, start = start, end = end)
            "expenses" -> viewModel.loadExpenses(accountId = BuildConfig.ID_ACCOUNT, start = start, end = end)
        }
    }

    val sumOfTransactions = calculateSumOfTransactions(transactions)

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

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary),
                title = "Сумма",
                trail = { Text(sumOfTransactions, color = MaterialTheme.colorScheme.onSurface) }
            )

            if (error != null) {
                Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
            }

            LazyColumn {
                items (transactions) { item ->
                    CustomListItem(
                        lead = {
                            if (item.emoji != ""){
                                Box(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
                                        .size(24.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(text = item.emoji)
                                }
                            }
                        },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(vertical = 10.dp),
                        title = item.categoryName,
                        subtitle = item.comment,
                        trail = {
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(text = item.amount.toCleanDecimal().formatWithSpaces(),
                                    color = MaterialTheme.colorScheme.onSurface)
                                Text(
                                    text = extractTime(item.transactionDate),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
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
