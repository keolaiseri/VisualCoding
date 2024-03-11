package com.keola.visualcoding.tunnel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class TunnelView : View {

    private lateinit var stars: Array<Star>
    private val paint = Paint()
    var shape: String = "star"
    var color: Int = Color.YELLOW
    var size: Float = 5f
    var speed: Float = 5f
    var isFullscreen: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        stars = Array(800) { Star(w, h / 2, shape, color, size) }
    }

    fun setStars(stars: ArrayList<Star>) {
        this.stars = stars.toTypedArray()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d("TunnelView", "Drawing shape: $shape")
        if (!isFullscreen) {
            canvas.clipRect(0, 0, width, height / 2) // Clip the canvas to the top half of the screen only if not in fullscreen mode
        }
        canvas.drawColor(Color.WHITE)
        for (star in stars) {
            star.update(speed)
            star.show(canvas, paint, shape, color, size)
        }
        invalidate()
    }
}