package kr.koizi.koiziapp.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivityVerificationCodeBinding
import kr.koizi.koiziapp.retrofit.RetrofitService.retrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VerificationCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationCodeBinding
    private lateinit var timer: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarVerificationCode.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        timer = object : CountDownTimer(300000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                binding.tvTimer.visibility = View.VISIBLE
                binding.tvTimer.text = "남은시간 : ".plus(String.format("%2d:%02d", minutes, seconds))
            }

            override fun onFinish() {
                binding.tvTimer.visibility = View.GONE
                binding.tvFailMessage.visibility = View.VISIBLE
                binding.tvFailMessage.text = getString(R.string.message_time_over)
                binding.buttonConfirm.btnConfirm.isEnabled = false
            }
        }
        timer.start()

        binding.buttonConfirm.btnConfirm.setOnClickListener{
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val phone = intent.getStringExtra("phone") ?: ""
                    val previousScreen = intent.getStringExtra("previousScreen") ?: ""
                    val response = retrofitService.getIdFind(
                        phone = phone,
                        certification = binding.etVerificationCode.text.toString()
                    )
                    withContext(Dispatchers.Main) {
                        runOnUiThread {
                            if (response.body()?.result == "OK") {
                                val userId = response.body()?.userId.toString()
                                //비밀번호 찾기 화면에서 왔을 경우
                                if (previousScreen == "FindPassWordActivity") {
//                                startNewActivity(PasswordResetActivity::class.java, response.body()?.userId)
                                    IntentBasedActivitySwitcher(this@VerificationCodeActivity).goToPasswordResetActivity(userId)
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                    //아이디 찾기 화면에서 왔을 경우
                                } else if (previousScreen == "FindIdActivity") {
//                                startNewActivity(FoundIdActivity::class.java, response.body()?.userId)
                                    IntentBasedActivitySwitcher(this@VerificationCodeActivity).goToFoundIdActivity(userId)
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                }
                            } else {
                                Toast.makeText(
                                    this@VerificationCodeActivity,
                                    "인증번호가 틀렸습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d("에러가 발생 : ", e.toString())
                    ExceptionAlertDialog.handleException(this@VerificationCodeActivity)
                }
            }
        }

        binding.etVerificationCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val textLength = s?.length ?: 0
                binding.buttonConfirm.btnConfirm.isEnabled = textLength >= 6
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}
