package kr.koizi.koiziapp.data.request

import com.google.gson.annotations.SerializedName

data class JoinAccountInfoRequest(
    @SerializedName("marketingAgreeFlg") val marketingAgreeFlg: Boolean,
    @SerializedName("userPhoneNumber") val userPhoneNumber: String?,
    @SerializedName("userId") val userId: String?,
    @SerializedName("userPw") val userPw: String?,
    @SerializedName("nickName") val nickName: String?,
    @SerializedName("profileImg") val profileImg: String?,
    @SerializedName("channelType") val channelType: Int?,
    @SerializedName("firebaseToken") val firebaseToken: String?
)