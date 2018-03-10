package com.firetv.infinitekitten.ui.animation

import android.view.animation.Animation
import android.view.animation.Transformation
import com.firetv.infinitekitten.ui.view.LoadingIndicatorView

/**
 * Created by dileepan on 09/03/18.
 */
class LoadingIndicatorAnimation(private val indicator: LoadingIndicatorView) : Animation() {

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        val angle = 360.0f * interpolatedTime
        indicator.startAngle = angle

        indicator.requestLayout()
    }

}