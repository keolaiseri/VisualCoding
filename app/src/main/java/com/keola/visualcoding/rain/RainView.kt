package com.keola.visualcoding.rain

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class RainView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val rainParticleSystem = RainParticleSystem(context)
    private var lastTouchPoint: PointF = PointF()



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        val halfParentHeight = parentHeight / 2
        val halfHeightMeasureSpec = MeasureSpec.makeMeasureSpec(halfParentHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, halfHeightMeasureSpec)
    }

    fun setRainSpeed(speed: Float) {
        rainParticleSystem.setRainSpeed(speed)
    }

    fun setWindSpeed(speed: Float) {
        rainParticleSystem.setWindSpeed(speed)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchPoint.set(touchX, touchY)
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = touchX - lastTouchPoint.x
                val dy = touchY - lastTouchPoint.y
                rainParticleSystem.moveWaterDroplets(dx, dy, lastTouchPoint, width, height)
                rainParticleSystem.removeRaindropOrDropletAt(lastTouchPoint)
                lastTouchPoint.set(touchX, touchY)
            }
            MotionEvent.ACTION_UP -> {
                val dx = touchX - lastTouchPoint.x
                val dy = touchY - lastTouchPoint.y
                rainParticleSystem.setWaterDropletsVelocity(dx, dy, lastTouchPoint)
            }
        }

        return true
    }


    fun addRaindrop(x: Float) {
        rainParticleSystem.addRaindrop(x)
    }

    fun addWaterDroplet(x: Float, y: Float, size: Float) {
        rainParticleSystem.addWaterDroplet(x, y, size)
    }

    fun update(dt: Float) {
        rainParticleSystem.update(dt)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rainParticleSystem.draw(canvas)
    }


}