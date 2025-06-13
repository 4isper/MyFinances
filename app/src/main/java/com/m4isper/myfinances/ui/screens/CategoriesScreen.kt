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
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.m4isper.myfinances.domain.categoriesDemo
import com.m4isper.myfinances.ui.CustomListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            CenterAlignedTopAppBar(
                title = { Text("Мои статьи", color = MaterialTheme.colorScheme.onSurface) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
            )

            CustomListItem(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                title = "Найти статью",
                trail = {
                    Icon(
                        Icons.Rounded.Search, "",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            )

            LazyColumn {
                items (categoriesDemo) { item ->
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
                                Text(
                                    text = item.emoji.toString(),
//                                    fontSize = if (item.emoji.toString().isEmoji()) 13.sp else 10.sp
                                )
                            }
                        },
                        title = item.name,
                    )
                }
            }
        }
    }
}