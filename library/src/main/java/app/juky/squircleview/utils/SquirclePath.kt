package app.juky.squircleview.utils

import android.graphics.Path
import android.graphics.RectF
import androidx.annotation.IntRange
import app.juky.squircleview.data.Constants.DEFAULT_CORNER_SMOOTHING
import kotlin.math.min

object SquirclePath {
    fun getRadiusAndSmoothing(
        height: Int,
        width: Int,
        @IntRange(from = 0, to = DEFAULT_CORNER_SMOOTHING)
        cornerSmoothing: Int = DEFAULT_CORNER_SMOOTHING.toInt()
    ): Pair<Float, Float> {
        val radius = min(width, height) / 2f
        // Percentage cannot exceed 100%, convert it to a decimal percentage (e.g. 0.6)
        val smoothingPercentage = min(cornerSmoothing, 100) / 100.0
        return Pair(radius, smoothingPercentage.toFloat())
    }

    /**
     * Get the Path used to represent a Squircle
     *
     * @param rect RectF Rectangle used to determine start / end bounds
     * @param width Int Final width used to determine the radius
     * @param height Int Final height used to determine the radius
     * @return Path Squircle Path
     */
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
}