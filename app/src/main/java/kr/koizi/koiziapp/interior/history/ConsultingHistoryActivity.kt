package kr.koizi.koiziapp.interior.history

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.adapter.HistoryAdapter
import kr.koizi.koiziapp.databinding.ActivityConsultingHistoryBinding
import kr.koizi.koiziapp.main.MainActivity.Companion.userId
import kr.koizi.koiziapp.retrofit.RetrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConsultingHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConsultingHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsultingHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarConsultingHistory.tvTitle.text = getString(R.string.title_ask_interior)
        binding.toolbarConsultingHistory.btnBack.setOnClickListener {
            IntentBasedActivitySwitcher(this@ConsultingHistoryActivity).goToAskActivity()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        val skeletonView = binding.toolbarSkeleton.shimmerViewContainer
        skeletonView.startShimmer()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    val historyResponse =
                        RetrofitService.retrofitService.getConsultingHistory(userId)
                    val history = historyResponse.body()

                    skeletonView.stopShimmer()
                    skeletonView.visibility = GONE
                    if (!history?.consultList.isNullOrEmpty()) { // consultList가 비어있는지 확인
                        // 상담 이력이 있을 경우 어댑터를 보여줍니다.
                        binding.recyclerHistory.layoutManager = LinearLayoutManager(
                            this@ConsultingHistoryActivity, LinearLayoutManager.VERTICAL, false)
                        binding.recyclerHistory.adapter = HistoryAdapter(history?.consultList!!)
                        binding.tvEmpty.visibility = GONE
                    } else {
                        // 상담 이력이 없을 경우 텍스트뷰를 보여줍니다.
                        binding.tvEmpty.visibility = VISIBLE
                        binding.recyclerHistory.visibility = GONE
                    }
                }
            } catch (e: Exception) {
                Log.d("에러가 발생 : ", e.toString())
                ExceptionAlertDialog.handleException(this@ConsultingHistoryActivity)
            }
        }
    }
}