package com.m4isper.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.m4isper.ui.components.CustomListItem
import com.m4isper.resources.R
import com.m4isper.settings.navigation.SETTINGS_APP_INFO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModelFactory: ViewModelProvider.Factory
) {
    val viewModel: ThemeViewModel = viewModel(factory = viewModelFactory)

    val settingsItems = listOf(
        SettingItem.Static(stringResource(R.string.primary_color)),
        SettingItem.Static(stringResource(R.string.sounds)),
        SettingItem.Static(stringResource(R.string.haptics)),
        SettingItem.Static(stringResource(R.string.password_code)),
        SettingItem.Static(stringResource(R.string.synchronization)),
        SettingItem.Static(stringResource(R.string.language)),
        SettingItem.Navigable(stringResource(R.string.about_program), SETTINGS_APP_INFO)
    )


    val isDarkTheme by viewModel.isDarkTheme.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            CenterAlignedTopAppBar(
                title = { Text(
                    text = stringResource(R.string.settings),
                    color = MaterialTheme.colorScheme.onSurface) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface),
                title = stringResource(R.string.dark_theme),
                trail = {
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = {
                            viewModel.toggleTheme(it)
                        },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            checkedThumbColor = MaterialTheme.colorScheme.onSurface
                        ),
                    )
                }
            )

            LazyColumn {
                items (settingsItems) { item ->
                    CustomListItem(
                        modifier = Modifier
                            .let {
                                if (item is SettingItem.Navigable) {
                                    it.clickable { navController.navigate(item.route) }
                                } else {
                                    it
                                }
                            }
                            .background(MaterialTheme.colorScheme.surface),
                        title = item.title,
                        trail = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.rotate(-90f),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }
        }
    }
}