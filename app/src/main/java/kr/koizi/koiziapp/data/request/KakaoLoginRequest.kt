package kr.koizi.koiziapp.data.request

import com.google.gson.annotations.SerializedName

data class KakaoLoginRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("userPassword") val userPassword: String,
    @SerializedName("firebaseToken") val firebaseToken: String?,
    @SerializedName("channelType") val channelType : Int
)