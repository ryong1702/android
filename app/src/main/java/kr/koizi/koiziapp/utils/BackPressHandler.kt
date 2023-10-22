package kr.koizi.koiziapp.utils

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity
import kr.koizi.koiziapp.R

class BackPressHandler(private val activity: FragmentActivity) {
    private var backPressedCallback: OnBackPressedCallback? = null

    fun addBackPressedCallback() {
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity.finish()
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        }
        activity.onBackPressedDispatcher.addCallback(backPressedCallback!!)
    }

    fun removeBackPressedCallback() {
        backPressedCallback?.remove()
    }
}