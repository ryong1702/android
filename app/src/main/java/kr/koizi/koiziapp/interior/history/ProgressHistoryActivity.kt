package kr.koizi.koiziapp.interior.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.adapter.ProgressHistoryAdapter
import kr.koizi.koiziapp.databinding.ActivityProgressHistoryBinding
import kr.koizi.koiziapp.main.MainActivity
import kr.koizi.koiziapp.retrofit.RetrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProgressHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProgressHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarProgressHistory.tvTitle.text = getString(R.string.text_interior_history)
        binding.toolbarProgressHistory.btnBack.setOnClickListener {
            IntentBasedActivitySwitcher(this@ProgressHistoryActivity).goToMyPageActivity()
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
                        RetrofitService.retrofitService.getInteriorProgressHistory(MainActivity.userId)
                    val history = historyResponse.body()

                    skeletonView.stopShimmer()
                    skeletonView.visibility = GONE
                    if (!history?.histories.isNullOrEmpty()) {
                        binding.recyclerHistory.layoutManager = LinearLayoutManager(this@ProgressHistoryActivity, LinearLayoutManager.VERTICAL, false)
                        binding.recyclerHistory.adapter = ProgressHistoryAdapter(history?.histories!!)
                        binding.recyclerHistory.visibility = VISIBLE
                    } else {
                        binding.tvEmpty.visibility = VISIBLE
                        binding.recyclerHistory.visibility = GONE
                    }
                }
            } catch (e: Exception) {
                Log.d("에러가 발생 : ", e.toString())
                ExceptionAlertDialog.handleException(this@ProgressHistoryActivity)
            }
        }
    }
}