package kr.koizi.koiziapp.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivityTestQ1Binding
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher

class TestQ1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityTestQ1Binding
    private var q1 = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestQ1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarSelectTest.btnBack.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        binding.select1.setOnClickListener { selectOption(1) }
        binding.select2.setOnClickListener { selectOption(2) }
        binding.select3.setOnClickListener { selectOption(3) }
        binding.select4.setOnClickListener { selectOption(4) }

        binding.buttonNext.btnNext.setOnClickListener {
            val gender = intent.getIntExtra("gender" , 0)
            IntentBasedActivitySwitcher(this@TestQ1Activity).goToTestQ2Activity(gender, q1)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
    private fun selectOption(selectedOption: Int) {
        when (selectedOption) {
            1 -> {
                binding.ivSelect1.setImageResource(R.drawable.q1_1_1)
                binding.ivSelect2.setImageResource(R.drawable.q1_2_0)
                binding.ivSelect3.setImageResource(R.drawable.q1_3_0)
                binding.ivSelect4.setImageResource(R.drawable.q1_4_0)
            }
            2 -> {
                binding.ivSelect1.setImageResource(R.drawable.q1_1_0)
                binding.ivSelect2.setImageResource(R.drawable.q1_2_1)
                binding.ivSelect3.setImageResource(R.drawable.q1_3_0)
                binding.ivSelect4.setImageResource(R.drawable.q1_4_0)
            }
            3 -> {
                binding.ivSelect1.setImageResource(R.drawable.q1_1_0)
                binding.ivSelect2.setImageResource(R.drawable.q1_2_0)
                binding.ivSelect3.setImageResource(R.drawable.q1_3_1)
                binding.ivSelect4.setImageResource(R.drawable.q1_4_0)
            }
            4 -> {
                binding.ivSelect1.setImageResource(R.drawable.q1_1_0)
                binding.ivSelect2.setImageResource(R.drawable.q1_2_0)
                binding.ivSelect3.setImageResource(R.drawable.q1_3_0)
                binding.ivSelect4.setImageResource(R.drawable.q1_4_1)
            }
        }
        binding.buttonNext.btnNext.isEnabled = true
        q1 = selectedOption
    }
}