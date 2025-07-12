package com.m4isper.myfinances.ui.screens.transactionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.m4isper.myfinances.domain.utils.DateUtils.extractDate
import com.m4isper.myfinances.domain.utils.DateUtils.extractTime
import com.m4isper.myfinances.ui.components.CustomListItem
import com.m4isper.myfinances.ui.components.DatePickerListItem
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModelFactory: ViewModelProvider.Factory
) {
    val viewModel: TransactionViewModel = viewModel(factory = viewModelFactory)

    val transactionState = viewModel.transaction.collectAsState()
    val transaction = transactionState.value

    val error by viewModel.error.collectAsState()

    val today = remember { LocalDate.now() }
    val monthStart = remember { today.withDayOfMonth(1) }

    var fromDate by rememberSaveable { mutableStateOf(monthStart) }
    var toDate by rememberSaveable { mutableStateOf(today) }

    LaunchedEffect(Unit) {
        viewModel.loadTransactionById()
    }

    var comment: String? by remember { mutableStateOf("") }
    var categoryName by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    var isIncome by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(transaction) {
        if (transaction != null) {
            comment = transaction.comment
            categoryName = transaction.categoryName
            currency = transaction.currency
            amount = transaction.amount
            date =  extractDate(transaction.transactionDate)
            time = extractTime(transaction.transactionDate)
            isIncome = transaction.isIncome
        }
    }

    val titleTextTopAppBar = if (isIncome) "доходы" else "расходы"

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Outlined.Close,
                            contentDescription = "Cancel",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                title = { Text("Мои $titleTextTopAppBar", color = MaterialTheme.colorScheme.onSurface) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            Icons.Outlined.Done,
                            contentDescription = "Done",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 10.dp),
                title = "Счет"
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 10.dp),
                title = "Статья",
                trail = {
                    Text(
                        text = categoryName,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 10.dp),
                title = "Сумма",
                trail = {
                    Text(
                        text = "$amount $currency",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
//
//            DatePickerListItem(
//                title = "Дата",
//                initialDate = fromDate,
//                onDateChange = { fromDate = it }
//            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 10.dp),
                title = "Дата",
                trail = {
                    Text(
                        text = date,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 10.dp),
                title = "Время",
                trail = {
                    Text(
                        text = time,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 10.dp),
                title = comment
            )

            if (error != null) {
                Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
            }

        }
    }
}