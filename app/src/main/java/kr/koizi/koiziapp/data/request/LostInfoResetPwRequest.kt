package kr.koizi.koiziapp.data.request

import com.google.gson.annotations.SerializedName

data class LostInfoResetPwRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("userPw") val userPw: String
)
