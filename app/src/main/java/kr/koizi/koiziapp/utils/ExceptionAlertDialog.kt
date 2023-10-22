package kr.koizi.koiziapp.utils

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import kr.koizi.koiziapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExceptionAlertDialog {
    companion object {
        private var dialog: AlertDialog? = null
        suspend fun handleException(context: Context) {
            withContext(Dispatchers.Main) {
                // 통신 오류
                dialog  = AlertDialog.Builder(context)
                    .setMessage(context.getString(R.string.error_message_connect))
                    .setPositiveButton("재시작") { _, _ ->
                        (context as Activity).recreate()
                    }
                    .setNegativeButton("앱 종료") { _, _ ->
                        (context as Activity).finishAffinity()
                    }
                    .setCancelable(false)
                    .show()
            }
        }
        fun dismiss() {
            dialog?.dismiss()
        }
    }
}