package kr.koizi.koiziapp.join

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.data.request.JoinAccountInfoRequest
import kr.koizi.koiziapp.databinding.ActivityRegisterProfileBinding
import kr.koizi.koiziapp.retrofit.RetrofitService.retrofitService
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.koizi.koiziapp.utils.*
import kr.koizi.koiziapp.utils.ExceptionAlertDialog.Companion.dismiss

class RegisterProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterProfileBinding
    private var base64EncodedImage: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarRegisterProfile.tvTitle.text = getString(R.string.title_register_profile)

        binding.toolbarRegisterProfile.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        binding.buttonNext.btnNext.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val marketing = intent.getBooleanExtra("marketing", false)
                val phone = intent.getStringExtra("phone") ?: ""
                val id = intent.getStringExtra("id") ?: ""
                val password = intent.getStringExtra("password") ?: ""
                val encodedPassword = Base64.encodeToString(password.toByteArray(), Base64.DEFAULT).trim()
                val nickName = binding.etNickName.text.toString()
                val profileImg = base64EncodedImage ?: ""

                try {
                    withContext(Dispatchers.Main) {
                        val request = JoinAccountInfoRequest(
                            marketingAgreeFlg = marketing,
                            userPhoneNumber = phone,
                            userId = id,
                            userPw = encodedPassword,
                            nickName = nickName,
                            profileImg = base64EncodedImage,
                            channelType = 0,
                            firebaseToken = PreferencesManager(this@RegisterProfileActivity).getFCMToken()
                        )
                        val response = retrofitService.getRegister(request)
                        if(response.body()?.result == "OK") {
                            dismiss()
                            DialogHelper.showAlertConfirmDialog(
                                context = this@RegisterProfileActivity,
                                title = getString(R.string.agree_title_message),
                                message = getString(R.string.sign_in_message),
                                positiveButtonText = getString(R.string.yes),
                                positiveButtonClickListener = {
                                    val preferencesManager = PreferencesManager(this@RegisterProfileActivity)
                                    preferencesManager.setLoginInfo(id, nickName, profileImg, phone)
                                    IntentBasedActivitySwitcher(this@RegisterProfileActivity).goToMainActivity()
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                    Toast.makeText(this@RegisterProfileActivity, "로그인에 성공하였습니다. 메인화면으로 이동합니다.", Toast.LENGTH_SHORT).show()
                                })
                        } else {
                            Toast.makeText(
                                this@RegisterProfileActivity,
                                "회원가입에 실패하였습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.d("에러가 발생 : ", e.toString())
                    ExceptionAlertDialog.handleException(this@RegisterProfileActivity)
                }
            }

        }

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                //data 파라미터의 uri 값을 사용하여 이미지뷰에 이미지를 설정합니다.
                val uri = data?.data
                val density = resources.displayMetrics.density // 픽셀 밀도 가져오기
                val sizeInDp = 80
                val sizeInPixels = (sizeInDp * density + 0.5f).toInt()
                Glide.with(this)
                    .load(uri)
                    .override(sizeInPixels, sizeInPixels)
                    .circleCrop()
                    .into(binding.profileImage)
                val bytes = contentResolver.openInputStream(uri!!)?.use { it.readBytes()}
                base64EncodedImage = Base64.encodeToString(bytes, Base64.DEFAULT)
            }
        }
        val listener = View.OnClickListener {
            launcher.launch(Intent(Intent.ACTION_PICK).apply { type = "image/*" })}
        binding.buttonProfileImage.setOnClickListener(listener)
        binding.btnUpload.setOnClickListener(listener)

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val textLength = s?.length ?: 0
                binding.buttonNext.btnNext.isEnabled = textLength >= 1
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }
        binding.etNickName.addTextChangedListener(textWatcher)
    }
}