package kr.koizi.koiziapp.join

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivityRegisterIdBinding
import kr.koizi.koiziapp.retrofit.RetrofitService.retrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.koizi.koiziapp.utils.Validate.Companion.validateId

class RegisterIdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterIdBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterIdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val marketing = intent.getBooleanExtra("marketing", false)
        binding.toolbarRegisterId.tvTitle.text = getString(R.string.title_sign_up)
        binding.toolbarRegisterId.btnBack.setOnClickListener {
            IntentBasedActivitySwitcher(this).goToIdentityVerificationActivity(marketing)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        // API 호출 및 응답 결과를 받아와서 변수에 저장하는 코드
        binding.etId.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val textLength =s?.length ?: 0

                if(textLength >= 4) {
                    if (!validateId(binding.etId.text.toString())) {
                        binding.tvMessage.text = getString(R.string.message_id_upper_special)
                        binding.tvMessage.setTextColor(Color.parseColor("#FF0000"))
                        binding.buttonNext.btnNext.isEnabled = false
                    } else {
                        binding.tvMessage.visibility = View.VISIBLE
                        binding.tvMessage.text = getString(R.string.message_available)
                        binding.tvMessage.setTextColor(Color.parseColor("#2AC769"))
                        binding.buttonNext.btnNext.isEnabled = true
                    }
                } else {
                    binding.tvMessage.visibility = View.VISIBLE
                    binding.tvMessage.text = getString(R.string.message_id_length)
                    binding.tvMessage.setTextColor(Color.parseColor("#FF0000"))
                    binding.buttonNext.btnNext.isEnabled = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        binding.buttonNext.btnNext.setOnClickListener{
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    withContext(Dispatchers.Main) {
                        val response = retrofitService.getUserId(binding.etId.text.toString(), 0)

                        if (response.body()?.result == "OK") {
                            val phone = intent.getStringExtra("phone")?: ""
                            val id = binding.etId.text.toString()
                            IntentBasedActivitySwitcher(this@RegisterIdActivity).goToRegisterPasswordActivity(marketing, phone, id)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        } else {
                            binding.tvMessage.text = getString(R.string.message_already)
                            binding.tvMessage.setTextColor(Color.parseColor("#FF0000"))
                        }
                    }
                } catch (e: Exception) {
                    Log.d("에러가 발생 : ", e.toString())
                    ExceptionAlertDialog.handleException(this@RegisterIdActivity)
                }
            }
        }
    }
}