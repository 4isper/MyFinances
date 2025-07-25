package com.m4isper.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.m4isper.domain.BuildConfig
import com.m4isper.resources.R
import com.m4isper.ui.components.CustomBasicTextField
import com.m4isper.ui.components.CustomListItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModelFactory: ViewModelProvider.Factory
) {
    val viewModel: AccountViewModel = viewModel(factory = viewModelFactory)

    val accountState = viewModel.account.collectAsState()
    val account = accountState.value

    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAccounts(BuildConfig.ID_ACCOUNT)
    }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(account) {
        if (account != null) {
            name = account.name
            balance = account.balance
            currency = account.currency
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(com.m4isper.resources.R.string.my_account),
                        color = MaterialTheme.colorScheme.onSurface)
                        },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Outlined.Close,
                            contentDescription = "Cancel",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            val result = viewModel.updateAccount(
                                id = BuildConfig.ID_ACCOUNT,
                                name = name,
                                balance = balance,
                                currency = currency
                            )
                            if (result) {
                                navController.popBackStack()
                            } else {
                                // можно показать Snackbar или ошибку
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
                    .background(MaterialTheme.colorScheme.surface),
                title = stringResource(R.string.name),
                trail = {
                    CustomBasicTextField(
                        text = name,
                        onValueChange = { name = it },
                        textPlaceholder = stringResource(R.string.enter_name)
                    )
                }
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface),
                lead = {
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = CircleShape
                            )
                            .size(24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "\uD83D\uDCB0")
                    }
                },
                title = stringResource(R.string.balance),
                trail = {
                    CustomBasicTextField(
                        text = balance,
                        onValueChange = { balance = it },
                        textPlaceholder = stringResource(R.string.enter_balance),
                        trail = {
                            Text(
                                text = " $currency",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    )
                }
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(onClick = { showBottomSheet = true }),
                title = stringResource(R.string.currency),
                trail = {
                    Text(
                        text = currency,
                        color = MaterialTheme.colorScheme.onSurface)
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            )

            if (error != null) {
                Text("${stringResource(R.string.error)}: $error", color = MaterialTheme.colorScheme.error)
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    containerColor = MaterialTheme.colorScheme.surface,
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    val currencies = listOf(
                        "₽" to stringResource(R.string.rub),
                        "$" to stringResource(R.string.usd),
                        "€" to stringResource(R.string.eur)
                    )

                    currencies.forEach { (symbol, title) ->
                        CustomListItem(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable {
                                    currency = symbol
                                    showBottomSheet = false
                                }
                                .padding(vertical = 10.dp),
                            lead = {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = symbol,
                                        fontSize = 24.sp
                                    )
                                }
                            },
                            title = "$title $symbol",
                        )
                    }

                    CustomListItem(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.error)
                            .clickable { showBottomSheet = false }
                            .padding(vertical = 10.dp),
                        lead = {
                            Box(
                                modifier = Modifier
                                    .size(24.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    Icons.Outlined.Close,
                                    contentDescription = stringResource(R.string.cancel),
                                    tint = MaterialTheme.colorScheme.surface
                                )
                            }
                            Text(
                                text = stringResource(R.string.cancel),
                                color = MaterialTheme.colorScheme.surface
                            )
                        },
                        title = null,
                    )
                }
            }
        }
    }
}