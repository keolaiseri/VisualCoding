package com.keola.visualcoding.fireworks

import android.graphics.Canvas
import android.graphics.Paint

class Particle(var x: Float, var y: Float, var vx: Float, var vy: Float, var lifespan: Int, var color: Int) {
    fun update() {
        x += vx
        y += vy
        lifespan--
    }

    fun draw(canvas: Canvas, paint: Paint) {
        paint.color = color
        canvas.drawCircle(x, y, 5f, paint)
    }
}
