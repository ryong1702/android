package kr.koizi.koiziapp.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivityFindPasswordBinding
import kr.koizi.koiziapp.retrofit.RetrofitService.retrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FindPassWordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFindPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFindPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarFindPassword.btnBack.setOnClickListener {
            IntentBasedActivitySwitcher(this).goToLoginActivity()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        val id = binding.etId.text
        val phone = binding.etPhone.text

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (id.isNotEmpty() && phone.isNotEmpty()) {
                    binding.buttonReceive.btnReceive.isEnabled =
                         id.length >= 4 && phone.length >= 11
                } else {
                    binding.buttonReceive.btnReceive.isEnabled = false
                }

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }
        binding.etId.addTextChangedListener(textWatcher)
        binding.etPhone.addTextChangedListener(textWatcher)

        binding.buttonReceive.btnReceive.setOnClickListener{
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val response = retrofitService.getRequirePw(
                        phone = phone.toString(),
                        userId = id.toString()
                    )
                    withContext(Dispatchers.Main) {
                        runOnUiThread {
                            if (response.body()?.result.toString() == "OK") {
                                Toast.makeText(
                                    this@FindPassWordActivity,
                                    "인증번호를 발송하였습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                IntentBasedActivitySwitcher(this@FindPassWordActivity).goToVerificationCodeActivity(
                                    id.toString(),
                                    phone.toString(),
                                    "FindPassWordActivity"
                                )
                                overridePendingTransition(
                                    R.anim.slide_in_right,
                                    R.anim.slide_out_left
                                )
                            } else {
                                Toast.makeText(
                                    this@FindPassWordActivity,
                                    response.body()?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d("에러가 발생 : ", e.toString())
                    ExceptionAlertDialog.handleException(this@FindPassWordActivity)
                }
            }
        }
    }
}