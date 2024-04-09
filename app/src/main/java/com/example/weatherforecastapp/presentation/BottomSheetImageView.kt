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
        clipPath.moveTo(0f + cornerRadius, 0f) // Начинаем с левого верхнего угла с учетом радиуса закругления
        clipPath.lineTo(width - cornerRadius, 0f) // Линия в правый верхний угол без учета радиуса закругления
        clipPath.quadTo(width, 0f, width, 0f + cornerRadius) // Дуга к правому верхнему углу
        clipPath.lineTo(width, height) // Линия до правого нижнего угла (без закругления)
        clipPath.lineTo(0f, height) // Линия до левого нижнего угла (без закругления)
        clipPath.lineTo(0f, 0f + cornerRadius) // Линия в левый верхний угол с учетом радиуса закругления
        clipPath.quadTo(0f, 0f, 0f + cornerRadius, 0f) // Дуга к левому верхнему углу
        clipPath.close() // Замыкаем путь
    }

}