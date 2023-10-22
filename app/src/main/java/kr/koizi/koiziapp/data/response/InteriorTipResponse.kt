package kr.koizi.koiziapp.data.response

import com.google.gson.annotations.SerializedName

data class InteriorTipResponse(
    @SerializedName("result") val result: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("images") val images: List<Images>?
)

data class Images (
    val img_id: String?,
    val t_url: String?
)