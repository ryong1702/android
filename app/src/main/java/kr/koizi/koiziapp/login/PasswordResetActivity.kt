package kr.koizi.koiziapp.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.data.request.LostInfoResetPwRequest
import kr.koizi.koiziapp.databinding.ActivityPasswordResetBinding
import kr.koizi.koiziapp.retrofit.RetrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kr.koizi.koiziapp.utils.Validate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PasswordResetActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordResetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPasswordResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarPasswordReset.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        val password = binding.etPassword.text
        val rePassword = binding.etRePassword.text

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.buttonConfirm.btnConfirm.isEnabled = false
                binding.tvMessage.visibility = View.GONE
                binding.tvReMessage.visibility = View.GONE
                if(!(password.length in 12 downTo 6 && Validate.validatePassword(
                        password.toString()
                    ))) {
                    binding.tvMessage.visibility = View.VISIBLE
                    binding.tvMessage.text = getString(R.string.text_password_limit)
                    binding.tvMessage.setTextColor(ContextCompat.getColor(binding.root.context, R.color.error))
                } else if(password.toString() != rePassword.toString()){
                    binding.tvMessage.visibility = View.GONE
                    binding.tvReMessage.visibility = View.VISIBLE
                    binding.tvReMessage.text = getString(R.string.message_password_not_match)
                    binding.tvReMessage.setTextColor(ContextCompat.getColor(binding.root.context, R.color.error))
                } else {
                    if (password.isNotEmpty() && rePassword.isNotEmpty()) {
                        binding.buttonConfirm.btnConfirm.isEnabled = (rePassword?.length ?: 1) >= 6
                    }
                }

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }
        binding.etPassword.addTextChangedListener(textWatcher)
        binding.etRePassword.addTextChangedListener(textWatcher)
        binding.buttonConfirm.btnConfirm.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val id = intent.getStringExtra("id") ?: ""
                val encodedPassword = Base64.encodeToString(password.toString().toByteArray(), Base64.DEFAULT).trim()
                try {
                    withContext(Dispatchers.Main) {
                        val request = LostInfoResetPwRequest(
                            userId = id,
                            userPw = encodedPassword
                        )
                        val response = RetrofitService.retrofitService.getResetPw(request)
                        runOnUiThread {
                            if (response.body()?.result == "OK") {
                                IntentBasedActivitySwitcher(this@PasswordResetActivity).goToLoginActivity()
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                Toast.makeText(
                                    this@PasswordResetActivity,
                                    "비밀번호 재설정이 완료되었습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@PasswordResetActivity,
                                    response.body()?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d("에러가 발생 : ", e.toString())
                    ExceptionAlertDialog.handleException(this@PasswordResetActivity)
                }
            }
        }
    }
}