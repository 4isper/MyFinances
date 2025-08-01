package com.m4isper.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.m4isper.domain.BuildConfig
import com.m4isper.resources.R
import com.m4isper.domain.utils.formatWithSpaces
import com.m4isper.domain.utils.toCleanDecimal
import com.m4isper.ui.components.AddButton
import com.m4isper.ui.components.CustomListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModelFactory: ViewModelProvider.Factory
) {
    val viewModel: AccountViewModel = viewModel(factory = viewModelFactory)

    val account by viewModel.account.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAccounts(BuildConfig.ID_ACCOUNT)
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            CenterAlignedTopAppBar(
                title = { Text(
                    text = stringResource(R.string.my_account),
                    color = MaterialTheme.colorScheme.onSurface)
                        },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { navController.navigate("account/edit") }) {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = "Edit",
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
                title = account?.name,
                trail = {
                    Text(
                        text = account?.balance.toCleanDecimal().formatWithSpaces() + " " + account?.currency,
                        color = MaterialTheme.colorScheme.onSurface)
//                    Icon(
//                        imageVector = Icons.Rounded.KeyboardArrowRight,
//                        contentDescription = "",
//                        tint = MaterialTheme.colorScheme.tertiary
//                    )
                }
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary),
                title = stringResource(R.string.currency),
                trail = {
                    account?.currency?.let { Text(it, color = MaterialTheme.colorScheme.onSurface) }
//                    Icon(
//                        imageVector = Icons.Rounded.KeyboardArrowRight,
//                        contentDescription = "",
//                        tint = MaterialTheme.colorScheme.tertiary
//                    )
                }
            )

            if (error != null) {
                Text("${stringResource(R.string.error)}: $error", color = MaterialTheme.colorScheme.error)
            }
        }

//        AddButton(
//            modifier = Modifier.align(Alignment.BottomEnd),
//            onClick = {}
//        )
    }
}