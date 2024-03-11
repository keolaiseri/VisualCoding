package com.keola.visualcoding.tunnel

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.Shader

class Star(private val width: Int, private val height: Int, var shape: String, var color: Int, var size: Float) {

    var x = (Math.random() * width).toFloat() - width / 2
    var y = (Math.random() * height).toFloat() - height / 2
    var z = (Math.random() * width).toFloat()

    fun show(canvas: Canvas, paint: Paint, shape: String, color: Int, size: Float) {
        val sx = x / z * width + width / 2
        val sy = y / z * height + height / 2
        val r = width / z * size

        when (shape) {
            "star;" -> drawStar(canvas, paint, sx, sy, 5, r, r / 2, color)
            "circle;" -> drawCircle(canvas, paint, sx, sy, r, color)
            "hexagon;" -> drawHexagon(canvas, paint, sx, sy, r, color)
            "diamond;" -> drawDiamond(canvas, paint, sx, sy, r, color)
            "orb;" -> drawOrb(canvas, paint, sx, sy, r, color)
            // Add more shapes here
        }

    }

    fun drawCircle(canvas: Canvas, paint: Paint, cx: Float, cy: Float, radius: Float, color: Int) {
        paint.color = color
        canvas.drawCircle(cx, cy, radius, paint)
    }

    fun drawStar(canvas: Canvas, paint: Paint, cx: Float, cy: Float, spikes: Int, outerRadius: Float, innerRadius: Float, color: Int) {
        val angle = Math.PI * 2 / spikes
        val path = Path()

        path.moveTo(cx, cy - outerRadius)

        for (i in 1 until spikes * 2) {
            val r = if (i % 2 == 0) outerRadius else innerRadius
            val currX = cx + r * Math.sin(i * angle).toFloat()
            val currY = cy - r * Math.cos(i * angle).toFloat()
            path.lineTo(currX, currY)
        }

        path.close()
        paint.color = color
        canvas.drawPath(path, paint)
    }

    fun drawHexagon(canvas: Canvas, paint: Paint, cx: Float, cy: Float, radius: Float, color: Int) {
        val angle = Math.PI * 2 / 6
        val path = Path()

        path.moveTo(cx + radius * Math.sin(0.0).toFloat(), cy - radius * Math.cos(0.0).toFloat())

        for (i in 1 until 6) {
            val currX = cx + radius * Math.sin(i * angle).toFloat()
            val currY = cy - radius * Math.cos(i * angle).toFloat()
            path.lineTo(currX, currY)
        }

        path.close()
        paint.color = color
        canvas.drawPath(path, paint)
    }

    fun drawDiamond(canvas: Canvas, paint: Paint, cx: Float, cy: Float, radius: Float, color: Int) {
        val path = Path()

        path.moveTo(cx, cy - radius) // Top point
        path.lineTo(cx + radius, cy) // Right point
        path.lineTo(cx, cy + radius) // Bottom point
        path.lineTo(cx - radius, cy) // Left point
        path.close()

        paint.color = color
        canvas.drawPath(path, paint)
    }

    fun drawOrb(canvas: Canvas, paint: Paint, cx: Float, cy: Float, radius: Float, color: Int) {
        val darkerColor = color.darker()
        val gradient = RadialGradient(cx, cy, radius, color, darkerColor, Shader.TileMode.CLAMP)
        paint.shader = gradient
        canvas.drawCircle(cx, cy, radius, paint)
        paint.shader = null // Reset the shader
    }

    fun Int.darker(): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(this, hsv)
        hsv[2] *= 0.8f // reduce value component to darken the color
        return Color.HSVToColor(hsv)
    }

    fun update(speed: Float) {
        z -= speed
        if (z <= 0) {
            z = width.toFloat()
            x = (Math.random() * width).toFloat() - width / 2
            y = (Math.random() * height).toFloat() - height / 2
        }


    }


}