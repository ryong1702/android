package kr.koizi.koiziapp.data.request

import com.google.gson.annotations.SerializedName

data class TestAnswerRequest(
    @SerializedName("answers") val answers: List<Int>?
)