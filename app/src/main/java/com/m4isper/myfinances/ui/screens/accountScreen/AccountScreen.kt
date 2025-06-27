package com.m4isper.myfinances.ui.screens.accountScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m4isper.myfinances.BuildConfig
import com.m4isper.myfinances.domain.utils.formatWithSpaces
import com.m4isper.myfinances.domain.utils.toCleanDecimal
import com.m4isper.myfinances.ui.components.AddButton
import com.m4isper.myfinances.ui.components.CustomListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val accounts by viewModel.accounts.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAccounts()
    }

    val accountId = BuildConfig.ID_ACCOUNT
    val account = accounts.find { it.id == accountId }

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
                        text = account?.balance.toCleanDecimal().formatWithSpaces(),
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
                    Text("₽", color = MaterialTheme.colorScheme.onSurface)
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            )

            if (error != null) {
                Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
            }
        }

        AddButton(
            modifier = Modifier.align(Alignment.BottomEnd),
            onClick = {}
        )
    }
}