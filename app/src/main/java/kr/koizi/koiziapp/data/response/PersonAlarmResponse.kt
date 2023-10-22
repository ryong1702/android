package kr.koizi.koiziapp.data.response

import com.google.gson.annotations.SerializedName

data class PersonAlarmResponse(
    @SerializedName("result") val result: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("alarmFlg") val alarmFlg: Boolean?
)