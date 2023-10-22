package kr.koizi.koiziapp.join

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivityRegisterPasswordBinding
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kr.koizi.koiziapp.utils.Validate.Companion.validatePassword

class RegisterPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarRegisterPassword.tvTitle.text = getString(R.string.title_sign_up)

        binding.toolbarRegisterPassword.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        val password = binding.etPassword.text
        val rePassword = binding.etRePassword.text

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.buttonNext.btnNext.isEnabled = false
                binding.tvMessage.visibility = View.GONE
                binding.tvReMessage.visibility = View.GONE
                if(!(password.length in 12 downTo 6 && validatePassword(password.toString()))) {
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
                        binding.buttonNext.btnNext.isEnabled = (rePassword?.length ?: 1) >= 6
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

        binding.buttonNext.btnNext.setOnClickListener {
            val marketing = intent.getBooleanExtra("marketing", false)
            val phone = intent.getStringExtra("phone")?:""
            val id = intent.getStringExtra("id")?:""
            IntentBasedActivitySwitcher(this).goToRegisterProfileActivity(marketing, phone, id, password.toString())
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}