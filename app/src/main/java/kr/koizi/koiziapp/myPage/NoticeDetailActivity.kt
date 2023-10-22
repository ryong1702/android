package kr.koizi.koiziapp.myPage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.databinding.ActivityNoticeDetailBinding
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher

// 공지사항 상세 화면
class NoticeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoticeDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNoticeDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbarNoticeConfirmation.btnBack.setOnClickListener {
            IntentBasedActivitySwitcher(this@NoticeDetailActivity).gotoNoticeActivity()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        binding.tvTitle.text = intent.getStringExtra("title")
        binding.tvContent.text = intent.getStringExtra("content")
        binding.tvDate.text = intent.getStringExtra("updateAt")
    }
}