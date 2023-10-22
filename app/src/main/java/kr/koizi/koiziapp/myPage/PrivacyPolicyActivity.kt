package kr.koizi.koiziapp.myPage

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.constants.ApiConstants.PRIVATE_TERMS_URL
import kr.koizi.koiziapp.databinding.ActivityPrivacyPolicyBinding
import kr.koizi.koiziapp.utils.BackPressHandler

class PrivacyPolicyActivity : AppCompatActivity() {
    lateinit var binding: ActivityPrivacyPolicyBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.wvPrivacyPolicy.apply {
            loadUrl(PRIVATE_TERMS_URL)
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.setSupportZoom(true)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        }


        binding.toolbarInfoTermsOfService.tvTitle.text = getString(R.string.title_privacy_policy)
        binding.toolbarInfoTermsOfService.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()
    }
}