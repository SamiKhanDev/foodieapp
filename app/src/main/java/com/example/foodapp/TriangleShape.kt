package com.example.foodapp

import android.util.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class TriangleShape: Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val trianglePath = Path()
        trianglePath.apply {
            moveTo(x = size.width / 2, y = 0f)
            lineTo(x = size.width, y = size.height)
            lineTo(x = 0f, y = size.height)
        }

        return Outline.Generic(path = trianglePath)
    }
}