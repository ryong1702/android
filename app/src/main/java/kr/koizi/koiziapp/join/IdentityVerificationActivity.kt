package kr.koizi.koiziapp.join

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivityIdentityVerificationBinding
import kr.koizi.koiziapp.retrofit.RetrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//본인 인증 휴대폰 번호 입력 화면
class IdentityVerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIdentityVerificationBinding
            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdentityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarIdentityVerification.tvTitle.text = getString(R.string.title_verification)
        binding.toolbarIdentityVerification.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        binding.etPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val textLength =s?.length ?: 0
                binding.buttonNext.btnNext.isEnabled = textLength >= 11
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.buttonNext.btnNext.setOnClickListener{
            lifecycleScope.launch(Dispatchers.IO) {
                val phone = binding.etPhone.text.toString()
                val marketing = intent.getBooleanExtra("marketing", false)
                try {
                    withContext(Dispatchers.Main) {
                        val response = RetrofitService.retrofitService.getRequirePhone(
                            phone = phone,
                            purFlg = 0
                        )
                        runOnUiThread {
                            if (response.body()?.result == "OK") {
                                Toast.makeText(
                                    this@IdentityVerificationActivity,
                                    "인증번호를 발송하였습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                IntentBasedActivitySwitcher(this@IdentityVerificationActivity).goToIdentityVerificationCodeActivity(
                                    marketing,
                                    phone
                                )
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                            } else {
                                Toast.makeText(
                                    this@IdentityVerificationActivity,
                                    "인증번호 발송에 실패하였습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d("에러가 발생 : ", e.toString())
                    ExceptionAlertDialog.handleException(this@IdentityVerificationActivity)
                }
            }
        }
    }
}