package kr.koizi.koiziapp.data.request

import com.google.gson.annotations.SerializedName

data class TestSaveRequest(
    @SerializedName("answers") val answers: List<Int>,
    @SerializedName("userid") val userId: String
)