package kr.koizi.koiziapp.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("result") val result: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("userId") val userId: String?,
    @SerializedName("nickName") val nickName: String?,
    @SerializedName("profile") val profile: String?,
    @SerializedName("phone") val phone: String?
)
