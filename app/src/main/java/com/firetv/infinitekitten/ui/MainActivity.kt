package com.firetv.infinitekitten.ui

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.animation.AnimationUtils
import com.firetv.infinitekitten.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()

        videosMeButton.hasFocus()
    }

    private fun setupUI() {
        videosMeButton.setOnFocusChangeListener { v, hasFocus ->
            onFocusChangeListener(v, hasFocus)
        }

        videosHumanButton.setOnFocusChangeListener { v, hasFocus ->
            onFocusChangeListener(v, hasFocus)
        }

    }

    private fun onFocusChangeListener(v: View, hasFocus: Boolean) {
        val anim = AnimationUtils.loadAnimation(this,
                if (hasFocus) R.anim.scale_in else R.anim.scale_out)
        v.startAnimation(anim)
        anim.fillAfter = true
    }
}
