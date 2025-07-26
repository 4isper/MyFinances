package com.m4isper.settings.appInfoScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.m4isper.resources.R
import com.m4isper.ui.components.CustomListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInfoScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val viewModel: AppInfoViewModel = viewModel()
    val info = viewModel.appInfo

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                title = { Text(
                    text = stringResource(R.string.about_program),
                    color = MaterialTheme.colorScheme.onSurface) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface),
                title = stringResource(R.string.version),
                trail = {
                    Text(
                        text = info.versionName,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface),
                title = stringResource(R.string.last_update),
                trail = {
                    Text(
                        text = info.lastUpdateDate,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    }
}