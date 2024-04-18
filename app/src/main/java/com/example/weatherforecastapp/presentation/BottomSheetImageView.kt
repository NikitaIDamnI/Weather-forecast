package com.example.weatherforecastapp.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class BottomSheetImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val clipPath = Path()
    private val cornerRadius = 70f // Радиус закругления углов

    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            clipPath(clipPath)
            super.onDraw(this)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        clipPath.reset()
        val width = measuredWidth.toFloat()
        val height = measuredHeight.toFloat()
        clipPath.moveTo(0f + cornerRadius, 0f)
        clipPath.lineTo(width - cornerRadius, 0f)
        clipPath.quadTo(width, 0f, width, 0f + cornerRadius)
        clipPath.lineTo(width, height)
        clipPath.lineTo(0f, height)
        clipPath.lineTo(0f, 0f + cornerRadius)
        clipPath.quadTo(0f, 0f, 0f + cornerRadius, 0f)
        clipPath.close()
    }

}