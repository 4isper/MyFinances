package com.m4isper.myfinances.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.m4isper.myfinances.domain.accountDemo
import com.m4isper.myfinances.ui.CustomListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            CenterAlignedTopAppBar(
                title = { Text("Мой счет", color = MaterialTheme.colorScheme.onSurface) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = "History",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary),
                lead = {
                    Box(
                        modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                            .size(24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "\uD83D\uDCB0")
                    }
                },
                title = "Баланс",
                trail = {
                    Text(
                        text = accountDemo.balance + " " + accountDemo.currency,
                        color = MaterialTheme.colorScheme.onSurface)
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary),
                title = "Валюта",
                trail = {
                    Text(accountDemo.currency, color = MaterialTheme.colorScheme.onSurface)
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            )
        }
        FloatingActionButton(
            onClick = {},
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary,
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Rounded.Add, "",
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier.size(30.dp),
                )
        }
    }
}