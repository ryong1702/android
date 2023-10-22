package kr.koizi.koiziapp.data.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("userId") val userId: String,
    @SerializedName("userPassword") val userPassword: String,
    @SerializedName("firebaseToken") val firebaseToken: String?
)