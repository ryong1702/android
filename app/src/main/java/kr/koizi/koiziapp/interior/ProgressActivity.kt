package kr.koizi.koiziapp.interior

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.adapter.ProgressAdapter
import kr.koizi.koiziapp.databinding.ActivityProgressBinding
import kr.koizi.koiziapp.retrofit.RetrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProgressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarProgress.tvTitle.text = getString(R.string.interior_progress)

        val flg = intent.getIntExtra("flg", 0)
        val progressId = intent.getStringExtra("progressId").toString()
        val previousScreen = intent.getStringExtra("previousScreen") ?: ""

        binding.toolbarProgress.btnBack.setOnClickListener{
            when (previousScreen) {
            "MainActivity" -> { IntentBasedActivitySwitcher(this@ProgressActivity).goToMainActivity() }
            "ProgressHistoryActivity" -> { IntentBasedActivitySwitcher(this@ProgressActivity).goToProgressHistory() }
            "AlarmActivity" -> { IntentBasedActivitySwitcher(this@ProgressActivity).goToAlarmActivity() }
                else -> { onBackPressedDispatcher.onBackPressed() }
            }
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        // 프로그레스바 초기화
        binding.progressBar.max = 100
        // 조건에 따라 프로그레스바 값을 설정
        when (flg){
            // 조건 1
            1 -> {
                binding.progressBar.progress = 25
                loadImage(R.drawable.pg_progress_25)
                binding.ivSmallProgress.setImageResource(R.drawable.progress_25)
                binding.tvPercent.text = getString(R.string.title_interior_25)
                binding.tvText.text = getString(R.string.text_interior_25)
//                binding.tvPercent.setTextColor(getColor(R.color.warning))
            }
            // 조건 2
            2 -> {
                binding.progressBar.progress = 50
                loadImage(R.drawable.pg_progress_50)
                binding.ivSmallProgress.setImageResource(R.drawable.progress_50)
                binding.tvPercent.text = getString(R.string.title_interior_50)
                binding.tvText.text = getString(R.string.text_interior_50)
//                binding.tvPercent.setTextColor(getColor(R.color.main_color))
            }

            // 조건 3
            3 -> {
                binding.progressBar.progress = 75
                binding.ivSmallProgress.setImageResource(R.drawable.progress_75)
                loadImage(R.drawable.pg_progress_75)
                binding.tvPercent.text = getString(R.string.title_interior_75)
                binding.tvText.text = getString(R.string.text_interior_75)
            }

            4 -> {
                binding.progressBar.progress = 100
                binding.ivSmallProgress.setImageResource(R.drawable.progress_100)
                loadImage(R.drawable.pg_progress_100)
                binding.tvPercent.text = getString(R.string.title_interior_100)
                binding.tvText.text = getString(R.string.text_interior_100)
                binding.tvPercent.setTextColor(getColor(R.color.completed))
            }
            // 그 외 0의 경우는 필요없지만 일단 놔둠.
            else -> {
                binding.progressBar.progress = 0
                binding.ivProgress.setImageResource(R.drawable.pg_progress_25)
                binding.ivSmallProgress.setImageResource(R.drawable.progress_25)
                binding.tvPercent.text = getString(R.string.title_interior_0)
                binding.tvText.text = getString(R.string.text_interior_0)

            }
        }
        lifecycleScope.launch(Dispatchers.IO) {

            try {
                withContext(Dispatchers.Main) {
                    val progressResponse = RetrofitService.retrofitService.getInteriorProgressDetail(progressId)
                    val phone = progressResponse.body()?.managerPhone.toString()
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                    binding.rcImages.layoutManager = LinearLayoutManager(this@ProgressActivity, LinearLayoutManager.VERTICAL, false)
                    binding.rcImages.setHasFixedSize(true)
                    binding.rcImages.adapter = progressResponse.body()?.imgs.orEmpty().let { ProgressAdapter(it) }
                    binding.rcImages.visibility = VISIBLE
                    binding.btnTell.setOnClickListener {
                        if (intent.resolveActivity(packageManager) != null) {
                            // 전화 어플이 설치되어 있는 경우
                            startActivity(intent)
                        } else {
                            // 전화 어플이 설치되어 있지 않은 경우
                            runOnUiThread {
                                Toast.makeText(
                                    this@ProgressActivity,
                                    "전화 어플이 설치되어 있지 않습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("에러가 발생 : ", e.toString())
                ExceptionAlertDialog.handleException(this@ProgressActivity)
            }
        }
    }

    private fun loadImage(imageName: Int) {
        Glide.with(binding.root.context)
            .load(imageName)
            .fitCenter()
            .into(binding.ivProgress)
    }
}