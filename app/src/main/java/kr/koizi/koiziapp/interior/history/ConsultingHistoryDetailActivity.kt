package kr.koizi.koiziapp.interior.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.constants.ApiConstants.CONSULTING_URL
import kr.koizi.koiziapp.databinding.ActivityConsultingHistoryDetailBinding
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher

class ConsultingHistoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConsultingHistoryDetailBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultingHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titleId = intent.getStringExtra("titleId") ?: ""
        val previousScreen = intent.getStringExtra("previousScreen") ?: ""

        binding.toolbarConsultingHistoryDetail.btnBack.setOnClickListener {
            when (previousScreen) {
                "ConsultingHistoryActivity" -> { IntentBasedActivitySwitcher(this@ConsultingHistoryDetailActivity).goToConsultingHistoryActivity() }
                "AlarmActivity" -> { IntentBasedActivitySwitcher(this@ConsultingHistoryDetailActivity).goToAlarmActivity() }
                else -> { onBackPressedDispatcher.onBackPressed() }
            }
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        binding.toolbarConsultingHistoryDetail.tvTitle.text = getString(R.string.text_consulting_history)


        binding.wvConsultingHistoryDetail.apply {
            loadUrl("$CONSULTING_URL$titleId")
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.setSupportZoom(true)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            settings.domStorageEnabled = true
        }
    }
}