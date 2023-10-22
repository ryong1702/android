package kr.koizi.koiziapp.utils

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    // ...
    private var alertDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로가기 버튼이 눌렸을 때의 동작
                Toast.makeText(this@BaseActivity, "종료합니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onPause() {
        super.onPause()
        alertDialog?.dismiss()
        ExceptionAlertDialog.dismiss()
    }
    // ...
}