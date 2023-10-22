package kr.koizi.koiziapp.interior

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivityAskBinding
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher

class AskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAsk.btnBack.setOnClickListener{
            IntentBasedActivitySwitcher(this@AskActivity).goToMainActivity()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        binding.btnAskNow.setOnClickListener {
            IntentBasedActivitySwitcher(this@AskActivity).goToAgreementCollectionPersonalInfoActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.btnAskList.setOnClickListener {
            IntentBasedActivitySwitcher(this@AskActivity).goToConsultingHistoryActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}