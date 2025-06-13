package com.m4isper.myfinances.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomListItem(
    modifier: Modifier = Modifier,
    lead: @Composable (() -> Unit)? = null,
    title: String,
    subtitle: String? = null,
    trail: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .size(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (lead != null) {
                lead()
            }
            Column {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (subtitle != null)
                    Text(
                        text = subtitle,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 1.sp,
                        style = MaterialTheme.typography.bodyMedium,
                    )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(13.dp, Alignment.End)
        ) {
            if (trail != null) {
                trail()
            }
        }
    }
    HorizontalDivider(
        thickness = 1.3.dp,
        color = MaterialTheme.colorScheme.outlineVariant
    )
}