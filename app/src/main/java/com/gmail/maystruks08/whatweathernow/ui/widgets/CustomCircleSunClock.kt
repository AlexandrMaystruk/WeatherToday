package com.gmail.maystruks08.whatweathernow.ui.widgets

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.gmail.maystruks08.whatweathernow.R
import java.util.*
import kotlin.math.cos
import kotlin.math.sin


class CustomCircleSunClock(context: Context, attrs: AttributeSet) : View(context, attrs) {


    private var startIcon: Bitmap
    private var finishIcon: Bitmap
    private var radius = 0f
    private var currentProgressTime = 0f
    private var startAngle = 180
    private var color = Color.TRANSPARENT
    private var rectF: RectF = RectF()
    private var paint: Paint
    private var strokeWidth = resources.getDimension(R.dimen.default_stoke_width)
    private var sunrise: Long = 0
    private var sunset: Long = 0
    var min: Int = 0

    fun setProgress(progress: Long) {
        currentProgressTime = progress.toFloat()
        invalidate()
    }


    fun setSunriseSunsetTime(sunriseTime: Long, sunsetTime: Long) {
        sunrise = sunriseTime
        sunset = sunsetTime
        invalidate()
    }


    fun setStrokeWidth(strokeWidth: Float) {
        this.strokeWidth = strokeWidth
        paint.strokeWidth = strokeWidth
        requestLayout()
    }


    fun setColor(color: Int) {
        this.color = color
        paint.color = color
        requestLayout()
    }


    init {

        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomCircleProgress,
            0, 0
        )

        try {
            strokeWidth = typedArray.getDimension(R.styleable.CustomCircleProgress_stokeWidth, strokeWidth)
            currentProgressTime =
                    typedArray.getFloat(R.styleable.CustomCircleProgress_sunClockProgress, currentProgressTime)
            color = typedArray.getInt(R.styleable.CustomCircleProgress_colorProgress, color)

            startAngle = typedArray.getInt(R.styleable.CustomCircleProgress_startAngle, startAngle)

            startIcon = drawableToBitmap(resources.getDrawable(R.drawable.ic_sunrise))
            finishIcon = drawableToBitmap(resources.getDrawable(R.drawable.ic_sunset))

        } finally {
            typedArray.recycle()
        }

        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        rectF.set(
            0f + strokeWidth / 2 + 30,
            0f + strokeWidth / 2 + 30,
            min.toFloat() - strokeWidth / 2 - 30,
            min.toFloat() - strokeWidth / 2 - 30
        )

        paint.color = resources.getColor(R.color.attention_capacity_50)
        paint.strokeWidth = strokeWidth + 2
        canvas.drawArc(rectF, startAngle.toFloat(), 180f, false, paint)

        paint.color = resources.getColor(R.color.attention)

        var progress = currentProgressTime - sunrise
        val max = sunset - sunrise
        if (currentProgressTime >= sunset) progress = max.toFloat()
        val angle = 180 * progress / max
        canvas.drawArc(rectF, startAngle.toFloat(), angle, false, paint)

        drawSunIcon(canvas)

    }


    private fun drawSunIcon(canvas: Canvas) {

        canvas.drawBitmap(
            startIcon,
            ((width / 2 - 24) + cos(Math.toRadians(startAngle.toDouble())) * radius).toFloat(),
            ((height / 2 - 24) + sin(Math.toRadians(startAngle.toDouble())) * radius).toFloat(),
            null
        )

        canvas.drawBitmap(
            finishIcon,
            ((width / 2 - 44) + cos(Math.toRadians(0.toDouble())) * radius).toFloat(),
            ((height / 2 - 24) + sin(Math.toRadians(0.toDouble())) * radius).toFloat(),
            null
        )

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val height = View.getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        min = Math.min(width, height)
        setMeasuredDimension(min, min)

        radius = (min / 2 - 30).toFloat()
        rectF.set(
            0f + strokeWidth / 2,
            0f + strokeWidth / 2,
            min.toFloat() - strokeWidth / 2,
            min.toFloat() - strokeWidth / 2
        )

    }


    private fun drawableToBitmap(drawable: Drawable): Bitmap {

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}
