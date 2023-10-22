package kr.koizi.koiziapp.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivityFindIdBinding
import kr.koizi.koiziapp.retrofit.RetrofitService.retrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FindIdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFindIdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFindIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarFindId.btnBack.setOnClickListener {
            IntentBasedActivitySwitcher(this@FindIdActivity).gotoLoginActivity()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        binding.etPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.buttonReceive.btnReceive.isEnabled = (s?.length ?: 0) >= 11
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.buttonReceive.btnReceive.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val phone = binding.etPhone.text.toString()
                    val response = retrofitService.getRequirePhone(
                        phone = phone,
                        purFlg = 1
                    )
                    withContext(Dispatchers.Main) {
                        runOnUiThread {
                            if (response.body()?.result.toString() == "OK") {
                                Toast.makeText(
                                    this@FindIdActivity,
                                    "인증번호를 발송하였습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                IntentBasedActivitySwitcher(this@FindIdActivity).goToVerificationCodeActivity(
                                    "",
                                    phone,
                                    "FindIdActivity")
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                            } else {
                                Toast.makeText(
                                    this@FindIdActivity,
                                    response.body()?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } catch (e: Exception){
                    Log.d("에러가 발생 : ", e.toString())
                    ExceptionAlertDialog.handleException(this@FindIdActivity)
                }
            }
        }
    }
}