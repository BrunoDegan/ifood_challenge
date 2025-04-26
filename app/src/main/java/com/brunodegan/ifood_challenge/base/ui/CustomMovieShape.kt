package com.brunodegan.ifood_challenge.base.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class PosterShape(
    private val cutOutHeight: Float = 10f,
    private val topCutOutHeight: Float = 10f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
            val halfImgWidth = size.width / 2
            val cornerLineWidth = 10f
            val path = Path().apply {

                moveTo(0f, topCutOutHeight)
                lineTo(halfImgWidth - cornerLineWidth, 0f)
                lineTo(halfImgWidth + cornerLineWidth, 0f)
                lineTo(size.width, topCutOutHeight)
                lineTo(size.width, size.height - cutOutHeight)

                lineTo(halfImgWidth + cornerLineWidth, size.height)
                lineTo(halfImgWidth - cornerLineWidth, size.height)
                lineTo(0f, size.height - cutOutHeight)
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
            .size(200.dp, 300.dp)
            .clip(PosterShape(cutOutHeight = 80f))
            .background(Color.Gray)
    )
}