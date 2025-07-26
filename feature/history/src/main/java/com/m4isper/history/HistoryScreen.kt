package com.m4isper.history

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.m4isper.domain.BuildConfig
import com.m4isper.resources.R
import com.m4isper.common.DateUtils.extractTime
import com.m4isper.domain.utils.calculateSumOfTransactions
import com.m4isper.domain.utils.formatWithSpaces
import com.m4isper.domain.utils.toCleanDecimal
import com.m4isper.ui.components.CustomListItem
import com.m4isper.ui.components.DatePickerListItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    type: String,
    viewModelFactory: ViewModelProvider.Factory
) {
    val viewModel: HistoryViewModel = viewModel(factory = viewModelFactory)

    val transactions by viewModel.transactions.collectAsState()
    val error by viewModel.error.collectAsState()

    val currency by viewModel.currency.collectAsState(initial = "₽")

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
                title = { Text(
                    text = stringResource(R.string.my_history),
                    color = MaterialTheme.colorScheme.onSurface) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate("${type}/analysis")
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_analysis),
                            contentDescription = "Period",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
            )

            DatePickerListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary),
                title = stringResource(R.string.start),
                initialDate = fromDate,
                onDateChange = { fromDate = it }
            )
            DatePickerListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary),
                title = stringResource(R.string.end),
                initialDate = toDate,
                onDateChange = { toDate = it }
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary),
                title = stringResource(R.string.amount),
                trail = { Text(
                    "$sumOfTransactions $currency",
                    color = MaterialTheme.colorScheme.onSurface) }
            )

            if (error != null) {
                Text("${stringResource(R.string.error)}: $error", color = MaterialTheme.colorScheme.error)
            }

            LazyColumn {
                items (transactions) { item ->
                    CustomListItem(
                        lead = {
                            if (item.emoji != ""){
                                Box(
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colorScheme.secondary,
                                            shape = CircleShape
                                        )
                                        .size(24.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(text = item.emoji)
                                }
                            }
                        },
                        modifier = Modifier
                            .clickable(onClick = {
                                navController.navigate("transaction/${item.id}")
                            }
                            )
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(vertical = 10.dp),
                        title = item.categoryName,
                        subtitle = item.comment,
                        trail = {
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(text = item.amount.toCleanDecimal().formatWithSpaces()
                                        + " "+ item.currency,
                                    color = MaterialTheme.colorScheme.onSurface)
                                Text(
                                    text = extractTime(item.transactionDate)
                                        .format(DateTimeFormatter.ofPattern("HH:mm")),
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
