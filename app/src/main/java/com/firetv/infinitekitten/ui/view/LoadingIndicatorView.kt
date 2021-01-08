package com.firetv.infinitekitten.ui.view

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.palette.graphics.Palette
import java.util.*

/**
 * Created by dileepan on 09/03/18.
 */
class LoadingIndicatorView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var selectedPng = ""

    private val outerThickness = dpToPx(10)
    private val outerThicknessBy2 = dpToPx(5)
    private val innerThickness = dpToPx(6)
    private val innerThicknessBy2 = dpToPx(3)

    private val innerPaint = Paint().apply {
        color = Color.parseColor("#0cb8ff")
        style = Paint.Style.STROKE
        strokeWidth = innerThickness.toFloat()
        isAntiAlias = true
        alpha = 255
    }

    private val outerPaint = Paint().apply {
        color = Color.parseColor("#dcdcdc")
        style = Paint.Style.STROKE
        strokeWidth = outerThickness.toFloat()
        isAntiAlias = true
        alpha = 255
    }

    private val radius = dpToPx(80)
    private val diameter = dpToPx(160)
    private val pngList = context.assets.list("pngs")?.toList() ?: emptyList()

    var startAngle = 0.0f

    init {
        init()
    }

    fun init() {
        selectedPng = pngList[Random().nextInt(pngList.size)]

        val bmp = BitmapFactory.decodeStream(context.assets.open("pngs/$selectedPng"))
        val palette = Palette.from(bmp).generate()

        val innerColor = palette.getVibrantColor(Color.parseColor("#0cb8ff"))
        val outerColor = palette.getDarkVibrantColor(Color.parseColor("#dcdcdc"))

        innerPaint.color = innerColor
        outerPaint.color = outerColor

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val start = (outerThickness - innerThickness).toFloat()
        val end = start + diameter.toFloat()
        canvas.drawCircle(radius.toFloat() + outerThicknessBy2, radius.toFloat() + outerThicknessBy2, radius.toFloat(), outerPaint)
        canvas.drawArc(start, start, end, end, startAngle, 180.0f, false, innerPaint)
    }

    fun getSelectedGif() = selectedPng.substringBefore(".") + ".gif"

    private fun dpToPx(dp: Int) = (dp * Resources.getSystem().displayMetrics.density).toInt()
}