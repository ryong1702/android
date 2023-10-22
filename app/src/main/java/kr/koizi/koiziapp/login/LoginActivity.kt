package kr.koizi.koiziapp.login

import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.Constants
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.koizi.koiziapp.BuildConfig
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.data.request.KakaoLoginRequest
import kr.koizi.koiziapp.data.request.LoginRequest
import kr.koizi.koiziapp.databinding.ActivityLoginBinding
import kr.koizi.koiziapp.main.MainActivity.Companion.nickName
import kr.koizi.koiziapp.main.MainActivity.Companion.userId
import kr.koizi.koiziapp.retrofit.RetrofitService.retrofitService
import kr.koizi.koiziapp.utils.*
import kr.koizi.koiziapp.utils.ExceptionAlertDialog.Companion.dismiss

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var lastClickedTime = 0L
    private var lastBackPressedTime = 0L
    private var alertDialog: AlertDialog? = null
    private val kakaoDisconnector = KakaoDisconnector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        Firebase.messaging.isAutoInitEnabled = true

        // 뒤로가기 버튼 콜백 등록//TODO: 각 필요한 곳에 콜백 등록
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Get current time in milliseconds
                val currentTime = System.currentTimeMillis()

                // Check if back button was pressed within 2 seconds
                if (currentTime - lastBackPressedTime < 2000) {
                    // Exit the app
                    finishAffinity()
                } else {
                    // Show a toast message
                    Toast.makeText(applicationContext, "뒤로가기 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show()
                    // Update last back button press time
                    lastBackPressedTime = currentTime
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvFindId.setOnClickListener {
            // 아이디 찾기 화면
            IntentBasedActivitySwitcher(this@LoginActivity).gotoFindIdActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.tvFindPassword.setOnClickListener {
            // 패스워드 찾기 화면
            IntentBasedActivitySwitcher(this@LoginActivity).goToFindPassWordActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.tvSignup.setOnClickListener {
            // 이용약관 동의 화면
            IntentBasedActivitySwitcher(this@LoginActivity).goToTermsOfUseActivity(0)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // 로그인 정보 저장 확인
        val preferencesManager = PreferencesManager(this)
        val rememberId = preferencesManager.loginPreferences()
        if (rememberId != null) {
            binding.etId.setText(rememberId)
        }
        // token 값 넣기
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("firebaseToken", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val firebaseToken = task.result
            PreferencesManager(applicationContext).setFCMToken(firebaseToken)
            Log.d("firebaseToken", "FCM registration token: $firebaseToken")
        }

        // 카카오 로그인 정보 확인
        lifecycleScope.launch(Dispatchers.IO) {
            if (checkLogin()) {
                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                    if (error != null) {
                        Log.e(Constants.TAG, "토큰 정보 보기 실패", error)
                    } else if (tokenInfo != null) {
                        Toast.makeText(this@LoginActivity, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                        IntentBasedActivitySwitcher(this@LoginActivity).goToMainActivity()
                    }
                }
            } else {
                kakaoDisconnector.unlinkKakaoAccount()
                kakaoDisconnector.logoutKakaoAccount()
            }

        }
        val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Log.e(Constants.TAG, "접근이 거부 됨(동의 취소) $error")
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Log.e(Constants.TAG, "유효하지 않은 앱 $error")
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Log.e(Constants.TAG, "인증 수단이 유효하지 않아 인증할 수 없는 상태 $error")
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Log.e(Constants.TAG, "요청 파라미터 오류 $error")
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Log.e(Constants.TAG, "유효하지 않은 scope ID $error")
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Log.e(Constants.TAG, "설정이 올바르지 않음(android key hash) $error")
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Log.e(Constants.TAG, "서버 내부 에러 $error")
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Log.e(Constants.TAG, "앱이 요청 권한이 없음 $error")
                    }
                    else -> { // Unknown
                        Log.e(Constants.TAG, "기타 에러 $error")
                    }
                }
            }
            else if (token != null) {
                UserApiClient.instance.me { user, errorUserInfo ->
                    if (errorUserInfo != null) {
                        Log.e(Constants.TAG, "사용자 정보 요청 실패 $errorUserInfo")
                    } else if (user != null) {
                        Log.e(Constants.TAG, "사용자 정보 요청 성공 : $user")
                        userId = user.kakaoAccount?.email.toString()
                        nickName = user.kakaoAccount?.profile?.nickname.toString()
                        lifecycleScope.launch(Dispatchers.IO) {
                            try {
                                withContext(Dispatchers.Main) {
                                    val request = KakaoLoginRequest(
                                        userId = userId,
                                        userPassword = BuildConfig.KAKAO_LOGIN_PASSWORD,
                                        firebaseToken = preferencesManager.getFCMToken(),
                                        channelType = 1
                                    )
                                    val response =
                                        retrofitService.getKakaoLogin(request)
                                    if (response.body()?.result == "OK") {
                                        PreferencesManager(this@LoginActivity).setLoginInfo(
                                            userId,
                                            nickName,
                                            "",
                                            ""
                                        )
                                        IntentBasedActivitySwitcher(this@LoginActivity).goToMainActivity()
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                    } else {
                                        DialogHelper.showAlertConfirmDialog(
                                            context = this@LoginActivity,
                                            title = getString(R.string.kakao_login_complete),
                                            message = getString(R.string.kakao_login_agree),
                                            positiveButtonText = getString(R.string.yes),
                                            positiveButtonClickListener = {
                                                // 이용약관 동의 화면
                                                IntentBasedActivitySwitcher(this@LoginActivity).goToTermsOfUseActivity(1)
                                                finish()
                                            }
                                        )
                                    }
                                }
                            } catch (e: Exception) {
                                Log.d("에러가 발생 : ", e.toString())
                                val kakaoDisconnector = KakaoDisconnector()
                                kakaoDisconnector.unlinkKakaoAccount()
                                kakaoDisconnector.logoutKakaoAccount()
                                ExceptionAlertDialog.handleException(this@LoginActivity)
                            }
                        }
                    }
                    dismiss()
                }
            }
        }

        binding.kakaoLogin.setOnClickListener {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this, callback = kakaoCallback)
            }else{
                UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoCallback)
            }
        }

        binding.btnLogin.setOnClickListener {
            val id = binding.etId.text.toString()
            val pw = binding.etPassword.text.toString()

            if (id.isEmpty() && pw.isEmpty()) {
                setErrorState(errorMessage = "아이디와 패스워드를 입력해주세요")
            } else if (id.isEmpty()) {
                setErrorState(errorMessage = "아이디를 입력해주세요")
            } else if (pw.isEmpty()) {
                setErrorState(errorMessage = "패스워드를 입력해주세요")
            } else {
                val currentClickedTime = System.currentTimeMillis()
                if (currentClickedTime - 0L >= 10000L) {
                    // 중복 클릭이 아닌 경우 처리할 내용 작성
                    lastClickedTime = currentClickedTime
                    getLogin(id, pw, preferencesManager)
                }
            }
        }
    }

    private fun setErrorState(errorMessage: String) {
        binding.tvError.text = errorMessage
        binding.tvError.setTextColor(getColor(R.color.error))
    }

    private fun getLogin(id: String, pw: String, preferencesManager: PreferencesManager) {
        lifecycleScope.launch(Dispatchers.IO) {
            val encodedPassword = Base64.encodeToString(pw.toByteArray(), Base64.DEFAULT).trim()
            val firebaseToken = preferencesManager.getFCMToken()
            val request = LoginRequest(
                userId = id,
                userPassword = encodedPassword,
                firebaseToken = firebaseToken
            )

            try {
                withContext(Dispatchers.Main) {
                    val progressBar = ProgressBar(this@LoginActivity)
                    val textView = TextView(this@LoginActivity).apply {
                        text = "로그인 중..."
                        gravity = Gravity.CENTER
                    }
                    val linearLayout = LinearLayout(this@LoginActivity).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(50, 50, 50, 50)
                        addView(progressBar)
                        addView(textView)
                    }
                    alertDialog = AlertDialog.Builder(this@LoginActivity)
                        .setView(linearLayout)
                        .setCancelable(false)
                        .create()
                    alertDialog!!.show()
                }

                val response = retrofitService.getLogin(request)

                withContext(Dispatchers.Main) {
                    if (response.body()?.result.toString() == "OK") {
                        alertDialog?.dismiss()
                        // 로그인 성공 로그인 정보 저장
                        preferencesManager.setLoginInfo(response.body()?.userId.toString()
                            ,response.body()?.nickName.toString()
                            ,response.body()?.profile.toString()
                            ,response.body()?.phone.toString()
                        )
                        if (binding.cbSaveId.isChecked) {
                            preferencesManager.setLoginRememberId(response.body()?.userId.toString())
                        } else {
                            preferencesManager.deleteRememberId()
                        }
                        IntentBasedActivitySwitcher(this@LoginActivity).goToMainActivity()
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    } else {
                        // 로그인 실패
                        binding.tvError.text = "아이디 또는 패스워드를 확인해주세요."
                        binding.tvError.setTextColor(Color.RED)
                        alertDialog?.dismiss()
                    }
                }
            } catch (e: Exception) {
                Log.d("에러가 발생 : ", e.toString())
                ExceptionAlertDialog.handleException(this@LoginActivity)
                alertDialog?.dismiss()
            }
        }
    }
    private suspend fun checkLogin(): Boolean {
        val preferencesManager = PreferencesManager(this)
        val request = KakaoLoginRequest(
            userId = preferencesManager.getUserInfo().userId.toString(),
            userPassword = BuildConfig.KAKAO_LOGIN_PASSWORD,
            firebaseToken = preferencesManager.getFCMToken(),
            channelType = 1
        )
        val response = retrofitService.getKakaoLogin(request)
        // response를 분석하여 로그인이 성공했는지 여부를 판단하고 true 또는 false 리턴
        if (response.body()?.result == "NG") {
            return false
        }
        return true
    }
}