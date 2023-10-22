package kr.koizi.koiziapp.data.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class NoticeResponse(
    @SerializedName("result") val result: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("notice") val notice: List<Notice>?
)

data class Notice(
    val id: Int?,
    val title: String?,
    val content: String?,
    val updatedAt: String?

)