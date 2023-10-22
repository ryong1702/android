package kr.koizi.koiziapp.myPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.user.Constants
import com.kakao.sdk.user.UserApiClient
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.data.request.SecessionRequest
import kr.koizi.koiziapp.databinding.ActivityMyInfoBinding
import kr.koizi.koiziapp.main.MainActivity.Companion.userId
import kr.koizi.koiziapp.retrofit.RetrofitService
import kr.koizi.koiziapp.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarInfo.tvTitle.text = getString(R.string.my_info)
        val preferencesManager = PreferencesManager(this)
        binding.tvId.text = preferencesManager.getUserInfo().userId
        binding.tvNickName.text = preferencesManager.getUserInfo().nickName
        binding.tvPhoneNumber.text = preferencesManager.getUserInfo().phone
        binding.toolbarInfo.btnBack.setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        binding.tvButtonService.setOnClickListener {
            startActivity(Intent(this, InfoTermsOfServiceActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.tvButtonPolicy.setOnClickListener {
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.btnDialog.setOnClickListener {
            DialogHelper.showAlertDialog(
                context = this,
                title = getString(R.string.delete),
                message = getString(R.string.delete_message),
                positiveButtonText = getString(R.string.delete_account),
                positiveButtonClickListener = {
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            withContext(Dispatchers.Main) {
                                val request = SecessionRequest(userId = userId)
                                val response = RetrofitService.retrofitService.getSecession(request)

                                if (response.body()?.result == "OK") {
                                    preferencesManager.clearAllPreferences()
                                    IntentBasedActivitySwitcher(this@InfoActivity).deleteActivity()
                                    UserApiClient.instance.unlink { error ->
                                        if (error != null) {
                                            Log.d(Constants.TAG, "연결 끊기 실패: $error")
                                        }
                                        else {
                                            Log.d(Constants.TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                                        }
                                    }
                                    IntentBasedActivitySwitcher(this@InfoActivity).deleteActivity()
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                } else {
                                    runOnUiThread {
                                        Toast.makeText(
                                            this@InfoActivity,
                                            "탈퇴를 실패하였습니다. 관리자에게 문의해주시기 바랍니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.d("에러가 발생 : ", e.toString())
                            ExceptionAlertDialog.handleException(this@InfoActivity)
                        }
                    }
                },
                positiveButtonBackground = ContextCompat.getDrawable(this, R.drawable.dialog_right_button_delete)
            )
        }
    }
}