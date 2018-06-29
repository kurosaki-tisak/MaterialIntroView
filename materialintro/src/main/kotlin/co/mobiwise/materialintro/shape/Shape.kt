package co.mobiwise.materialintro.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import co.mobiwise.materialintro.target.Target

/**
 * Created by yuchen on 3/17/16.
 */
abstract class Shape(protected var target: Target, protected var focus: Focus, protected var focusGravity: FocusGravity, protected var padding: Int) {

    protected val focusPoint: Point
        get() {
            return when (focusGravity) {
                FocusGravity.LEFT -> {
                    val xLeft = target.rect.left + (target.point.x - target.rect.left) / 2
                    Point(xLeft, target.point.y)
                }
                FocusGravity.RIGHT -> {
                    val xRight = target.point.x + (target.rect.right - target.point.x) / 2
                    Point(xRight, target.point.y)
                }
                else -> target.point
            }
        }

    abstract val point: Point

    abstract val height: Int

    abstract fun draw(canvas: Canvas, eraser: Paint, padding: Int)

    abstract fun reCalculateAll()

    /**
     * Determines if a click is on the shape
     * @param x x-axis location of click
     * @param y y-axis location of click
     * @return true if click is inside shape
     */
    abstract fun isTouchOnFocus(x: Double, y: Double): Boolean
}
