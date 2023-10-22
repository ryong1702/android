package kr.koizi.koiziapp.utils

import android.util.Log
import com.kakao.sdk.user.Constants
import com.kakao.sdk.user.UserApiClient

class KakaoDisconnector {
    fun unlinkKakaoAccount() {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.d(Constants.TAG, "연결 끊기 실패: $error")
            }
            else {
                Log.d(Constants.TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
    }
    fun logoutKakaoAccount() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.d(Constants.TAG, "로그아웃 실패. SDK에서 토큰 삭제됨: $error")
            }
            else {
                Log.d(Constants.TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }
}