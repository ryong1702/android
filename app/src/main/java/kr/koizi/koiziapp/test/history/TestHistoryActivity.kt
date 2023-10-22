package kr.koizi.koiziapp.test.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.adapter.SampleHistoryAdapter
import kr.koizi.koiziapp.databinding.ActivitySampleHistoryBinding
import kr.koizi.koiziapp.main.MainActivity.Companion.userId
import kr.koizi.koiziapp.retrofit.RetrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySampleHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySampleHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarQuickHistory.tvTitle.text = getString(R.string.title_quick_test)
        binding.toolbarQuickHistory.btnBack.setOnClickListener {
            IntentBasedActivitySwitcher(this@TestHistoryActivity).goToSampleTestActivity()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        lifecycleScope.launch(Dispatchers.IO) {

            try {
                withContext(Dispatchers.Main) {
                    val historyResponse =
                        RetrofitService.retrofitService.getTestHistory(userId)
                    val history = historyResponse.body()

                    if (!history?.sampleTestList.isNullOrEmpty()) {
                        // 상담 이력이 있을 경우 어댑터를 보여줍니다.
                        binding.recyclerTestHistory.visibility = VISIBLE
                        binding.tvEmpty.visibility = GONE

                        binding.recyclerTestHistory.layoutManager = LinearLayoutManager(this@TestHistoryActivity, LinearLayoutManager.VERTICAL, false)
                        binding.recyclerTestHistory.adapter = history!!.sampleTestList?.let { SampleHistoryAdapter(it) }
                        binding.recyclerTestHistory.visibility = VISIBLE
                    } else {
                        // 상담 이력이 없을 경우 텍스트뷰를 보여줍니다.
                        binding.recyclerTestHistory.visibility = GONE
                        binding.tvEmpty.visibility = VISIBLE
                    }
                }
            } catch (e: Exception) {
                Log.d("에러가 발생 : ", e.toString())
                ExceptionAlertDialog.handleException(this@TestHistoryActivity)
            }
        }
    }
}