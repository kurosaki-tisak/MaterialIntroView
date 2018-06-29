package co.mobiwise.materialintro.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import co.mobiwise.materialintro.target.Target

/**
 * Created by mertsimsek on 25/01/16.
 */
class Circle(target: Target, focus: Focus, focusGravity: FocusGravity, padding: Int) : Shape(target, focus, focusGravity, padding) {

    private var radius: Int = 0

    private var circlePoint = Point()

    override val point: Point
        get() = circlePoint

    override val height: Int
        get() = 2 * radius

    init {
        circlePoint = focusPoint
        calculateRadius(padding)
    }

    override fun draw(canvas: Canvas, eraser: Paint, padding: Int) {
        calculateRadius(padding)
        circlePoint = focusPoint
        canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), radius.toFloat(), eraser)
    }

    override fun reCalculateAll() {
        calculateRadius(padding)
        circlePoint = focusPoint
    }

    private fun calculateRadius(padding: Int) {
        val side: Int

        side = when {
            focus === Focus.MINIMUM -> Math.min(target.rect.width() / 2, target.rect.height() / 2)
            focus === Focus.ALL -> Math.max(target.rect.width() / 2, target.rect.height() / 2)
            else -> {
                val minSide = Math.min(target.rect.width() / 2, target.rect.height() / 2)
                val maxSide = Math.max(target.rect.width() / 2, target.rect.height() / 2)
                (minSide + maxSide) / 2
            }
        }

        radius = side + padding
    }

    override fun isTouchOnFocus(x: Double, y: Double): Boolean {
        val xV = point.x
        val yV = point.y

        val dx = Math.pow(x - xV, 2.0)
        val dy = Math.pow(y - yV, 2.0)
        return dx + dy <= Math.pow(radius.toDouble(), 2.0)
    }
}