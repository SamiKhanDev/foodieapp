package com.example.foodapp.tooltip


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TooltipContent(
    tooltipText: String,
    modifier: Modifier = Modifier,
    showOnHover: Boolean = false,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Tooltip(
            tooltipText = tooltipText,
            showOnHover = showOnHover,
            content = content
        )
    }
}

@Composable
private fun Tooltip(
    tooltipText: String,
    modifier: Modifier = Modifier,
    showOnHover: Boolean = false,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()

        TooltipPopup(
            modifier = Modifier.padding(start = 8.dp),
            requesterView = { content() },
            tooltipContent = {
                Text(
                    text = tooltipText,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(vertical = 8.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                    color = Color.White,
                )
            }
        )
    }
}
