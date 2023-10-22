package kr.koizi.koiziapp.data.response

import com.google.gson.annotations.SerializedName

data class SampleHistoryResponse(
    @SerializedName("result") val result: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("sampleTestList") val sampleTestList: List<SampleList>?
)

data class SampleList(
    val id: Int?,
    val title: String?,
    val sex: Int?,
    val answer: List<Int>?,
    val answerId: String?,
    val createdAt: String
)