package kr.koizi.koiziapp.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivityFoundIdBinding
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher

// 핸드폰 인증으로 아이디 찾은 화면
class FoundIdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoundIdBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFoundIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기 버튼
        binding.toolbarFoundId.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        // 이전 화면에서 intent로 전달한 아이디를 표시
        binding.tvFoundId.text = intent.getStringExtra("id")

        // 로그인 화면으로 이동
        binding.btnLogin.setOnClickListener{
            IntentBasedActivitySwitcher(this@FoundIdActivity).goToLoginActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // 패스워드 찾기 화면으로 이동
        binding.btnFindPassword.setOnClickListener{
            IntentBasedActivitySwitcher(this@FoundIdActivity).goToFindPassWordActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}