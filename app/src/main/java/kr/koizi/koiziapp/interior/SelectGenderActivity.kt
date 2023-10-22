package kr.koizi.koiziapp.interior

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivitySelectGenderBinding
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher

class SelectGenderActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectGenderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectGenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var gender = 0
        binding.toolbarSelectGender.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        //여자를 선택 했을 때
        binding.selectFemale.setOnClickListener {
            // 이미지뷰1을 활성화 상태로 변경
            binding.selectFemale.isEnabled = true
            // 이미지뷰1의 src 속성을 imgEnabled.png로 설정
            binding.ivFemale.setImageResource(R.drawable.female_2)
            binding.ivTextFemale.setImageResource(R.drawable.female_text1)
            // 이미지뷰2를 비활성화 상태로 변경
            binding.ivMale.isEnabled = false
            // 이미지뷰2의 src 속성을 imgDisabled.png로 설정
            binding.ivMale.setImageResource(R.drawable.male_1)
            binding.ivTextMale.setImageResource(R.drawable.male_text)
            binding.buttonNext.btnNext.isEnabled = true
            gender = 1
        }
        //남자를 선택 했을 때
        binding.selectMale.setOnClickListener {
            // 이미지뷰2를 활성화 상태로 변경
            binding.ivMale.isEnabled = true
            // 이미지뷰2의 src 속성을 imgEnabled.png로 설정
            binding.ivMale.setImageResource(R.drawable.male_2)
            binding.ivTextMale.setImageResource(R.drawable.male_text1)
            // 이미지뷰1을 비활성화 상태로 변경
            binding.ivFemale.isEnabled = false
            // 이미지뷰1의 src 속성을 imgDisabled.png로 설정
            binding.ivFemale.setImageResource(R.drawable.female_1)
            binding.ivTextFemale.setImageResource(R.drawable.female_text)
            binding.buttonNext.btnNext.isEnabled = true
            gender = 0
        }

        binding.buttonNext.btnNext.setOnClickListener {
            IntentBasedActivitySwitcher(this@SelectGenderActivity).goToTestQ1Activity(gender)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}