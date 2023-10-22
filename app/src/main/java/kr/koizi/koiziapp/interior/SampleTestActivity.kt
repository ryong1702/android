package kr.koizi.koiziapp.interior

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivitySampleTestBinding
import kr.koizi.koiziapp.main.MainActivity.Companion.nickName
import kr.koizi.koiziapp.main.MainActivity.Companion.userId
import kr.koizi.koiziapp.retrofit.RetrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SampleTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySampleTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textTitle.text = getString(R.string.sample_test, nickName)

        binding.toolbarSampleTest.btnBack.setOnClickListener{
            IntentBasedActivitySwitcher(this@SampleTestActivity).goToMainActivity()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        binding.btnTest.setOnClickListener{
            IntentBasedActivitySwitcher(this@SampleTestActivity).goToSelectGenderActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.btnTestHistory.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {

                try {
                    withContext(Dispatchers.Main) {
                        val response = RetrofitService.retrofitService.getTestHistory(userId)
                        if(response.body()?.result == "OK") {
                            //메인 화면으로 이동
                            IntentBasedActivitySwitcher(this@SampleTestActivity).goToTestHistoryActivity()
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        } else {
                            runOnUiThread {
                                Toast.makeText(
                                    this@SampleTestActivity,
                                    "회원가입 실패",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d("에러가 발생 : ", e.toString())
                    ExceptionAlertDialog.handleException(this@SampleTestActivity)
                }
            }
        }
    }
}