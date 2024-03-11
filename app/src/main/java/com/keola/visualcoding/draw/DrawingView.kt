package com.keola.visualcoding.draw

import android.content.Context
import android.graphics.Canvas
import android.graphics.ComposePathEffect
import android.graphics.DashPathEffect
import android.graphics.DiscretePathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.graphics.PathEffect
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.SumPathEffect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

enum class BrushShape {
    ROUND,
    SQUARE,
    DASH,
    DISCRETE,
    PATH_DASH,
    COMPOSE,
    SUM
}

class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var brushShape: BrushShape = BrushShape.ROUND

    private var drawPath: Path = Path()
    private var drawPaint: Paint = Paint()

    init {
        setupPaint()
    }

    fun getBrushShape(): BrushShape {
        return brushShape
    }

    private fun setupPaint() {

        drawPaint.pathEffect = getPathEffect(brushShape)

        drawPaint.color = 0xFF000000.toInt()
        drawPaint.isAntiAlias = true
        drawPaint.strokeWidth = 5f
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND

        drawPaint.strokeCap = if (brushShape == BrushShape.ROUND) Paint.Cap.ROUND else Paint.Cap.SQUARE

    }

    fun setBrushShape(shape: BrushShape) {
        brushShape = shape
        drawPaint.pathEffect = getPathEffect(brushShape)
    }

    private fun getPathEffect(shape: BrushShape): PathEffect {
        return when (shape) {
            BrushShape.DASH -> DashPathEffect(floatArrayOf(10f, 10f), 0f)
            BrushShape.DISCRETE -> DiscretePathEffect(10f, 5f)
            BrushShape.PATH_DASH -> {
                val dashPath = Path()
                dashPath.lineTo(10f, 20f)
                dashPath.lineTo(20f, 0f)
                dashPath.close()
                PathDashPathEffect(dashPath, 12f, 0f, PathDashPathEffect.Style.ROTATE)
            }
            BrushShape.COMPOSE -> ComposePathEffect(DashPathEffect(floatArrayOf(10f, 10f), 0f), DiscretePathEffect(10f, 5f))
            BrushShape.SUM -> SumPathEffect(DashPathEffect(floatArrayOf(10f, 10f), 0f), DiscretePathEffect(10f, 5f))
            else -> DiscretePathEffect(0f, 0f) // return a PathEffect that doesn't change the path
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(drawPath, drawPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        // Only allow drawing on the top half of the screen
        if (touchY <= height / 2) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    drawPath.moveTo(touchX, touchY)
                }
                MotionEvent.ACTION_MOVE -> {
                    drawPath.lineTo(touchX, touchY)
                }
                MotionEvent.ACTION_UP -> {
                    drawPath.lineTo(touchX, touchY)
                }
                else -> return super.onTouchEvent(event)
            }

            invalidate()
            return true
        }

        // Ignore touch events on the bottom half of the screen
        return super.onTouchEvent(event)
    }

        fun setPaintColor(color: Int) {
        drawPaint.color = color
    }

    fun clearCanvas() {
        drawPath.reset()
        invalidate()
    }

    fun setStrokeSize(size: Float) {
        drawPaint.strokeWidth = size
    }

    fun setPaintOpacity(opacity: Int) {
        val alpha = (opacity * 25.5).toInt()
        drawPaint.alpha = alpha
    }

}