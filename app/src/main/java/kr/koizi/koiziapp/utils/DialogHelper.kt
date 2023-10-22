package kr.koizi.koiziapp.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import kr.koizi.koiziapp.R

class DialogHelper {
    companion object {
        // 로그아웃, 탈퇴하기, 간편테스트 저장하기 다이어로그
        fun showAlertDialog(context: Context, title: String, message: String, positiveButtonText: String, positiveButtonClickListener: () -> Unit, positiveButtonBackground: Drawable? = null) {
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_logout, null)
            val builder = AlertDialog.Builder(context)
            builder.setView(view)

            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_background)

            val titleTextView = view.findViewById<TextView>(R.id.title)
            val messageTextView = view.findViewById<TextView>(R.id.message)
            val positiveButton = view.findViewById<Button>(R.id.btn_ok)
            val cancelButton = view.findViewById<Button>(R.id.btn_cancel)

            titleTextView.text = title
            messageTextView.text = message
            positiveButton.text = positiveButtonText
            positiveButtonBackground?.let { positiveButton.background = it }

            positiveButton.setOnClickListener {
                positiveButtonClickListener()
                dialog.dismiss()
            }

            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.setCancelable(false)
            dialog.show()
        }

        // 간편테스트 저장하기 다이어로그의 확인메시지 다이어로그
        fun showAlertConfirmDialog(context: Context, title: String, message: String, positiveButtonText: String, positiveButtonClickListener: () -> Unit) {
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_save, null)
            val builder = AlertDialog.Builder(context)
            builder.setView(view)

            val dialog = builder.create()
            dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_background)

            val titleTextView = view.findViewById<TextView>(R.id.title)
            val messageTextView = view.findViewById<TextView>(R.id.message)
            val positiveButton = view.findViewById<Button>(R.id.btn_save)

            titleTextView.text = title
            messageTextView.text = message
            positiveButton.text = positiveButtonText

            positiveButton.setOnClickListener {
                positiveButtonClickListener()
                dialog.dismiss()
            }

            dialog.setCancelable(false)
            dialog.show()
        }
    }
}