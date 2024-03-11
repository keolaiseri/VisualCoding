package com.keola.visualcoding.fractal

import android.animation.ArgbEvaluator
import android.animation.TimeAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

class FractalView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var shape: String = "Star" // Add this line
    private val paint = Paint()
    private var colors = arrayOf(Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA)
    private var radius = 0f
    private var rotationAngle = 0.0
    private var growthAnimator: ValueAnimator? = null
    private var rotationAnimator: ValueAnimator? = null
    private var colorAnimator: ValueAnimator? = null
    private var liquidAnimator: TimeAnimator? = null
    private var rotationSpeed = 0.01 // Add this line




    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f

        rotationAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 5000
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                rotationAngle += rotationSpeed // Use rotationSpeed here
                invalidate()
            }
            start()
        }

        colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), *colors).apply {
            duration = colors.size * 1000L
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animation ->
                paint.color = animation.animatedValue as Int
                invalidate()
            }
            start()
        }

        liquidAnimator = TimeAnimator().apply {
            setTimeListener { _, _, _ ->
                rotationAngle += 0.01
                invalidate()
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f

        if (radius > 0) {
            drawFractal(canvas, centerX, centerY, radius, rotationAngle, 0)
        }
    }

    fun setColorScheme(colorScheme: Array<Int>) {
        colors = colorScheme
        colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), *colors).apply {
            duration = colors.size * 1000L
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animation ->
                paint.color = animation.animatedValue as Int
                invalidate()
            }
            start()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            growthAnimator?.cancel()

            val centerX = width / 2f
            val centerY = height / 2f
            val finalRadius = Math.min(centerX, centerY) / 2

            growthAnimator = ValueAnimator.ofFloat(0f, finalRadius).apply {
                duration = 2000
                addUpdateListener { animation ->
                    radius = animation.animatedValue as Float
                    invalidate()
                }
                start()
            }
        }

        return super.onTouchEvent(event)
    }

    private fun drawFractal(canvas: Canvas, x: Float, y: Float, radius: Float, angle: Double, depth: Int) {
        if (depth == 5) return

        val corners = when (shape) {
            "Star" -> 6
            "Square" -> 4
            "Triangle" -> 3
            "Pentagon" -> 5
            else -> 6 // Default to star
        }

        for (i in 0 until corners) {
            val endX = x + radius * cos(angle + 2 * Math.PI * i / corners).toFloat()
            val endY = y + radius * sin(angle + 2 * Math.PI * i / corners).toFloat()

            canvas.drawLine(x, y, endX, endY, paint)

            drawFractal(canvas, endX, endY, radius / 2, angle + 2 * Math.PI * i / corners + Math.sin(rotationAngle).toFloat() / 10, depth + 1)
        }
    }

    fun setBranchWidth(width: Float) {
        paint.strokeWidth = width
        invalidate()
    }

    fun setFractalSpeed(speed: Float) {
        rotationSpeed = speed / 1000.0 // Update rotationSpeed here
    }
}