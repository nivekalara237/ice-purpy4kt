package com.nivekaa.icepurpykt.infra.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.nivekaa.icepurpykt.application.AbstractAppActivity
import com.nivekaa.icepurpykt.R
import com.nivekaa.icepurpykt.domain.spi.SessionStoragePort
import com.nivekaa.icepurpykt.infra.storage.SessionStorage
import butterknife.BindView


class SplashScreenActivity : AbstractAppActivity() {
    private var animation: Animation? = null
    @JvmField
    @BindView(R.id.logo_img)
    var logo: ImageView? = null

    @JvmField
    @BindView(R.id.track_txt)
    var appTitle: TextView? = null

    @JvmField
    @BindView(R.id.pro_txt)
    var appSlogan: TextView? = null
    private var sessionStoragePort: SessionStoragePort? = null
    override fun hasErrorView(): Boolean {
        return false
    }

    override fun hasToolBar(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sessionStoragePort = SessionStorage.getInstance(this)
        val fontPath = "font/CircleD_Font_by_CrazyForMusic.ttf"
        val tf: Typeface = Typeface.createFromAsset(assets, fontPath)
        appTitle?.setTypeface(tf)
        appSlogan?.setTypeface(tf)
        val isFirstTime: Boolean = sessionStoragePort?.retrieveDataBoolean(FIRST_TIME) == true
        if (isFirstTime) {
            if (savedInstanceState == null) {
                flyIn()
            }
            sessionStoragePort?.saveDataBoolean(FIRST_TIME, true)
            Handler().postDelayed({ endSplash() }, 3500)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun flyIn() {
        animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        logo!!.startAnimation(animation)
        animation = AnimationUtils.loadAnimation(
            this,
            R.anim.app_name_animation
        )
        appTitle?.startAnimation(animation)
        animation = AnimationUtils.loadAnimation(this, R.anim.pro_animation)
        appSlogan?.startAnimation(animation)
    }

    private fun endSplash() {
        animation = AnimationUtils.loadAnimation(
            this,
            R.anim.logo_animation_back
        )
        logo!!.startAnimation(animation)
        animation = AnimationUtils.loadAnimation(
            this,
            R.anim.app_name_animation_back
        )
        appTitle?.startAnimation(animation)
        animation = AnimationUtils.loadAnimation(
            this,
            R.anim.pro_animation_back
        )
        appSlogan?.startAnimation(animation)
        animation?.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(arg0: Animation) {
                val intent = Intent(
                    applicationContext,
                    MainActivity::class.java
                )
                startActivity(intent)
                finish()
            }

            override fun onAnimationRepeat(arg0: Animation) {}
            override fun onAnimationStart(arg0: Animation) {}
        })
    }

    override fun onBackPressed() {
        // Do nothing
    }

    companion object {
        const val FIRST_TIME = "ipkt_is_my_first_time_ID"
    }
}