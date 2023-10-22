package kr.koizi.koiziapp.data.response

import com.google.gson.annotations.SerializedName

data class AlarmResponse(
    @SerializedName("result") val result: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("alarmList") val alarmList: List<AlarmList>?
)

data class AlarmList(
    @SerializedName("title") val title: String,
    @SerializedName("alarmType") val alarmType: Int,
    @SerializedName("keyId") val keyId: String,
    @SerializedName("confirmFlg") val confirmFlg: Int,
    @SerializedName("createdAt") val createdAt: String
)