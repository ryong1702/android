package kr.koizi.koiziapp.data.response

import com.google.gson.annotations.SerializedName

data class InteriorHistoryResponse (
    @SerializedName("result") val result: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("histories") val histories: List<InteriorHistoryList>
)
data class InteriorHistoryList(
    val id: String?,
    val title: String?,
    val progress: List<ProgressList>
)

data class ProgressList(
    val flg: Int?,
    val id: String?
)