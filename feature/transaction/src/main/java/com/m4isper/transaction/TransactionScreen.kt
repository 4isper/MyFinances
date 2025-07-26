package com.m4isper.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.m4isper.common.DateUtils.combineDateTimeToIsoUtc
import com.m4isper.common.DateUtils.extractDate
import com.m4isper.common.DateUtils.extractTime
import com.m4isper.ui.components.CustomBasicTextField
import com.m4isper.ui.components.CustomListItem
import com.m4isper.ui.components.DatePickerListItem
import com.m4isper.ui.components.TimePickerListItem
import com.m4isper.resources.R
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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

    val categories by viewModel.categories.collectAsState()
    val currency by viewModel.currency.collectAsState(initial = "₽")

    val account by viewModel.account.collectAsState()

    val error by viewModel.error.collectAsState()

    val today = remember { LocalDate.now() }
    val timeNow = remember { LocalTime.now() }

    LaunchedEffect(Unit) {
        if (viewModel.transactionId != -1)
            viewModel.loadTransactionById()
        viewModel.loadAccount()
        viewModel.loadCategories()
    }

    val categoryPairs = categories.map { it.name to it.id}

    var comment: String? by remember { mutableStateOf("") }
    var categoryName by remember { mutableStateOf("") }
    var categoryId by remember { mutableIntStateOf(0) }
    var amount by remember { mutableStateOf("0") }
    var date by remember { mutableStateOf(today) }
    var time by remember { mutableStateOf(timeNow) }

    var isIncome by remember { mutableStateOf(true) }

    var expanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(transaction) {
        if (transaction != null) {
            comment = transaction.comment
            categoryName = transaction.categoryName
            categoryId = transaction.categoryId
            amount = transaction.amount
            date =  extractDate(transaction.transactionDate)
            time = extractTime(transaction.transactionDate)
            isIncome = transaction.isIncome
        }
    }

    val titleTextTopAppBar = if (isIncome)
        stringResource(R.string.my_incomes) else stringResource(R.string.my_expenses)

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
                title = { Text(titleTextTopAppBar, color = MaterialTheme.colorScheme.onSurface) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        if (viewModel.transactionId == -1)
                            scope.launch {
                                val result = viewModel.addTransaction(
                                    categoryId = categoryId,
                                    amount = amount,
                                    transactionDate = combineDateTimeToIsoUtc(date, time),
                                    comment = comment
                                )
                                if (result) {
                                    navController.popBackStack()
                                } else {
                                    // можно показать Snackbar или ошибку
                                }
                            }
                        if (viewModel.transactionId != -1){
                            scope.launch {
                                val result = viewModel.updateTransaction(
                                    categoryId = categoryId,
                                    amount = amount,
                                    transactionDate = combineDateTimeToIsoUtc(date, time),
                                    comment = comment
                                )
                                if (result) {
                                    navController.popBackStack()
                                } else {
                                    // можно показать Snackbar или ошибку
                                }
                            }
                        }
                    }) {
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
                title = stringResource(R.string.account),
                trail = {
                    account?.name?.let { text ->
                        Text(
                            text = text,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 10.dp)
                    .clickable { expanded = true }
                ,
                title = stringResource(R.string.category),
                trail = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = categoryName,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Icon(
                            imageVector = Icons.Rounded.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categoryPairs.forEach {
                            DropdownMenuItem(
                                text = { Text(it.first) },
                                onClick = {
                                    categoryName = it.first
                                    categoryId = it.second
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 10.dp),
                title = stringResource(R.string.amount),
                trail = {
                    CustomBasicTextField(
                        text = amount,
                        onValueChange = { amount = it },
                        textPlaceholder = stringResource(R.string.enter_amount),
                        trail = {
                            Text(
                                text = " $currency",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
            )

            DatePickerListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 10.dp),
                title = stringResource(R.string.date),
                initialDate = date,
                onDateChange = { date = it },
                dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy"),
            )

            TimePickerListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 10.dp),
                title = stringResource(R.string.time),
                initialTime = time,
                onTimeChange = { time = it }
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(vertical = 10.dp),
                lead = {
                    CustomBasicTextField(
                        text = comment,
                        onValueChange = { comment = it },
                        textPlaceholder = stringResource(R.string.enter_comment),
                        textAlign = TextAlign.Start
                    )
                },
                title = null,
            )

            if (viewModel.transactionId != -1){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 20.dp)
                        .size(50.dp)
                        .background(color = MaterialTheme.colorScheme.surface)
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            scope.launch {
                                val result = viewModel.deleteTransaction()
                                if (result) {
                                    navController.popBackStack()
                                } else {
                                    // можно показать Snackbar или ошибку
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.delete_transaction),
                            color = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }

            if (error != null) {
                Text("${stringResource(R.string.error)}: $error", color = MaterialTheme.colorScheme.error)
            }

        }
    }
}