package com.keola.visualcoding.fireworks

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.Random

class FireworksView(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs),
    SurfaceHolder.Callback {


    // A paint object used to draw on the canvas
    private val paint: Paint = Paint()

    // A list of particles representing fireworks explosions
    private val particles: MutableList<Particle> = mutableListOf()

    var particleSize: Int = 7 // Default size


    // Variable to store the selected color
    var selectedColor: Int? = null

    // Initialize the FireworksView
    init {
        holder.addCallback(this)
        paint.color = Color.WHITE
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        particles.forEach { it.draw(canvas, paint) }
        particles.removeAll { it.lifespan <= 0 }
        invalidate()
    }


    // Called when the surface is created or recreated
    override fun surfaceCreated(holder: SurfaceHolder) {
        // Start a thread for the fireworks animation
        FireworksThread().start()
    }

    // Called when the surface is changed
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Handle changes in surface size, if needed
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Stop the thread when the surface is destroyed
    }

    override fun performClick(): Boolean {
        // Handle the click action if needed
        // Call super.performClick() to ensure accessibility events are triggered
        super.performClick()
        // Your custom handling for the click event goes here
        return true
    }

    // A thread that draws on the canvas and updates the particle positions
    private inner class FireworksThread : Thread() {
        override fun run() {
            while (true) {
                val canvas = holder.lockCanvas()
                if (canvas != null) {
                    // Update particle positions
                    updateParticles()

                    // Draw particles on the canvas
                    drawParticles(canvas)

                    holder.unlockCanvasAndPost(canvas)

                    // Sleep for a short duration to control the frame rate
                    try {
                        sleep(16)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun updateParticles() {
        for (particle in particles) {
            particle.x += particle.vx
            particle.y += particle.vy
        }
    }

    private fun drawParticles(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)

        for (particle in particles) {
            // Use the selected color if available, otherwise use the random color
            paint.color = particle.color ?: selectedColor ?: Color.WHITE
            canvas.drawCircle(particle.x, particle.y, particleSize.toFloat(), paint)
        }
    }

    fun addFirework(x: Float, y: Float) {
        val color = selectedColor ?: getRandomColor()
        
        for (i in 0 until 50) {
            val vx = (Math.random() * 10 - 5).toFloat()
            val vy = (Math.random() * 10 - 5).toFloat()
            particles.add(Particle(x, y, vx, vy, 100, color))
        }
    }


    private fun getRandomColor(): Int {
        val random = Random()
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }
}