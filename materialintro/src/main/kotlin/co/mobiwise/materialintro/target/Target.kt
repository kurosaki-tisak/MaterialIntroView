package co.mobiwise.materialintro.target

import android.graphics.Point
import android.graphics.Rect
import android.view.View

/**
 * Created by mertsimsek on 25/01/16.
 */
interface Target {
    /**
     * Returns center point of target.
     * We can get x and y coordinates using
     * point object
     * @return
     */
    val point: Point

    /**
     * Returns Rectangle points of target view
     * @return
     */
    val rect: Rect

    /**
     * return target view
     * @return
     */
    val view: View
}
