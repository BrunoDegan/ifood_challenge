package com.brunodegan.ifood_challenge.base.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class MovieShape(
    val cornerRadius: Float = 0f,
    val color: Color = Color.Transparent
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        left = 0f,
                        top = 0f,
                        right = size.width,
                        bottom = size.height
                    ),
                    cornerRadius = CornerRadius(cornerRadius)
                )
            )
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
@Preview
fun CustomMovieShapePreview() {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(MovieShape(cornerRadius = 16f, color = Color.Red))
    )
}