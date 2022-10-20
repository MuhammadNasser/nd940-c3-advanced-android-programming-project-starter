package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnRepeat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var textColor = resources.getColor(R.color.white, null)
    private var backColor = resources.getColor(R.color.colorPrimary, null)
    private var circleColor = resources.getColor(R.color.colorAccent, null)
    private var loadingColor = resources.getColor(R.color.colorPrimaryDark, null)
    private var progress = 0

    private val loadingRect = Rect()
    private var valueAnimator = ValueAnimator()

    private val textPaint = Paint().apply {
        color = textColor
        textSize = 55f
        textAlign = Paint.Align.CENTER
    }

    private val arcPaint = Paint().apply {
        color = circleColor
    }

    private val backPaint = Paint().apply {
        color = backColor
    }

    private val loadingPaint = Paint().apply {
        color = loadingColor
    }

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { _, _, new ->
        when (new) {
            ButtonState.Loading -> {
                startLoadingAnimation()
            }
            else -> {
                invalidate()
            }
        }
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0) {
            backColor = getColor(R.styleable.LoadingButton_backgroundColor, backColor)
            textColor = getColor(R.styleable.LoadingButton_textColor, textColor)
            loadingColor = getColor(R.styleable.LoadingButton_loadingColor, loadingColor)
            circleColor = getColor(R.styleable.LoadingButton_circleColor, circleColor)
        }

        isClickable = true
        buttonState = ButtonState.Completed
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//        val centerText = (textPaint.ascent() + textPaint.descent()) / 2
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backPaint)


        when (buttonState) {
            ButtonState.Completed -> {
                canvas.drawText(
                    "Download",
                    width / 2f,
                    (height + 30) / 2f,
                    textPaint
                )
            }

            else -> {
                loadingRect.set(0, 0, width * progress / 360, height)
                canvas.drawRect(loadingRect, loadingPaint)

                canvas.drawArc(
                    width / 1.2f,
                    height / 4f,
                    (width / 1.2f + 80f),
                    (height / 4f) + 80f,
                    0f,
                    progress.toFloat(),
                    true,
                    arcPaint
                )


                canvas.drawText(
                    "We are loading...",
                    width / 2f,
                    (height + 30) / 2f,
                    textPaint
                )
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minW: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minW, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun performClick(): Boolean {
        super.performClick()

        isClickable = false
        buttonState = ButtonState.Loading
        invalidate()

        return true
    }

    private fun startLoadingAnimation() {
        valueAnimator = ValueAnimator.ofInt(0, 360).setDuration(1000).apply {
            addUpdateListener {
                progress = it.animatedValue as Int
                invalidate()
            }
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART

            doOnRepeat {
                buttonState = ButtonState.Completed
                isClickable = true
                invalidate()
            }
            start()
        }
    }

    fun changeButtonState(newButtonState: ButtonState) {
        buttonState = newButtonState
    }
}