package com.m4isper.myfinances.ui.screens.accountScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.m4isper.myfinances.BuildConfig
import com.m4isper.myfinances.domain.utils.formatWithSpaces
import com.m4isper.myfinances.domain.utils.toCleanDecimal
import com.m4isper.myfinances.ui.components.AddButton
import com.m4isper.myfinances.ui.components.CustomBasicTextField
import com.m4isper.myfinances.ui.components.CustomListItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAccountScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AccountViewModel = hiltViewModel()
) {
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
                title = { Text("Мой счет", color = MaterialTheme.colorScheme.onSurface) },
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
                title = "Название",
                trail = {
                    CustomBasicTextField(
                        text = name,
                        onValueChange = { name = it },
                        textPlaceholder = "Введите название"
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
                title = "Баланс",
                trail = {
                    CustomBasicTextField(
                        text = balance,
                        onValueChange = { balance = it },
                        textPlaceholder = "Введите сумму баланса"
                    )
                }
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(onClick = { showBottomSheet = true }),
                title = "Валюта",
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
                Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    containerColor = MaterialTheme.colorScheme.surface,
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    val currencies = listOf("₽" to "Российский рубль", "$" to "Доллар США", "€" to "Евро")

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
                                    contentDescription = "Cancel",
                                    tint = MaterialTheme.colorScheme.surface
                                )
                            }
                            Text(
                                text = "Отмена",
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