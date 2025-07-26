package com.m4isper.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CustomBasicTextField(
    text: String?,
    onValueChange: (String) -> Unit,
    textPlaceholder: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.End,
    trail: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterEnd
    ) {
        Row {
            BasicTextField(
                value = text.orEmpty(),
                onValueChange = onValueChange,
//            modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    textAlign = textAlign,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                decorationBox = { innerTextField ->
                    if (text.isNullOrEmpty()) {
                        Text(
                            text = textPlaceholder,
//                        modifier = Modifier.fillMaxWidth(),
                            textAlign = textAlign,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                    innerTextField()
                }
            )

            trail?.invoke()
        }
    }
}