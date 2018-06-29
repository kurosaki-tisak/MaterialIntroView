package co.mobiwise.materialintro.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.RectF
import co.mobiwise.materialintro.target.Target

/**
 * Created by mertsimsek on 25/01/16.
 */
class Rect(target: Target, focus: Focus, focusGravity: FocusGravity, padding: Int) : Shape(target, focus, focusGravity, padding) {

    lateinit var adjustedRect: RectF

    override val point: Point
        get() = target.point

    override val height: Int
        get() = adjustedRect.height().toInt()

    init {
        calculateAdjustedRect()
    }

    override fun draw(canvas: Canvas, eraser: Paint, padding: Int) {
        canvas.drawRoundRect(adjustedRect, padding.toFloat(), padding.toFloat(), eraser)
    }

    private fun calculateAdjustedRect() {
        val rect = RectF()
        rect.set(target.rect)

        rect.left -= padding.toFloat()
        rect.top -= padding.toFloat()
        rect.right += padding.toFloat()
        rect.bottom += padding.toFloat()

        adjustedRect = rect
    }

    override fun reCalculateAll() {
        calculateAdjustedRect()
    }

    override fun isTouchOnFocus(x: Double, y: Double): Boolean =
            adjustedRect.contains(x.toFloat(), y.toFloat())
}
