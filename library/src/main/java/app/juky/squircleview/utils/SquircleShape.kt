package app.juky.squircleview.utils

import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.PathShape
import android.view.View
import androidx.annotation.IntRange
import app.juky.squircleview.data.Constants.DEFAULT_CORNER_SMOOTHING
import com.google.android.material.shape.CornerSize
import com.google.android.material.shape.CutCornerTreatment
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.shape.ShapePath
import kotlin.math.min

/**
 * This class provides a ShapePath, ShapeAppearance and ShapeDrawable which can be used
 * instead of the custom view, allowing you to use the default shapes in Android.
 */
object SquircleShape {
    fun getSquirclePath(
        rect: RectF,
        width: Int,
        height: Int,
        @IntRange(from = 0, to = DEFAULT_CORNER_SMOOTHING)
        cornerSmoothing: Int = DEFAULT_CORNER_SMOOTHING.toInt()
    ): ShapePath {
        val radiusAndSmoothing = SquirclePath.getRadiusAndSmoothing(height, width, cornerSmoothing)
        val radius = radiusAndSmoothing.first
        val smoothing = radiusAndSmoothing.second

        // Start position
        val path = ShapePath(radius, 0f)
        // Top line
        path.lineTo((width - radius), 0f)
        // Top right corner
        path.cubicToPoint((width - radius * (1 - smoothing)), 0f, width.toFloat(), (radius * (1 - smoothing)), width.toFloat(), radius)
        // Right line
        path.lineTo(width.toFloat(), (height - radius))
        // Bottom right corner
        path.cubicToPoint(width.toFloat(), ((height - radius * (1 - smoothing))), ((width - radius * (1 - smoothing))), height.toFloat(), (width - radius), height.toFloat())
        // Bottom line
        path.lineTo(radius, height.toFloat())
        // Bottom left corner
        path.cubicToPoint(((radius * (1 - smoothing))), height.toFloat(), 0f, ((height - radius * (1 - smoothing))), 0f, (height - radius))
        // Left line
        path.lineTo(0f, radius)
        // Top left corner
        path.cubicToPoint(0f, ((radius * (1 - smoothing))), ((radius * (1 - smoothing))), 0f, radius, 0f)

        return path
    }

    fun getShapeAppearance(
        @IntRange(from = 0, to = DEFAULT_CORNER_SMOOTHING)
        cornerSmoothing: Int = DEFAULT_CORNER_SMOOTHING.toInt()
    ): ShapeAppearanceModel.Builder =
        ShapeAppearanceModel.builder().setAllCorners(SquircleCornerTreatment(cornerSmoothing))

    fun getShapeDrawable(
        view: View,
        @IntRange(from = 0, to = DEFAULT_CORNER_SMOOTHING)
        cornerSmoothing: Int = DEFAULT_CORNER_SMOOTHING.toInt()
    ): ShapeDrawable {
        val path = SquirclePath.getSquirclePath(
            rect = RectF(0f, 0f, view.width.toFloat(), view.height.toFloat()),
            width = view.width,
            height = view.height,
            cornerSmoothing = cornerSmoothing
        )

        return object : ShapeDrawable(PathShape(path, view.width.toFloat(), view.height.toFloat())) {
            //可以重写下面这2个,根据getColorStateList的状态显示不同的colorRes
//            override fun isStateful(): Boolean {
//                return super.isStateful()
//            }
//
//            override fun onStateChange(stateSet: IntArray?): Boolean {
//                return super.onStateChange(stateSet)
//            }
            override fun onBoundsChange(bounds: Rect) {
                super.onBoundsChange(bounds)
                shape = PathShape(
                    SquirclePath.getSquirclePath(
                        RectF(0f, 0f, bounds.width().toFloat(), bounds.height().toFloat()),
                        width = bounds.width(),
                        height = bounds.height(),
                        cornerSmoothing = cornerSmoothing
                    ),
                    bounds.width().toFloat(),
                    bounds.height().toFloat()
                )
            }
        }
    }

    /**
     * CornerTreatment used to apply a treatment to the corners of buttons, images, constraintlayouts, etc.
     */
    class SquircleCornerTreatment(
        @IntRange(from = 0, to = DEFAULT_CORNER_SMOOTHING)
        private val cornerSmoothing: Int = DEFAULT_CORNER_SMOOTHING.toInt()
    ) : CutCornerTreatment() {
        override fun getCornerPath(shapePath: ShapePath, angle: Float, interpolation: Float, bounds: RectF, size: CornerSize) {
            val startX = 0f
            val startY = 0f
            val radiusAndSmoothing = SquirclePath.getRadiusAndSmoothing(bounds.height().toInt(), bounds.width().toInt(), cornerSmoothing)
            val radius = radiusAndSmoothing.first
            val smoothing = radiusAndSmoothing.second

            shapePath.lineTo(startX, startY + radius)
            shapePath.reset(0f, radius)

            shapePath.cubicToPoint(0f, ((radius * (1 - smoothing))), ((radius * (1 - smoothing))), 0f, radius, 0f)
        }
    }
}