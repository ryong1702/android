package kr.koizi.koiziapp.data.response

import com.google.gson.annotations.SerializedName

data class JoinResponse(
    @SerializedName("result") val result: String?,
    @SerializedName("message") val message: String?
)
