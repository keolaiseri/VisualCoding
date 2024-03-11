package com.keola.visualcoding.writing

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class WritingView (context: Context, attrs: AttributeSet?) : View(context, attrs) {
    public val paint = Paint().apply {
        textSize = 100f
        typeface = Typeface.createFromAsset(context.assets, "cursive_font.ttf") // Replace with your cursive font file
    }
    var text: String = "Hello World"
    private val animator = ValueAnimator.ofFloat(0f, 1f)

    init {
        animator.duration = 500
        animator.addUpdateListener {
            paint.alpha = (it.animatedValue as Float * 255).toInt()
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val textWidth = paint.measureText(text)
        val centerX = (width - textWidth) / 2f
        val centerY = height / 2f

        if (textWidth > width) {
            // If the text width is greater than the view width, split the text into multiple lines
            val words = text.split(" ")
            var line = ""
            var y = centerY
            for (word in words) {
                if (paint.measureText("$line $word") > width) {
                    // Draw the current line and start a new one
                    canvas.drawText(line, centerX, y, paint)
                    line = word
                    y += paint.descent() - paint.ascent() // Move to the next line
                } else {
                    // Add the word to the current line
                    line += " $word"
                }
            }
            // Draw the last line
            canvas.drawText(line, centerX, y, paint)
        } else {
            // If the text width is less than the view width, draw the text as a single line
            canvas.drawText(text, centerX, centerY, paint)
        }
    }

    fun animateText() {
        animator.start()
    }
}