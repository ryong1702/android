package kr.koizi.koiziapp.myPage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivityAppSettingBinding
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.PreferencesManager

class AppSettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAppSetting.tvTitle.text = getString(R.string.app_setting)
        binding.tvVersion.text = getString(R.string.version)
        binding.toolbarAppSetting.btnBack.setOnClickListener{
            startActivity(Intent(this, MyPageActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        val preferencesManager = PreferencesManager(this)
        binding.pushSwitch.isChecked = preferencesManager.isNotificationEnabled()

        binding.pushSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferencesManager.setNotificationEnabled(isChecked) // 수정된 부분
        }
    }
}