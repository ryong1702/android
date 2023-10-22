package kr.koizi.koiziapp.data.response

import kr.koizi.koiziapp.data.HistoryList
import com.google.gson.annotations.SerializedName
import java.util.*

data class ConsultingHistoryResponse (
    @SerializedName("result") val result: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("consultList") val consultList: List<HistoryList>?
)