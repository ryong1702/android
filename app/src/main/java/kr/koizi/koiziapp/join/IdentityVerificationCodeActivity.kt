package kr.koizi.koiziapp.join

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivityIdentityVerificationCodeBinding
import kr.koizi.koiziapp.retrofit.RetrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IdentityVerificationCodeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIdentityVerificationCodeBinding
    private lateinit var timer: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val marketing = intent.getBooleanExtra("marketing", false)

        binding = ActivityIdentityVerificationCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarIdentityVerificationCode.tvTitle.text = getString(R.string.title_verification)
        binding.toolbarIdentityVerificationCode.btnBack.setOnClickListener {
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
                binding.buttonNext.btnNext.isEnabled = false
            }
        }
        timer.start()
        binding.buttonNext.btnNext.setOnClickListener{
            lifecycleScope.launch(Dispatchers.IO) {
                val phone = intent.getStringExtra("phone") ?: ""
                try {
                    withContext(Dispatchers.Main) {
                        val response = RetrofitService.retrofitService.getPhone(
                            phone = phone,
                            certification = binding.etVerificationCode.text.toString()
                        )

                        if (response.body()?.result == "OK") {
                            binding.tvFailMessage.visibility = View.GONE
                            IntentBasedActivitySwitcher(this@IdentityVerificationCodeActivity).goToRegisterIdActivity(marketing, phone)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        } else if (response.body()?.message != "") {
                            binding.tvFailMessage.text = response.body()?.message.toString()
                            binding.tvFailMessage.visibility = View.VISIBLE
                        } else {
                            binding.tvFailMessage.text = getString(R.string.fail_verification)
                            binding.tvFailMessage.visibility = View.VISIBLE
                        }
                    }
                } catch (e: Exception) {
                    Log.d("에러가 발생 : ", e.toString())
                    ExceptionAlertDialog.handleException(this@IdentityVerificationCodeActivity)
                }
            }
        }
        binding.etVerificationCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val textLength = s?.length ?: 0
                binding.buttonNext.btnNext.isEnabled = textLength >= 6
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}