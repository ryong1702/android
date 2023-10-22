package kr.koizi.koiziapp.data.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class SecessionResponse (
    @SerializedName("result") val result: String?,
    @SerializedName("message") val message: String?
)