package com.m4isper.myfinances.ui.screens.expensesScreen

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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.m4isper.myfinances.BuildConfig
import com.m4isper.myfinances.ui.theme.MyFinancesTheme
import com.m4isper.myfinances.R
import com.m4isper.myfinances.domain.utils.DateUtils
import com.m4isper.myfinances.domain.utils.calculateSumOfTransactions
import com.m4isper.myfinances.domain.utils.formatWithSpaces
import com.m4isper.myfinances.domain.utils.toCleanDecimal
import com.m4isper.myfinances.ui.components.AddButton
import com.m4isper.myfinances.ui.components.CustomListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModelFactory: ViewModelProvider.Factory
) {
    val viewModel: ExpensesViewModel = viewModel(factory = viewModelFactory)

    val expenses by viewModel.expenses.collectAsState()
    val error by viewModel.error.collectAsState()

    val currency by viewModel.currency.collectAsState(initial = "₽")

    val sumOfExpenses = calculateSumOfTransactions(expenses)

    LaunchedEffect(Unit) {
        val today = DateUtils.today()
        viewModel.loadExpenses(
            accountId = BuildConfig.ID_ACCOUNT,
            start = today,
            end = today
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            CenterAlignedTopAppBar(
                title = { Text("Расходы сегодня", color = MaterialTheme.colorScheme.onSurface) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate("expenses/history")
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_history),
                            contentDescription = "History",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary),
                title = "Всего",
                trail = { Text(
                    "$sumOfExpenses $currency",
                    color = MaterialTheme.colorScheme.onSurface) }
            )

            if (error != null) {
                Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
            }

            LazyColumn {
                items (expenses) { item ->
                    CustomListItem(
                        modifier = Modifier
                            .clickable(onClick = {
                                navController.navigate("transaction/${item.id}")
                            }
                            )
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(vertical = 10.dp),
                        lead = {
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
                                    .size(24.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(text = item.emoji)
                            }
                        },
                        title = item.categoryName,
                        subtitle = item.comment,
                        trail = {
                            Text(
                                text = item.amount.toCleanDecimal().formatWithSpaces()+ " " +item.currency,
                                color = MaterialTheme.colorScheme.onSurface
                            )
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
        AddButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = {
                navController.navigate("transaction/-1")
            }
        )
    }
}

@Preview
@Composable
private fun MyIncomeScreen() {
    MyFinancesTheme {
//        ExpensesScreen()
    }
}

@Preview()
@Composable
private fun MyCustomListItem() {
    MyFinancesTheme {
        CustomListItem(
            modifier = Modifier
                .background(Color.White)
                .padding(vertical = 10.dp),
            lead = {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
                        .size(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "\uD83C\uDFE1")
                }
            },
            title = "На собачку",
            subtitle = "Ремонт - фурнитура для дверей",
            trail = {
                Text("100 000 ₽")
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowRight,
                    contentDescription = "",
                    tint = LocalContentColor.current.copy(alpha = 0.5f)
                )
            }
        )
    }
}