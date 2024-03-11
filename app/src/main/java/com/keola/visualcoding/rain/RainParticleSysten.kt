package com.keola.visualcoding.rain

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import com.keola.visualcoding.R
import java.util.Random

class RainParticleSystem(private val context: Context) {

    private val raindrops = mutableListOf<Raindrop>()
    private val random = Random()
    private val paint = Paint()
    private val raindropBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.raindrop)
    private val waterDroplets = mutableListOf<WaterDroplet>()
    private val waterDropletBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.droplet)
    private var raindropSize = 50f // Default size


    // Add a variable to hold the speed of the rain
    private var rainSpeed: Float = 100f // Default speed

    private var windSpeed = 0f // Add a wind speed variable


    fun setWindSpeed(speed: Float) {
        // Update the wind speed
        windSpeed = speed
    }


    fun removeRaindropOrDropletAt(point: PointF) {
        raindrops.removeAll { raindrop ->
            // Increase the size of the RectF object to increase the area of touch around the raindrop
            val raindropRect = RectF(raindrop.x - 20, raindrop.y - 20, raindrop.x + raindrop.size + 20, raindrop.y + raindrop.size + 20)
            raindropRect.contains(point.x, point.y)
        }
        waterDroplets.removeAll { waterDroplet ->
            val waterDropletRect = RectF(waterDroplet.x, waterDroplet.y, waterDroplet.x + waterDroplet.size, waterDroplet.y + waterDroplet.size)
            waterDropletRect.contains(point.x, point.y)
        }
    }

    fun addWaterDroplet(x: Float, y: Float, size: Float) {
        waterDroplets.add(WaterDroplet(x, y, size))
    }

    fun addRaindrop(x: Float) {
        val vx = random.nextFloat() * 2 - 1 // -1 to 1 units per second
        val vy = random.nextFloat() * 75 + 75 // 5 to 10 units per second
        val size = random.nextFloat() * 10 + 10 // 10 to 20 pixels
        raindrops.add(Raindrop(x, 0f, vx, vy, size))
    }

    fun setRainSpeed(speed: Float) {
        // Update the speed of the rain
        rainSpeed = speed
    }


    fun update(dt: Float) {
        val iterator = raindrops.iterator()

        while (iterator.hasNext()) {
            val raindrop = iterator.next()
            raindrop.y += rainSpeed * dt
            raindrop.x += windSpeed * dt // Adjust the x-coordinate based on the wind speed
            if (raindrop.y > context.resources.displayMetrics.heightPixels) {
                iterator.remove()
            }
        }

        while (iterator.hasNext()) {
            val raindrop = iterator.next()
            raindrop.x += raindrop.vx * dt
            // Use the rainSpeed variable to update the y position of the raindrop
            raindrop.y += rainSpeed * dt
            if (raindrop.y > context.resources.displayMetrics.heightPixels) {
                iterator.remove()
            }
        }

        val dropletIterator = waterDroplets.iterator()
        while (dropletIterator.hasNext()) {
            val droplet = dropletIterator.next()
            droplet.x += droplet.vx * dt
            droplet.y += droplet.vy * dt
            if (droplet.x !in 0f..context.resources.displayMetrics.widthPixels.toFloat() ||
                droplet.y !in 0f..context.resources.displayMetrics.heightPixels.toFloat()) {
                dropletIterator.remove()
            }
        }
    }

    fun setWaterDropletsVelocity(dx: Float, dy: Float, lastTouchPoint: PointF) {
        for (droplet in waterDroplets) {
            if (lastTouchPoint.x in (droplet.x - droplet.size)..(droplet.x + droplet.size) &&
                lastTouchPoint.y in (droplet.y - droplet.size)..(droplet.y + droplet.size)) {
                droplet.vx = dx
                droplet.vy = dy
            }
        }
    }

    fun moveWaterDroplets(dx: Float, dy: Float, lastTouchPoint: PointF, width: Int, height: Int) {
        val iterator = waterDroplets.iterator()
        while (iterator.hasNext()) {
            val droplet = iterator.next()

            // Check if the last touch point is within the water droplet
            if (lastTouchPoint.x in (droplet.x - droplet.size)..(droplet.x + droplet.size) &&
                lastTouchPoint.y in (droplet.y - droplet.size)..(droplet.y + droplet.size)) {

                // Update the position of the water droplet
                droplet.x += dx
                droplet.y += dy

                // If the water droplet is outside the bounds of the RainView, remove it
                if (droplet.x !in 0f..width.toFloat() || droplet.y !in 0f..height.toFloat()) {
                    iterator.remove()
                }
            }
        }
    }


    fun draw(canvas: Canvas) {
        for (raindrop in raindrops) {
            val dst = RectF(raindrop.x, raindrop.y, raindrop.x + raindrop.size, raindrop.y + raindrop.size)
            canvas.drawBitmap(raindropBitmap, null, dst, paint)
        }

        // Draw water droplets
        for (waterDroplet in waterDroplets) {
            val dst = RectF(waterDroplet.x, waterDroplet.y, waterDroplet.x + waterDroplet.size, waterDroplet.y + waterDroplet.size)
            canvas.drawBitmap(waterDropletBitmap, null, dst, paint)
        }

    }
}