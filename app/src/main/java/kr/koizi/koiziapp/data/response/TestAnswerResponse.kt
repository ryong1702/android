package kr.koizi.koiziapp.data.response

import com.google.gson.annotations.SerializedName

data class TestAnswerResponse(
    @SerializedName("result") val result: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("safety") val safety: Int?,
    @SerializedName("explain") val explain: String?,
    @SerializedName("guide") val guide: List<List<String>>?
)
