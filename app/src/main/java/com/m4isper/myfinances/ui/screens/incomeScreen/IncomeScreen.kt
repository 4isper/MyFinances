package com.m4isper.myfinances.ui.screens.incomeScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.m4isper.myfinances.BuildConfig
import com.m4isper.myfinances.R
import com.m4isper.myfinances.domain.utils.DateUtils
import com.m4isper.myfinances.domain.utils.calculateSumOfTransactions
import com.m4isper.myfinances.domain.utils.formatWithSpaces
import com.m4isper.myfinances.domain.utils.toCleanDecimal
import com.m4isper.myfinances.ui.components.AddButton
import com.m4isper.myfinances.ui.components.CustomListItem
import com.m4isper.myfinances.ui.theme.MyFinancesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: IncomeViewModel = hiltViewModel()
) {
    val income by viewModel.income.collectAsState()
    val error by viewModel.error.collectAsState()

    val currency by viewModel.currency.collectAsState(initial = "₽")

    val sumOfIncome = calculateSumOfTransactions(income)

    LaunchedEffect(Unit) {
        val today = DateUtils.today()
        viewModel.loadIncome(
            accountId = BuildConfig.ID_ACCOUNT,
            start = today,
            end = today
        )
    }

    income.forEach {
        Log.d("INCOME_ITEM", "${it.comment}: isIncome=${it.isIncome}")
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            CenterAlignedTopAppBar(
                title = { Text("Доходы сегодня", color = MaterialTheme.colorScheme.onSurface) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate("income/history")
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
                    "$sumOfIncome $currency",
                    color = MaterialTheme.colorScheme.onSurface) }
            )

            if (error != null) {
                Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
            }

            LazyColumn {
                items (income) { item ->
                    CustomListItem(
                        modifier = Modifier
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
                            Text(text = item.amount.toCleanDecimal().formatWithSpaces(),
                                color = MaterialTheme.colorScheme.onSurface)
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
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun MyIncomeScreen() {
    MyFinancesTheme {
//        IncomeScreen()
    }
}