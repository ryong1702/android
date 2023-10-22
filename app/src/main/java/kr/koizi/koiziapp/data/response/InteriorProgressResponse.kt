package kr.koizi.koiziapp.data.response

import com.google.gson.annotations.SerializedName

data class InteriorProgressResponse (
    @SerializedName("result") val result: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("progress") val progress: InteriorProgress
)
data class InteriorProgress(
    val title: String,
    val text: String,
    val color: String,
    val flg: Int,
    val progressId: String
)