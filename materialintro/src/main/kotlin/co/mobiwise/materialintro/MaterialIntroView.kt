package co.mobiwise.materialintro

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Build
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import co.mobiwise.materialintro.animation.AnimationFactory
import co.mobiwise.materialintro.animation.AnimationListener
import co.mobiwise.materialintro.databinding.MaterialIntroCardBinding
import co.mobiwise.materialintro.prefs.PreferencesManager
import co.mobiwise.materialintro.shape.Circle
import co.mobiwise.materialintro.shape.Focus
import co.mobiwise.materialintro.shape.FocusGravity
import co.mobiwise.materialintro.shape.Rect
import co.mobiwise.materialintro.shape.Shape
import co.mobiwise.materialintro.shape.ShapeType
import co.mobiwise.materialintro.target.Target
import co.mobiwise.materialintro.target.ViewTarget
import co.mobiwise.materialintro.utils.Constants
import co.mobiwise.materialintro.utils.Utils

/**
 * Created by mertsimsek on 22/01/16.
 */
class MaterialIntroView : RelativeLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var introViewId: String? = null
    private var maskColor: Int = 0
    private var delayMillis: Long = 0
    private var isReady: Boolean = false
    private var isFadeAnimationEnabled: Boolean = false
    private var fadeAnimationDuration: Long = 0
    private var targetShape: Shape? = null
    private var focusType: Focus? = null
    private var focusGravity: FocusGravity? = null
    private var targetView: Target? = null
    private var eraser: Paint? = null
    private var mHandler: Handler? = null
    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private var padding: Int = 0
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var dismissOnTouch: Boolean = false
    private var dismissOnBackPress: Boolean = false
    private var infoView: View? = null
    private var isInfoEnabled: Boolean = false
    private var dotView: View? = null
    private var isDotViewEnabled: Boolean = false
    private var preferencesManager: PreferencesManager? = null
    private var isLayoutCompleted: Boolean = false
    private var materialIntroListener: MaterialIntroListener? = null
    private var isPerformClick: Boolean = false
    private var isIdempotent: Boolean = false
    private var shapeType: ShapeType? = null

    val viewModel = MaterialIntroViewModel()
    var binding: MaterialIntroCardBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.material_intro_card, null, false)

    init {
        binding.viewModel = viewModel
        infoView = binding.infoLayout

        setWillNotDraw(false)
        visibility = View.INVISIBLE

        mHandler = Handler()
        preferencesManager = PreferencesManager(context)

        eraser = Paint()
        eraser!!.color = -0x1
        eraser!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        eraser!!.flags = Paint.ANTI_ALIAS_FLAG

        dotView = LayoutInflater.from(context).inflate(R.layout.dotview, null)
        dotView!!.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                targetShape!!.reCalculateAll()
                if (targetShape != null && targetShape!!.point.y != 0 && !isLayoutCompleted) {
                    if (isInfoEnabled)
                        setInfoLayout()
                    if (isDotViewEnabled)
                        setDotViewLayout()
                    removeOnGlobalLayoutListener(this@MaterialIntroView, this)
                }
            }
        })
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun removeOnGlobalLayoutListener(v: View, listener: ViewTreeObserver.OnGlobalLayoutListener) {
        v.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mWidth = measuredWidth
        mHeight = measuredHeight
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!isReady) return

        if (bitmap == null || canvas == null) {
            if (bitmap != null) bitmap!!.recycle()

            bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888)
            this.canvas = Canvas(bitmap!!)
        }

        /**
         * Draw mask
         */
        this.canvas!!.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        this.canvas!!.drawColor(maskColor)

        /**
         * Clear focus area
         */
        targetShape!!.draw(this.canvas!!, eraser!!, padding)

        canvas!!.drawBitmap(bitmap!!, 0f, 0f, null)
    }

    /**
     * Perform click operation when user
     * touches on target circle.
     *
     * @param event
     * @return
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val xT = event.x
        val yT = event.y

        val isTouchOnFocus = targetShape!!.isTouchOnFocus(xT.toDouble(), yT.toDouble())

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                if (isTouchOnFocus && isPerformClick) {
                    targetView?.view?.isPressed = true
                    targetView?.view?.invalidate()
                }

                return true
            }
            MotionEvent.ACTION_UP -> {

                if (isTouchOnFocus || dismissOnTouch)
                    dismiss()

                if (isTouchOnFocus && isPerformClick) {
                    targetView?.view?.performClick()
                    targetView?.view?.isPressed = true
                    targetView?.view?.invalidate()
                    targetView?.view?.isPressed = false
                    targetView?.view?.invalidate()
                }

                return true
            }
            else -> {
            }
        }

        return super.onTouchEvent(event)
    }

    /**
     * Shows material intro view with fade in
     * animation
     *
     * @param activity
     */
    private fun show(activity: Activity, configMaterial: MaterialIntroViewConfig? = null) {

        val res = overrideIntroViewConfig(configMaterial)

        introViewId = res.introViewId
        maskColor = res.maskColor!!
        delayMillis = res.delayMillis!!
        isFadeAnimationEnabled = res.isFadeAnimationEnabled!!
        shapeType = res.shapeType
        isReady = res.isReady!!
        targetView = res.target
        focusType = res.focusType
        padding = res.targetPadding!!
        dismissOnTouch = res.dismissOnTouch!!
        dismissOnBackPress = res.dismissOnBackPress!!
        focusGravity = res.focusGravity

        isInfoEnabled = res.isInfoEnabled!!
        isIdempotent = res.idempotent!!
        isDotViewEnabled = res.isDotViewEnabled!!

        shapeType = res.shapeType
        targetShape = convertShape(res.shapeType)

        materialIntroListener = res.materialIntroListener

        if (dismissOnBackPress)
            enableDismissOnBackPress()

        if (preferencesManager!!.isDisplayed(introViewId!!))
            return

        (activity.window.decorView as ViewGroup).addView(this)

        isReady = true

        mHandler!!.postDelayed({
            if (isFadeAnimationEnabled)
                AnimationFactory.animateFadeIn(this@MaterialIntroView, fadeAnimationDuration, object : AnimationListener.OnAnimationStartListener {
                    override fun onAnimationStart() {
                        visibility = View.VISIBLE
                    }
                })
            else
                visibility = View.VISIBLE
            if (dismissOnBackPress) {
                requestFocus()
            }
        }, delayMillis)

        if (isIdempotent) {
            preferencesManager!!.setDisplayed(introViewId!!)
        }

        viewModel.title.set(res.textTitle)
        viewModel.desc.set(res.textInfo)
    }

    /**
     * Dismiss Material Intro View
     */
    fun dismiss() {
        if (!isIdempotent) {
            preferencesManager!!.setDisplayed(introViewId!!)
        }

        AnimationFactory.animateFadeOut(this, fadeAnimationDuration, object : AnimationListener.OnAnimationEndListener {
            override fun onAnimationEnd() {
                visibility = View.GONE
                removeIntroView()

                if (materialIntroListener != null)
                    materialIntroListener!!.onUserClicked(introViewId!!)
            }
        })
    }

    private fun removeIntroView() {
        if (parent != null)
            (parent as ViewGroup).removeView(this)
    }

    /**
     * locate info card view above/below the
     * circle. If circle's Y coordiante is bigger than
     * Y coordinate of root view, then locate cardview
     * above the circle. Otherwise locate below.
     */
    private fun setInfoLayout() {

        mHandler!!.post {
            isLayoutCompleted = true

            if (infoView!!.parent != null)
                (infoView!!.parent as ViewGroup).removeView(infoView)

            val infoDialogParams = RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)

            if (targetShape!!.point.y < mHeight / 2) {
                (infoView as RelativeLayout).gravity = Gravity.TOP
                infoDialogParams.setMargins(
                        0,
                        targetShape!!.point.y + targetShape!!.height / 2,
                        0,
                        0)
            } else {
                (infoView as RelativeLayout).gravity = Gravity.BOTTOM
                infoDialogParams.setMargins(
                        0,
                        0,
                        0,
                        mHeight - (targetShape!!.point.y + targetShape!!.height / 2) + 2 * targetShape!!.height / 2)
            }

            infoView!!.layoutParams = infoDialogParams
            infoView!!.postInvalidate()

            addView(infoView)

            infoView!!.visibility = View.VISIBLE
        }
    }

    private fun setDotViewLayout() {

        mHandler!!.post {
            if (dotView!!.parent != null)
                (dotView!!.parent as ViewGroup).removeView(dotView)

            val dotViewLayoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dotViewLayoutParams.height = Utils.dpToPx(Constants.DEFAULT_DOT_SIZE)
            dotViewLayoutParams.width = Utils.dpToPx(Constants.DEFAULT_DOT_SIZE)
            dotViewLayoutParams.setMargins(
                    targetShape!!.point.x - dotViewLayoutParams.width / 2,
                    targetShape!!.point.y - dotViewLayoutParams.height / 2,
                    0,
                    0)
            dotView!!.layoutParams = dotViewLayoutParams
            dotView!!.postInvalidate()
            addView(dotView)

            dotView!!.visibility = View.VISIBLE
            AnimationFactory.performAnimation(dotView!!)
        }
    }

    private fun overrideIntroViewConfig(configMaterial: MaterialIntroViewConfig?): MaterialIntroViewConfig {
        return MaterialIntroViewConfig(
                maskColor = configMaterial?.maskColor ?: Constants.DEFAULT_MASK_COLOR,
                delayMillis = configMaterial?.delayMillis ?: Constants.DEFAULT_DELAY_MILLIS,
                isFadeAnimationEnabled = configMaterial?.isFadeAnimationEnabled ?: true,
                focusType = configMaterial?.focusType ?: Focus.MINIMUM,
                focusGravity = configMaterial?.focusGravity ?: FocusGravity.CENTER,
                targetView = configMaterial?.targetView,
                targetPadding = configMaterial?.targetPadding ?: Constants.DEFAULT_TARGET_PADDING,
                dismissOnTouch = configMaterial?.dismissOnTouch ?: false,
                dismissOnBackPress = configMaterial?.dismissOnBackPress ?: false,
                shapeType = configMaterial?.shapeType ?: ShapeType.CIRCLE,
                colorTextViewInfo = configMaterial?.colorTextViewInfo
                        ?: Constants.DEFAULT_COLOR_TEXTVIEW_INFO,
                textTitle = configMaterial?.textTitle ?: "Step 1",
                isTitleEnabled = configMaterial?.isTitleEnabled ?: true,
                textInfo = configMaterial?.textInfo ?: "Test test",
                isInfoEnabled = configMaterial?.isInfoEnabled ?: true,
                idempotent = configMaterial?.idempotent ?: false,
                isDotViewEnabled = configMaterial?.isDotViewEnabled ?: true,
                isPerformClick = configMaterial?.isPerformClick ?: true,
                isReady = configMaterial?.isReady ?: false,
                configurationMaterial = setConfiguration(configMaterial?.configurationMaterial),
                materialIntroListener = configMaterial?.materialIntroListener,
                target = ViewTarget(configMaterial?.targetView!!),
                introViewId = configMaterial.introViewId
        )
    }

    private fun convertShape(shapeType: ShapeType?): Shape {
        return if (shapeType == ShapeType.CIRCLE) {
            Circle(targetView!!, focusType!!, focusGravity!!, padding)
        } else {
            Rect(targetView!!, focusType!!, focusGravity!!, padding)
        }
    }

    private fun setConfiguration(configurationMaterial: MaterialIntroConfiguration?): MaterialIntroConfiguration? {
        if (configurationMaterial != null) {
            this.maskColor = configurationMaterial.maskColor
            this.delayMillis = configurationMaterial.delayMillis
            this.isFadeAnimationEnabled = configurationMaterial.isFadeAnimationEnabled
            this.isDotViewEnabled = configurationMaterial.isDotViewEnabled
            this.dismissOnTouch = configurationMaterial.isDismissOnTouch
            this.dismissOnBackPress = configurationMaterial.isDismissOnBackPress
            this.focusType = configurationMaterial.focusType
            this.focusGravity = configurationMaterial.focusGravity
        }

        return configurationMaterial
    }

    data class MaterialIntroViewConfig(
            val maskColor: Int? = null,
            val delayMillis: Long? = null,
            val isFadeAnimationEnabled: Boolean? = null,
            val shapeType: ShapeType? = null,
            val isReady: Boolean? = null,
            val target: Target? = null,
            val focusType: Focus? = null,
            val targetView: View?,
            val shape: Shape? = null,
            val targetPadding: Int? = null,
            val dismissOnTouch: Boolean? = null,
            val dismissOnBackPress: Boolean? = null,
            val focusGravity: FocusGravity? = null,
            val colorTextViewInfo: Int? = null,
            val textTitle: CharSequence?,
            val isTitleEnabled: Boolean? = null,
            val textInfo: CharSequence?,
            val isInfoEnabled: Boolean? = null,
            val idempotent: Boolean? = null,
            val isDotViewEnabled: Boolean? = null,
            val configurationMaterial: MaterialIntroConfiguration? = null,
            val introViewId: String?,
            val materialIntroListener: MaterialIntroListener? = null,
            val isPerformClick: Boolean? = null
    )

    private fun enableDismissOnBackPress() {
        isFocusableInTouchMode = true
        isFocusable = true
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (dismissOnBackPress && event.keyCode == KeyEvent.KEYCODE_BACK) {
            if (event.action == KeyEvent.ACTION_UP) {
                dismiss()
            }
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    companion object {
        fun build(activity: AppCompatActivity, configMaterial: MaterialIntroViewConfig?): MaterialIntroView {
            val introView = MaterialIntroView(activity)
            introView.show(activity, configMaterial)
            return introView
        }
    }
}
