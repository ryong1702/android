package kr.koizi.koiziapp.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivityTestQ5Binding
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher

class TestQ5Activity : AppCompatActivity() {
    private lateinit var binding: ActivityTestQ5Binding
    private var q5 = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestQ5Binding.inflate(layoutInflater)
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
            val q1 = intent.getIntExtra("q1" , 0)
            val q2 = intent.getIntExtra("q2" , 0)
            val q3 = intent.getIntExtra("q3" , 0)
            val q4 = intent.getIntExtra("q4" , 0)
            //검사 결과 화면으로 이동
            IntentBasedActivitySwitcher(this@TestQ5Activity).goToTestAnswerActivity(gender, q1, q2, q3, q4, q5)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
    private fun selectOption(selectedOption: Int) {
        when (selectedOption) {
            1 -> {
                binding.ivSelect1.setImageResource(R.drawable.q5_1_1)
                binding.ivSelect2.setImageResource(R.drawable.q5_2_0)
                binding.ivSelect3.setImageResource(R.drawable.q5_3_0)
                binding.ivSelect4.setImageResource(R.drawable.q5_4_0)
            }
            2 -> {
                binding.ivSelect1.setImageResource(R.drawable.q5_1_0)
                binding.ivSelect2.setImageResource(R.drawable.q5_2_1)
                binding.ivSelect3.setImageResource(R.drawable.q5_3_0)
                binding.ivSelect4.setImageResource(R.drawable.q5_4_0)
            }
            3 -> {
                binding.ivSelect1.setImageResource(R.drawable.q5_1_0)
                binding.ivSelect2.setImageResource(R.drawable.q5_2_0)
                binding.ivSelect3.setImageResource(R.drawable.q5_3_1)
                binding.ivSelect4.setImageResource(R.drawable.q5_4_0)
            }
            4 -> {
                binding.ivSelect1.setImageResource(R.drawable.q5_1_0)
                binding.ivSelect2.setImageResource(R.drawable.q5_2_0)
                binding.ivSelect3.setImageResource(R.drawable.q5_3_0)
                binding.ivSelect4.setImageResource(R.drawable.q5_4_1)
            }
        }
        binding.buttonNext.btnNext.isEnabled = true
        q5 = selectedOption
    }
}