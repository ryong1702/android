package kr.koizi.koiziapp.myPage

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.constants.ApiConstants.SERVICE_URL
import kr.koizi.koiziapp.databinding.ActivityMyInfoTermsOfServiceBinding
import kr.koizi.koiziapp.utils.BackPressHandler

class InfoTermsOfServiceActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyInfoTermsOfServiceBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoTermsOfServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.wvInfoTermsOfService.apply {
            loadUrl(SERVICE_URL)
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.setSupportZoom(true)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        }

        binding.toolbarInfoTermsOfService.tvTitle.text = getString(R.string.my_info_service)
        binding.toolbarInfoTermsOfService.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()
    }
}