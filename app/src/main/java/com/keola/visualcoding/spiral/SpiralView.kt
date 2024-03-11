package com.keola.visualcoding.spiral

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SpiralView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var rotation = 0f

    var color: Int = Color.BLACK
        set(value) {
            field = value
            paint.color = value
            invalidate() // Redraw the view with the new color
        }

    var size: Float = 1f
        set(value) {
            field = value
            invalidate() // Redraw the view with the new size
        }

    var speed: Float = 1f
        set(value) {
            field = value
            invalidate() // Redraw the view with the new speed
        }

    private val paint = Paint().apply {
        color = this@SpiralView.color
        strokeWidth = this@SpiralView.strokeWidth
1    }

    var strokeWidth: Float = 5f
        set(value) {
            field = value
            paint.strokeWidth = value
            invalidate() // Redraw the view with the new stroke width
        }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f

        var radius = 0f
        var angle = 0f

        canvas.save() // Save the current state of the canvas

        // Rotate the canvas around the center point
        canvas.rotate(rotation, centerX, centerY)

        while (radius < width / 2) {
            val x = centerX + radius * Math.cos(angle.toDouble()).toFloat() * size
            val y = centerY + radius * Math.sin(angle.toDouble()).toFloat() * size

            canvas.drawPoint(x, y, paint)

            radius += 0.1f * speed
            angle += 0.1f * speed
        }

        canvas.restore() // Restore the canvas to its previous state

        // Increment the rotation angle
        rotation += speed
        if (rotation >= 360f) {
            rotation = 0f
        }

        // Invalidate the view to trigger a redraw
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        val halfParentHeight = parentHeight / 2

        setMeasuredDimension(measuredWidth, halfParentHeight)
    }
}