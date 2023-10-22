package kr.koizi.koiziapp.myPage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivityMyPageBinding
import com.bumptech.glide.Glide
import com.kakao.sdk.user.Constants.TAG
import com.kakao.sdk.user.UserApiClient
import kr.koizi.koiziapp.utils.*

class MyPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferencesManager = PreferencesManager(this)
        val nickName = preferencesManager.getUserInfo().nickName.toString()
        val encodeProfile = preferencesManager.getUserInfo().profileImg.toString()
        val decodedBytes: ByteArray = Base64.decode(encodeProfile, Base64.DEFAULT)
        val decodeProfile: Bitmap? = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

        if (decodeProfile != null) {
            // 프로필 이미지
            Glide.with(this)
                .load(decodeProfile)
                .circleCrop()
                .into(binding.ivProfile)
        }
        binding.toolbarMyPage.tvTitle.text = getString(R.string.my_page)
        binding.tvNickName.text = nickName

        // 뒤로가기 버튼
        binding.toolbarMyPage.btnBack.setOnClickListener {
            IntentBasedActivitySwitcher(this@MyPageActivity).goToMainActivity()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        binding.btnMyInfo.setOnClickListener {
            IntentBasedActivitySwitcher(this@MyPageActivity).gotoInfoActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.tvNotice.setOnClickListener {
            IntentBasedActivitySwitcher(this@MyPageActivity).gotoNoticeActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.tvAppSetting.setOnClickListener {
            IntentBasedActivitySwitcher(this@MyPageActivity).gotoAppSettingActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.tvInteriorHistory.setOnClickListener {
            IntentBasedActivitySwitcher(this@MyPageActivity).goToProgressHistory()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        binding.btnDialog.setOnClickListener {
            DialogHelper.showAlertDialog(
                context = this,
                title = getString(R.string.logout),
                message = getString(R.string.logout_message),
                positiveButtonText = getString(R.string.yes),
                positiveButtonClickListener = {
                    preferencesManager.clearUserInfo()
                    val kakaoDisconnector = KakaoDisconnector()
                    kakaoDisconnector.unlinkKakaoAccount()
                    // 로그아웃
                    IntentBasedActivitySwitcher(this@MyPageActivity).deleteActivity()
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                })
        }
    }
}