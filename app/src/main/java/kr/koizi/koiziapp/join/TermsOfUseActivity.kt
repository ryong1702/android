package kr.koizi.koiziapp.join

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.koizi.koiziapp.BuildConfig
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.data.request.JoinAccountInfoRequest
import kr.koizi.koiziapp.databinding.ActivityTermsOfUseBinding
import kr.koizi.koiziapp.main.MainActivity.Companion.nickName
import kr.koizi.koiziapp.main.MainActivity.Companion.userId
import kr.koizi.koiziapp.retrofit.RetrofitService
import kr.koizi.koiziapp.utils.*
import kr.koizi.koiziapp.utils.ExceptionAlertDialog.Companion.dismiss

// 이용약관 동의 화면
class TermsOfUseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTermsOfUseBinding
    private val kakaoDisconnector = KakaoDisconnector()
    private var kakaoDeleteToken = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 앱 종료 시에만 토큰 삭제 작업을 수행
        kakaoDeleteToken = true

        binding = ActivityTermsOfUseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarTermsOfUse.btnBack.setOnClickListener {
            IntentBasedActivitySwitcher(this).goToLoginActivity()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            kakaoDisconnector.unlinkKakaoAccount()
            kakaoDisconnector.logoutKakaoAccount()
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        binding.buttonNext.btnNext.setOnClickListener{
            val flg = intent.getIntExtra("flg", 0)
            val marketing = binding.chkMarketing.isChecked
            dismiss()
            when (flg) {
                1 -> DialogHelper.showAlertConfirmDialog(
                        context = this@TermsOfUseActivity,
                        title = getString(R.string.agree_title_message),
                        message = getString(R.string.sign_in_message),
                        positiveButtonText = getString(R.string.yes),
                        positiveButtonClickListener = {
                            lifecycleScope.launch(Dispatchers.IO) {
                                try {
                                    withContext(Dispatchers.Main) {
                                        val request = JoinAccountInfoRequest(
                                            marketingAgreeFlg = marketing,
                                            userPhoneNumber = "",
                                            userId = userId,
                                            userPw = BuildConfig.KAKAO_LOGIN_PASSWORD,
                                            nickName = nickName,
                                            profileImg = "",
                                            channelType = flg,
                                            firebaseToken = PreferencesManager(this@TermsOfUseActivity).getFCMToken()
                                        )
                                        val response =
                                            RetrofitService.retrofitService.getRegister(request)
                                        if (response.body()?.result == "OK") {
                                            val preferencesManager =
                                                PreferencesManager(this@TermsOfUseActivity)
                                            preferencesManager.setLoginInfo(
                                                userId,
                                                nickName,
                                                "",
                                                ""
                                            )
                                            Toast.makeText(this@TermsOfUseActivity, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                                            IntentBasedActivitySwitcher(this@TermsOfUseActivity).goToMainActivity()
                                        }
                                    }
                                } catch (e: Exception) {
                                    Log.d("에러가 발생 : ", e.toString())
                                    ExceptionAlertDialog.handleException(this@TermsOfUseActivity)
                                }
                            }
                        })
                else -> IntentBasedActivitySwitcher(this).goToIdentityVerificationActivity(marketing)
            }
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.tvShowService.setOnClickListener{
            IntentBasedActivitySwitcher(this).goToInfoTermsOfServiceActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.tvShowPrivacy.setOnClickListener{
            IntentBasedActivitySwitcher(this).goToPrivacyPolicyActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.tvShowMarketing.setOnClickListener{
            IntentBasedActivitySwitcher(this).goToMarketingInformationActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // 체크박스들을 배열로 만들기
        val checkboxes =
            arrayOf(binding.chkAge, binding.chkService, binding.chkPersonal, binding.chkMarketing)

        // 체크박스의 상태가 변경될 때 호출되는 함수
        fun onCheckboxChanged() {
            // 필수 선택 3개가 모두 체크되어 있으면 버튼을 활성화하고, 그렇지 않으면 비활성화하기
            binding.buttonNext.btnNext.isEnabled = checkboxes.sliceArray(0..2).all { it.isChecked }
        }

        // 전체선택 체크박스를 체크하거나 해제하는 함수
        fun checkAll(checked: Boolean) {
            // 배열의 각 요소에 대해 체크박스의 상태를 checked로 변경하기
            checkboxes.forEach { it.isChecked = checked }
        }

        // 필수 선택 3개와 그냥 선택이 모두 체크되어 있으면 전체선택 체크박스를 체크하고, 그렇지 않으면 해제하는 함수
        fun checkMaster() {
            // 모든 체크박스가 체크되어 있으면 전체선택 체크박스를 체크하고, 그렇지 않으면 해제하기
            binding.chkMaster.isChecked = checkboxes.all { it.isChecked }
        }

        // 각 체크박스에 리스너 설정하기
        binding.chkMaster.setOnCheckedChangeListener { buttonView, isChecked ->
            // 전체선택 체크박스가 사용자에 의해 클릭되었으면 다른 모든 체크박스의 상태를 변경하고 버튼의 활성화 상태를 변경하기
            if (buttonView.isPressed) {
                checkAll(isChecked)
                onCheckboxChanged()
            }
        }

        // 다른 체크박스들에도 리스너 설정하기
        checkboxes.forEach {
            it.setOnCheckedChangeListener { _, _ ->
                // 전체선택 체크박스의 상태를 변경하고 버튼의 활성화 상태를 변경하기
                checkMaster()
                onCheckboxChanged()
            }
        }
    }
    override fun onDestroy() {
        kakaoDisconnector.unlinkKakaoAccount()
        kakaoDisconnector.logoutKakaoAccount()
        super.onDestroy()
    }

    override fun onStop() {
        if (kakaoDeleteToken) {
            kakaoDisconnector.unlinkKakaoAccount()
            kakaoDisconnector.logoutKakaoAccount()
        }
        super.onStop()
    }
    override fun onResume() {
        // 앱으로 다시 돌아왔을 때 토큰 삭제 작업을 수행하지 않도록 설정
        kakaoDeleteToken = false
        super.onResume()
    }
}