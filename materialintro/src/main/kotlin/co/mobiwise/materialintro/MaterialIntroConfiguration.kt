package co.mobiwise.materialintro

import co.mobiwise.materialintro.shape.Focus
import co.mobiwise.materialintro.shape.FocusGravity
import co.mobiwise.materialintro.utils.Constants

class MaterialIntroConfiguration {

    var maskColor: Int = 0

    var delayMillis: Long = 0

    var isFadeAnimationEnabled: Boolean = false

    var focusType: Focus? = null

    var focusGravity: FocusGravity? = null

    var padding: Int = 0

    var isDismissOnTouch: Boolean = false

    var isDismissOnBackPress: Boolean = false

    var colorTextViewInfo: Int = 0

    var isDotViewEnabled: Boolean = false

    init {
        maskColor = Constants.DEFAULT_MASK_COLOR
        delayMillis = Constants.DEFAULT_DELAY_MILLIS
        padding = Constants.DEFAULT_TARGET_PADDING
        colorTextViewInfo = Constants.DEFAULT_COLOR_TEXTVIEW_INFO
        focusType = Focus.ALL
        focusGravity = FocusGravity.CENTER
        isFadeAnimationEnabled = false
        isDismissOnTouch = false
        isDotViewEnabled = false
    }
}