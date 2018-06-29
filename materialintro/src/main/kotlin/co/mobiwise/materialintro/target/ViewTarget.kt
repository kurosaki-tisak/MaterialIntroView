package co.mobiwise.materialintro.target

import android.graphics.Point
import android.graphics.Rect
import android.view.View
import co.mobiwise.materialintro.target.Target

/**
 * Created by mertsimsek on 25/01/16.
 */
class ViewTarget(override val view: View) : Target {

    override val point: Point
        get() {

            val location = IntArray(2)
            view.getLocationInWindow(location)
            return Point(location[0] + view.width / 2, location[1] + view.height / 2)
        }

    override val rect: Rect
        get() {
            val location = IntArray(2)
            view.getLocationInWindow(location)
            return Rect(
                    location[0],
                    location[1],
                    location[0] + view.width,
                    location[1] + view.height
            )
        }
}
