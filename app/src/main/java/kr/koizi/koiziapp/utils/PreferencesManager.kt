package kr.koizi.koiziapp.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kr.koizi.koiziapp.data.UserInfo
import kr.koizi.koiziapp.main.MainActivity

class PreferencesManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()


    fun loginPreferences(): String? {
        if (getUserInfo().userId != null && getUserInfo().phone != null) {
            val intent = Intent(context, MainActivity::class.java)
            Toast.makeText(context, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
            context.startActivity(intent)
        }
        return sharedPreferences.getString("rememberId", null)
    }

    fun setLoginInfo(userId: String, nickName: String, profileImg: String, phone: String) {
        // 로그인 정보 저장
        editor.putString("userId", userId)
        editor.putString("nickName", nickName)
        editor.putString("profileImg", profileImg)
        editor.putString("phone", phone)
        editor.putString("firebaseToken", PreferencesManager(context).getFCMToken())
        editor.apply()
    }

    fun setLoginRememberId(rememberId: String) {
        // 로그인 정보 저장
        editor.putString("rememberId", rememberId)
        editor.apply()
    }

    fun deleteRememberId() {
        // 로그인 정보 저장
        editor.remove("rememberId")
        editor.apply()
    }

    fun clearUserInfo() {
        editor.remove("userId")
        editor.remove("nickName")
        editor.remove("profileImg")
        editor.remove("phone")
        editor.apply()
    }

    fun clearAllPreferences() {
        editor.clear().apply()
    }

    fun getUserInfo(): UserInfo {
        val userId = sharedPreferences.getString("userId", null)
        val nickName = sharedPreferences.getString("nickName", null)
        val profileImg = sharedPreferences.getString("profileImg", null)
        val phone = sharedPreferences.getString("phone", null)
        val firebaseToken = sharedPreferences.getString("firebaseToken", null)
        return UserInfo(userId, nickName, profileImg, phone, firebaseToken)
    }

    fun setFCMToken(token: String) {
        editor.putString("firebaseToken", token)
        editor.apply()
    }

    fun getFCMToken(): String? {
        return sharedPreferences.getString("firebaseToken", null)
    }

    private fun getNotificationPreferences(): SharedPreferences {
        return context.getSharedPreferences("notification_switch", Context.MODE_PRIVATE)
    }

    fun isNotificationEnabled(): Boolean {
        return getNotificationPreferences().getBoolean("switch_on", true)
    }

    fun setNotificationEnabled(isEnabled: Boolean) {
        val editor = getNotificationPreferences().edit()
        editor.putBoolean("switch_on", isEnabled).apply()
    }
}