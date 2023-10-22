package kr.koizi.koiziapp.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class HistoryList (
    @SerializedName("title") val title: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("titleId") val titleId: String?
)