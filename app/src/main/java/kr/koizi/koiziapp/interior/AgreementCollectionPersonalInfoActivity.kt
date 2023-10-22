package kr.koizi.koiziapp.interior

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.constants.ApiConstants.KAKAO_URL
import kr.koizi.koiziapp.databinding.ActivityAgreementCollectionPersonalInfoBinding
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher

class AgreementCollectionPersonalInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgreementCollectionPersonalInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgreementCollectionPersonalInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAgreementCollection.tvTitle.text = getString(R.string.privacy_collection)
        binding.toolbarAgreementCollection.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        binding.btnAgree.setOnClickListener {
            IntentBasedActivitySwitcher(this@AgreementCollectionPersonalInfoActivity).goToExternalLink(KAKAO_URL)
        }
    }
}