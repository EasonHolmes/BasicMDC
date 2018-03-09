package com.widget.library.progress


import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue

class ProgressBarCircularIndeterminate(context: Context, attrs: AttributeSet) : CustomView(context, attrs) {

    internal var backgroundColor = Color.parseColor("#1E88E5")


    init {
        setAttributes(attrs)

    }

    // Set atributtes of XML to View
    protected fun setAttributes(attrs: AttributeSet) {

        minimumHeight = dpToPx(32f, resources)
        minimumWidth = dpToPx(32f, resources)

        //Set background Color
        // Color by resource
        val bacgroundColor = attrs.getAttributeResourceValue(ANDROIDXML, "background", -1)
        if (bacgroundColor != -1) {
            setBackgroundColor(resources.getColor(bacgroundColor))
        } else {
            // Color by hexadecimal
            val background = attrs.getAttributeIntValue(ANDROIDXML, "background", -1)
            if (background != -1)
                setBackgroundColor(background)
            else
                setBackgroundColor(Color.parseColor("#1E88E5"))
        }

        minimumHeight = dpToPx(3f, resources)


    }

    /**
     * Make a dark color to ripple effect
     * @return
     */
    protected fun makePressColor(): Int {
        val r = this.backgroundColor shr 16 and 0xFF
        val g = this.backgroundColor shr 8 and 0xFF
        val b = this.backgroundColor shr 0 and 0xFF
        //		r = (r+90 > 245) ? 245 : r+90;
        //		g = (g+90 > 245) ? 245 : g+90;
        //		b = (b+90 > 245) ? 245 : b+90;
        return Color.argb(128, r, g, b)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (firstAnimationOver == false)
            drawFirstAnimation(canvas)
        if (cont > 0)
            drawSecondAnimation(canvas)
        invalidate()

    }

    internal var radius1 = 0f
    internal var radius2 = 0f
    internal var cont = 0
    internal var firstAnimationOver = false
    /**
     * Draw first animation of view
     * @param canvas
     */
    private fun drawFirstAnimation(canvas: Canvas) {
        if (radius1 < width / 2) {
            val paint = Paint()
            paint.isAntiAlias = true
            paint.color = makePressColor()
            radius1 = if (radius1 >= width / 2) width.toFloat() / 2 else radius1 + 1
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius1, paint)
        } else {
            val bitmap = Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.ARGB_8888)
            val temp = Canvas(bitmap)
            val paint = Paint()
            paint.isAntiAlias = true
            paint.color = makePressColor()
            temp.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (height / 2).toFloat(), paint)
            val transparentPaint = Paint()
            transparentPaint.isAntiAlias = true
            transparentPaint.color = resources.getColor(android.R.color.transparent)
            transparentPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            if (cont >= 50) {
                radius2 = if (radius2 >= width / 2) width.toFloat() / 2 else radius2 + 1
            } else {
                radius2 = if (radius2 >= width / 2 - dpToPx(4f, resources)) width.toFloat() / 2 - dpToPx(4f, resources) else radius2 + 1
            }
            temp.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius2, transparentPaint)
            canvas.drawBitmap(bitmap, 0f, 0f, Paint())
            if (radius2 >= width / 2 - dpToPx(4f, resources))
                cont++
            if (radius2 >= width / 2)
                firstAnimationOver = true
        }
    }

    internal var arcD = 1
    internal var arcO = 0
    internal var rotateAngle = 0f
    internal var limite = 0
    /**
     * Draw second animation of view
     * @param canvas
     */
    private fun drawSecondAnimation(canvas: Canvas) {
        if (arcO == limite)
            arcD += 6
        if (arcD >= 290 || arcO > limite) {
            arcO += 6
            arcD -= 6
        }
        if (arcO > limite + 290) {
            limite = arcO
            arcO = limite
            arcD = 1
        }
        rotateAngle += 4f
        canvas.rotate(rotateAngle, (width / 2).toFloat(), (height / 2).toFloat())

        val bitmap = Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.ARGB_8888)
        val temp = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = backgroundColor
        //		temp.drawARGB(0, 0, 0, 255);
        temp.drawArc(RectF(0f, 0f, width.toFloat(), height.toFloat()), arcO.toFloat(), arcD.toFloat(), true, paint)
        val transparentPaint = Paint()
        transparentPaint.isAntiAlias = true
        transparentPaint.color = resources.getColor(android.R.color.transparent)
        transparentPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        temp.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2 - dpToPx(4f, resources)).toFloat(), transparentPaint)

        canvas.drawBitmap(bitmap, 0f, 0f, Paint())
    }


    // Set color of background
    override fun setBackgroundColor(color: Int) {
        super.setBackgroundColor(resources.getColor(android.R.color.transparent))
        if (isEnabled)
            beforeBackground = backgroundColor
        this.backgroundColor = color
    }

    fun dpToPx(dp: Float, resources: Resources): Int {
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics())
        return px.toInt()
    }

    companion object {


        internal val ANDROIDXML = "http://schemas.android.com/apk/res/android"
    }

}
