package kr.koizi.koiziapp.myPage

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.adapter.NoticeAdapter
import kr.koizi.koiziapp.databinding.ActivityMyPageNoticeBinding
import kr.koizi.koiziapp.retrofit.RetrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoticeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageNoticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarNotice.tvTitle.text = getString(R.string.notice)

        binding.toolbarNotice.btnBack.setOnClickListener{
            IntentBasedActivitySwitcher(this@NoticeActivity).goToMyPageActivity()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        val skeletonView = binding.toolbarSkeleton.shimmerViewContainer
        skeletonView.startShimmer()

        lifecycleScope.launch(Dispatchers.IO) {

            try {
                withContext(Dispatchers.Main) {
                    val noticeResponse = RetrofitService.retrofitService.getNotice()
                    skeletonView.stopShimmer()
                    skeletonView.visibility = GONE
                    if (!noticeResponse.body()?.notice.isNullOrEmpty()) {
                        // 공지사항이 있을 경우 리사이클러뷰 표시
                        binding.rcvNotice.adapter = NoticeAdapter(noticeResponse.body()?.notice ?: emptyList())
                        binding.rcvNotice.layoutManager = LinearLayoutManager(this@NoticeActivity)
                        binding.tvEmpty.visibility = GONE
                        binding.rcvNotice.visibility = VISIBLE
                    } else {
                        // 공지사항이 없을 경우 텍스트뷰를 보여줍니다.
                        binding.tvEmpty.visibility = VISIBLE
                        binding.rcvNotice.visibility = GONE
                    }
                }
            } catch (e: Exception) {
                Log.d("에러가 발생 : ", e.toString())
                ExceptionAlertDialog.handleException(this@NoticeActivity)
            }
        }
    }
}