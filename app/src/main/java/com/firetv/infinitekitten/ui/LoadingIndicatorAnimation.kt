package com.firetv.infinitekitten.ui

import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * Created by dileepan on 09/03/18.
 */
class LoadingIndicatorAnimation(private val indicator: LoadingIndicator) : Animation() {

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        val angle = 360.0f * interpolatedTime
        indicator.startAngle = angle

        indicator.requestLayout()
    }

}