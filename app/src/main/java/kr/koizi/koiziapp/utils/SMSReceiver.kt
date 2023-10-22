package kr.koizi.koiziapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage

class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            val bundle = intent?.extras
            val messages = smsMessageParse(bundle!!)
            if (messages.isNotEmpty()) {
                val content = messages[0]?.messageBody.toString()
                content.replace("[^0-9]".toRegex(), "")
            }
        }
    }

    private fun smsMessageParse(bundle: Bundle): Array<SmsMessage?> {
        val pds = bundle.getStringArray("pds") as Array<*>?
        val messages: Array<SmsMessage?> = arrayOfNulls(pds?.size ?: 0)
        pds?.let {
            for (i in it.indices) {
                messages[i] = SmsMessage.createFromPdu(it[i] as ByteArray?, bundle.getString("format"))
            }
        }
        return messages
    }
}