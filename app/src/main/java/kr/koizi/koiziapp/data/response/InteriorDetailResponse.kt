package kr.koizi.koiziapp.data.response

import com.google.gson.annotations.SerializedName

data class InteriorDetailResponse (
    @SerializedName("result") val result: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("managerPhone") val managerPhone: String?,
    @SerializedName("imgs") val imgs: List<String>?
)