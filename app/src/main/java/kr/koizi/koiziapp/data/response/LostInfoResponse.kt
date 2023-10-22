package kr.koizi.koiziapp.data.response

import com.google.gson.annotations.SerializedName

data class LostInfoResponse(
    @SerializedName("result") val result: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("userId") val userId: String?)
