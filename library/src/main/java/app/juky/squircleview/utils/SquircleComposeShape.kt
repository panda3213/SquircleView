package app.juky.squircleview.utils


import android.graphics.RectF
import androidx.annotation.IntRange
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import app.juky.squircleview.data.Constants.DEFAULT_CORNER_SMOOTHING
import app.juky.squircleview.utils.SquirclePath.getRadiusAndSmoothing

object SquircleComposeShape {
    fun getSquirclePath(
        size: Size,
        @IntRange(from = 0, to = DEFAULT_CORNER_SMOOTHING)
        cornerSmoothing: Int = DEFAULT_CORNER_SMOOTHING.toInt()
    ): Path {
        return getSquirclePath(
            rect = RectF(0f, 0f, size.width, size.height),
            width = size.width.toInt(),
            height = size.height.toInt(),
            cornerSmoothing = cornerSmoothing
        )
    }

    fun getSquirclePath(
        rect: RectF,
        width: Int,
        height: Int,
        @IntRange(from = 0, to = DEFAULT_CORNER_SMOOTHING)
        cornerSmoothing: Int = DEFAULT_CORNER_SMOOTHING.toInt()
    ): Path {
        val radiusAndSmoothing = getRadiusAndSmoothing(height, width, cornerSmoothing)
        val radius = radiusAndSmoothing.first
        val smoothing = radiusAndSmoothing.second


        val path = Path()
        // Start position
        path.moveTo(radius, 0f)
        // Top line
        path.lineTo((width - radius), 0f)
        // Top right corner
        path.cubicTo((width - radius * (1 - smoothing)), 0f, width.toFloat(), (radius * (1 - smoothing)), width.toFloat(), radius)
        // Right line
        path.lineTo(width.toFloat(), (height - radius))
        // Bottom right corner
        path.cubicTo(width.toFloat(), ((height - radius * (1 - smoothing))), ((width - radius * (1 - smoothing))), height.toFloat(), (width - radius), height.toFloat())
        // Bottom line
        path.lineTo(radius, height.toFloat())
        // Bottom left corner
        path.cubicTo(((radius * (1 - smoothing))), height.toFloat(), 0f, ((height - radius * (1 - smoothing))), 0f, (height - radius))
        // Left line
        path.lineTo(0f, radius)
        // Top left corner
        path.cubicTo(0f, ((radius * (1 - smoothing))), ((radius * (1 - smoothing))), 0f, radius, 0f)
        path.close()

        return path
    }

    class SquircleComposeShape(
        @IntRange(from = 0, to = DEFAULT_CORNER_SMOOTHING)
        val cornerSmoothing: Int = DEFAULT_CORNER_SMOOTHING.toInt()
    ) : Shape {
        override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
            return Outline.Generic(
                getSquirclePath(
                    rect = RectF(0f, 0f, size.width, size.height),
                    width = size.width.toInt(),
                    height = size.height.toInt(),
                    cornerSmoothing = cornerSmoothing
                )
            )
        }
    }
}