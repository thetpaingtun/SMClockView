package me.thet.smClock

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.lang.RuntimeException
import kotlin.math.roundToInt


class SMClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mStart: Boolean = false
    private val DEFAULT_ANIMATION_DURATION = 2000
    private var mDashLinePaint: Paint
    private val mBaseCirclePaint: Paint
    private val mClockPaint: Paint
    private val mClockLabelPaint: Paint
    private val mPositionBasePaint: Paint
    private val mDensity: Float
    private val mScaleDensity: Float
    private var mDefPadding: Float = 0f

    private var sunDrawable: Drawable
    private var moonDrawable: Drawable
    private var mCurrentDrawable: Drawable


    private val mPositionIconRect: Rect
    private val mPositionIconBaseRect: RectF
    private var mPositionIconSize: Float = 0f
    private var mPositionIconBaseSize: Float = 0f
    private var mClockDigitLabelSpace: Float = 0f


    private var mPaddingLeft: Float = 0f
    private var mPaddingRight: Float = 0f
    private var mPaddingTop: Float = 0f
    private var mPaddingBottom: Float = 0f


    private val POSITION_BASE_RATIO_TO_ICON = 1.58f
    private val RATIO_CLOCK_DIGIT = 0.1f
    private val RATIO_CLOCK_LABEL = 0.06f
    private val RATIO_BASE_CIRCLE_STROKE = 0.015f
    private val RATIO_POSITION_ICON = 0.03f
    private val RATIO_DEF_PADDING = 0.01f
    private val RATIO_CLOCK_DIGIT_LABEL_SPACE = 0.009f
    private var mTypeFace: Typeface? = null

    private var mPositionalIconPosition: Float = 180f
    private val POSITIONAL_START_ANGLE = 180f

    private var dayBreakHour: Int = 5
    private var daybreakMin: Int = 30

    private var nightFallHour: Int = 18
    private var nightFallMin: Int = 30

    private var mSunPositionCal: SunPositionCalculator

    private lateinit var mClockTime: String
    private var mClockLabel: String

    private var mLargeCircleColor: Int
    private var mClockColor: Int
    private var mClockSubLabelColor: Int
    private var mSmallCircleColor: Int
    private var mDashLineColor: Int

    private var mAnimationDuration: Int = DEFAULT_ANIMATION_DURATION

    private var mTimeReceiver: TimeTickReceiver


    init {

        mTimeReceiver = TimeTickReceiver()

        sunDrawable = ContextCompat.getDrawable(context, R.drawable.ic_sm_sun)!!
        moonDrawable = ContextCompat.getDrawable(context, R.drawable.ic_sm_moon)!!


        mLargeCircleColor = getColor(R.color.primaryDark)
        mClockColor = getColor(R.color.primaryDark)
        mClockSubLabelColor = getColor(R.color.primaryDark)
        mSmallCircleColor = getColor(R.color.primaryDark)
        mDashLineColor = getColor(R.color.primaryDarkTransparent)

        mClockLabel = ""

        retrieveAttrs(attrs)


        mDensity = getContext().resources.displayMetrics.density
        mScaleDensity = getContext().resources.displayMetrics.scaledDensity


        mCurrentDrawable = sunDrawable

        if (!isInEditMode) {
            mTypeFace = Typeface.create(
                ResourcesCompat.getFont(context, R.font.robotoslab_regular),
                Typeface.BOLD
            )
        }



        mPositionIconRect = Rect()
        mPositionIconBaseRect = RectF()

        mBaseCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mLargeCircleColor
            style = Paint.Style.STROKE
        }

        mDashLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mDashLineColor
            style = Paint.Style.STROKE
            strokeWidth = dp(2f)
            pathEffect = DashPathEffect(floatArrayOf(dp(1f), dp(2f), dp(1f)), 0f)
        }


        mClockPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mClockColor
            typeface = mTypeFace

        }

        mClockLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mClockSubLabelColor
            typeface = mTypeFace
        }

        mPositionBasePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mSmallCircleColor
            style = Paint.Style.FILL_AND_STROKE
        }

        mSunPositionCal = SunPositionCalculator(
            HourMin(dayBreakHour, daybreakMin),
            HourMin(nightFallHour, nightFallMin)
        )


    }

    private fun retrieveAttrs(attrs: AttributeSet?) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.SMClockView, 0, 0).apply {
            try {
                mStart = getBoolean(R.styleable.SMClockView_sm_start_immediateley, true)

                sunDrawable = getDrawable(R.styleable.SMClockView_sm_sun_icon) ?: sunDrawable
                moonDrawable = getDrawable(R.styleable.SMClockView_sm_moon_icon) ?: moonDrawable

                mLargeCircleColor =
                    getColor(R.styleable.SMClockView_sm_large_circle_color, mLargeCircleColor)

                mSmallCircleColor =
                    getColor(R.styleable.SMClockView_sm_small_circle_color, mSmallCircleColor)


                mDashLineColor =
                    getColor(R.styleable.SMClockView_sm_dash_line_color, mDashLineColor)

                mClockColor = getColor(R.styleable.SMClockView_sm_clock_color, mClockColor)

                mClockSubLabelColor =
                    getColor(R.styleable.SMClockView_sm_clock_sub_label_color, mClockSubLabelColor)

                mClockLabel = getString(R.styleable.SMClockView_sm_clock_sub_label) ?: mClockLabel

                mPositionIconSize = getDimension(R.styleable.SMClockView_sm_icon_size, 0f)


                dayBreakHour = getInteger(R.styleable.SMClockView_sm_day_break_hour, dayBreakHour)
                daybreakMin = getInteger(R.styleable.SMClockView_sm_day_break_min, daybreakMin)
                nightFallHour =
                    getInteger(R.styleable.SMClockView_sm_night_fall_hour, nightFallHour)
                nightFallMin = getInteger(R.styleable.SMClockView_sm_night_fall_min, nightFallMin)

                mAnimationDuration =
                    getInteger(
                        R.styleable.SMClockView_sm_total_animation_duration,
                        DEFAULT_ANIMATION_DURATION
                    );

                validateHourMin()


            } finally {
                recycle()
            }
        }
    }

    private fun validateHourMin() {
        if (dayBreakHour < 0 || dayBreakHour > 23) {
            throw RuntimeException("Daybreak hour should be between 0 and 23")
        }

        if (nightFallHour < 0 || nightFallHour > 23) {
            throw RuntimeException("Night fall hour should be between 0 and 23")
        }

        if (daybreakMin < 0 || daybreakMin > 59) {
            throw RuntimeException("Daybreak hour should be between 0 and 59")
        }

        if (nightFallMin < 0 || nightFallMin > 59) {
            throw RuntimeException("Night fall min should be between 0 and 50")
        }

        mSunPositionCal = SunPositionCalculator(
            HourMin(dayBreakHour, daybreakMin),
            HourMin(nightFallHour, nightFallMin)
        )
    }


    fun getColor(@ColorRes color: Int) = ContextCompat.getColor(context, color)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val width = resolveSize(dp(240f).roundToInt(), widthMeasureSpec)
        val height = resolveSize(dp(240f).roundToInt(), heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        resolvePaddingSizes()

        //bg color for debugging purpose
//        canvas.drawColor(getColor(R.color.colorAccent))

        //half of the available canvas (excluding paddings)
        val availableHalfW = ((width - mPaddingLeft - mPaddingRight) / 2f)
        val availableHalfH = ((height - mPaddingTop - mPaddingBottom) / 2f)

        //center of available canvas
        val centerX = availableHalfW + mPaddingLeft
        val centerY = availableHalfH + mPaddingTop
        val pCenter = PointF(centerX, centerY)

        val radius = Math.min(availableHalfW, availableHalfH)

        drawCircle(canvas, pCenter, radius)
        drawDashLine(canvas, pCenter)
        drawClockText(canvas, pCenter, radius)
        drawPositionalIcon(canvas, pCenter, radius)

    }

    private fun startPositionAnimation() {

        val currentAngle = getCurrentEndAngle()
        val endAngle = 180 - currentAngle

        val duration = (mAnimationDuration * (currentAngle / 360)).toLong()

        val animator: ValueAnimator = ValueAnimator.ofFloat(POSITIONAL_START_ANGLE, endAngle)

        animator.setDuration(duration)
        animator.addUpdateListener { animation ->
            Log.d("ANIM", "animated value => " + animation.getAnimatedValue())

            val degree = animation.getAnimatedValue() as Float
            mPositionalIconPosition = degree

            if (degree <= 0f) {
                mCurrentDrawable = moonDrawable
                Logger.log("degree => 0")
            }
            invalidate()
        }
        animator.interpolator = AccelerateDecelerateInterpolator()


        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator) {
                startClockTicking()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
        animator.start()
    }

    private fun getCurrentEndAngle(): Float {
        return mSunPositionCal.getCurrentAngle(HourMin.currentHourMin)
    }

    private fun getCurrentDegreePerMin(): Float {
        return mSunPositionCal.getCurrentDegreePerMin(HourMin.currentHourMin)
    }

    private fun getCurrentPeriod(): SunPositionCalculator.PERIOD {
        return mSunPositionCal.getCurrentPeriod(HourMin.currentHourMin)
    }

    private fun startClockTicking() {
        val intentFilter = IntentFilter(Intent.ACTION_TIME_TICK)
        context.registerReceiver(mTimeReceiver, intentFilter)
    }

    private fun resolvePaddingSizes() {

        val minCanvasSize =
            Math.min(width - paddingLeft - paddingRight, height - paddingTop - paddingBottom)
        mDefPadding = dp(RATIO_DEF_PADDING * minCanvasSize)
        mClockDigitLabelSpace = dp(RATIO_CLOCK_DIGIT_LABEL_SPACE * minCanvasSize)


        mPositionIconSize =
            if (mPositionIconSize == 0f) dp(RATIO_POSITION_ICON * minCanvasSize) else mPositionIconSize


        mPositionIconBaseSize = POSITION_BASE_RATIO_TO_ICON * mPositionIconSize

        val positionIconBaseHalfSize = mPositionIconBaseSize / 2f

        mPaddingLeft = paddingLeft + mDefPadding + positionIconBaseHalfSize
        mPaddingRight = paddingRight + mDefPadding + positionIconBaseHalfSize
        mPaddingTop = paddingTop + mDefPadding + positionIconBaseHalfSize
        mPaddingBottom = paddingBottom + mDefPadding + positionIconBaseHalfSize
    }

    private fun drawPositionalIcon(canvas: Canvas, pCenter: PointF, radius: Float) {
        val positionBaseHalfHeight = mPositionIconBaseSize / 2f

        val cx = pCenter.x
        val cy = pCenter.y

        val radiant = mPositionalIconPosition * (Math.PI / 180)


        val x = radius * Math.cos(radiant)
        val y = radius * Math.sin(radiant)

        val positionIconHalfWidth = (mPositionIconSize / 2).roundToInt()
        val positionIconHalfHeight = (mPositionIconSize / 2).roundToInt()


        mPositionIconRect.top = (cy.roundToInt() - y.roundToInt()) - positionIconHalfHeight
        mPositionIconRect.left = (cx.roundToInt() + x.roundToInt()) - positionIconHalfWidth
        mPositionIconRect.right = mPositionIconRect.left + mPositionIconSize.roundToInt()
        mPositionIconRect.bottom = mPositionIconRect.top + mPositionIconSize.roundToInt()


        val positionBaseHalfWidth = mPositionIconBaseSize / 2f

        mPositionIconBaseRect.top = (cy - y.toFloat()) - positionBaseHalfHeight
        mPositionIconBaseRect.left = (cx + x.toFloat()) - positionBaseHalfWidth
        mPositionIconBaseRect.right = mPositionIconBaseRect.left + mPositionIconBaseSize
        mPositionIconBaseRect.bottom = mPositionIconBaseRect.top + mPositionIconBaseSize

        canvas.drawOval(mPositionIconBaseRect, mPositionBasePaint)

        mCurrentDrawable.setBounds(mPositionIconRect)
        mCurrentDrawable.draw(canvas)
    }

    private fun drawClockText(canvas: Canvas, pCenter: PointF, radius: Float) {

        mClockTime = getCurrentTimeIn12HourFormat()

        val cx = pCenter.x
        val cy = pCenter.y

        val bounds = Rect()
        mClockPaint.apply {
            // text size will change with the base circle
            textSize = sp(RATIO_CLOCK_DIGIT * radius)
        }
        mClockPaint.getTextBounds(mClockTime, 0, mClockTime.length, bounds)

        val clockTextWidth = mClockPaint.measureText(mClockTime)
        val clockTextHeight = bounds.height()


        val clockX = cx - (clockTextWidth / 2)
        //since text are drawn at the baseline
        val clockY = cy + (clockTextHeight / 2)


        mClockLabelPaint.apply {
            textSize = sp(radius * RATIO_CLOCK_LABEL)
        }
        val labelWidth = mClockLabelPaint.measureText(mClockLabel)


        val labelX = cx - (labelWidth / 2)
        val labelY = clockY + clockTextHeight + mClockDigitLabelSpace


        canvas.drawText(mClockTime, clockX, clockY, mClockPaint)
        canvas.drawText(mClockLabel, labelX, labelY, mClockLabelPaint)

    }

    private fun getCurrentTimeIn12HourFormat(): String {
        return HourMin.currentHourMin.formatIn12Hour()
    }

    private fun drawDashLine(canvas: Canvas, pCenter: PointF) {

        val startX = 0f
        //full width of the canvas
        val stopX = width.toFloat()
        val startY = pCenter.y
        val stopY = startY

        canvas.drawLine(startX, startY, stopX, stopY, mDashLinePaint)
    }

    private fun drawCircle(canvas: Canvas, p: PointF, radius: Float) {
        mBaseCirclePaint.apply {
            strokeWidth = dp(RATIO_BASE_CIRCLE_STROKE * radius)
        }
        canvas.drawCircle(p.x, p.y, radius, mBaseCirclePaint)

    }


    /**
     * convert sp to px using scaledensity
     *
     * @param sp to be converted
     * @return px
     */
    private fun sp(sp: Float): Float {
        return mScaleDensity * sp
    }

    /**
     * convert dp to px using density
     *
     * @param dp to be converted
     * @return
     */
    private fun dp(dp: Float): Float {
        return mDensity * dp
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mStart) {
            startPositionAnimation()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unregisterTimeReceiver()
    }

    private fun unregisterTimeReceiver() {
        context.unregisterReceiver(mTimeReceiver)
    }

    inner class TimeTickReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val delta = getCurrentDegreePerMin()
            val period = getCurrentPeriod()

            mPositionalIconPosition -= delta

            mCurrentDrawable = when (period) {
                SunPositionCalculator.PERIOD.DAY -> sunDrawable
                else -> moonDrawable
            }

            invalidate()
        }
    }


    fun setDayBreakAndNightFallHourMin(daybreak: HourMin, nightFall: HourMin): SMClockView {
        dayBreakHour = daybreak.hour
        daybreakMin = daybreak.min

        nightFallHour = nightFall.hour
        nightFallMin = nightFall.min

        validateHourMin()

        return this

    }

    fun start() {
        mStart = true
        startPositionAnimation()

    }
}