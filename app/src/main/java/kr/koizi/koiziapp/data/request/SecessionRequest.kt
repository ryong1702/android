package kr.koizi.koiziapp.data.request

import com.google.gson.annotations.SerializedName

data class SecessionRequest(
    @SerializedName("userId") val userId: String
)