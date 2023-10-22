package kr.koizi.koiziapp.join

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.constants.ApiConstants.MARKETING_URL
import kr.koizi.koiziapp.databinding.ActivityMarketingInformationBinding
import kr.koizi.koiziapp.utils.BackPressHandler

class MarketingInformationActivity : AppCompatActivity() {
    lateinit var binding: ActivityMarketingInformationBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketingInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.wvMarketingInformation.apply {
            loadUrl(MARKETING_URL)
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.setSupportZoom(true)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
        }

        binding.toolbarMarketingInformation.tvTitle.text = getString(R.string.my_info_service)
        binding.toolbarMarketingInformation.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()
    }
}