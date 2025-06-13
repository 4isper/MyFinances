package com.m4isper.myfinances.ui.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.m4isper.myfinances.ui.theme.MyFinancesTheme
import com.m4isper.myfinances.R
import com.m4isper.myfinances.domain.expensesDemo
import com.m4isper.myfinances.ui.CustomListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(modifier: Modifier = Modifier) {
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
                    IconButton(onClick = { /* doSomething() */ }) {
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
                trail = { Text("436 558 ₽", color = MaterialTheme.colorScheme.onSurface) }
            )

            LazyColumn {
                items (expensesDemo) { item ->
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
                                Text(text = item.category.emoji)
                            }
                        },
                        title = item.category.name,
                        subtitle = item.comment,
                        trail = {
                            Text(
                                text = item.amount + " " + item.account.currency,
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
        FloatingActionButton(
            onClick = {},
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ){
            Icon(Icons.Rounded.Add, "",
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(30.dp))
        }
    }
}

@Preview
@Composable
private fun MyIncomeScreen() {
    MyFinancesTheme {
        ExpensesScreen()
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