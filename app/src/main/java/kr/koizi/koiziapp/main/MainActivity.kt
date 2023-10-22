package kr.koizi.koiziapp.main

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.adapter.ImageAdapter
import kr.koizi.koiziapp.constants.ApiConstants.COMMUNITY_URL
import kr.koizi.koiziapp.databinding.ActivityMainBinding
import kr.koizi.koiziapp.retrofit.RetrofitService
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kr.koizi.koiziapp.utils.PreferencesManager
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var lastClickedTime = 0L
    var lastBackPressedTime = 0L
    companion object {
        const val MIN_CLICK_INTERVAL = 1000L
        lateinit var userId: String
        lateinit var nickName: String
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferencesManager = PreferencesManager(this)
        userId = preferencesManager.getUserInfo().userId.toString()
        nickName = preferencesManager.getUserInfo().nickName.toString()

        binding.btnProfile.setOnClickListener {
            val currentClickedTime = System.currentTimeMillis()
            if (currentClickedTime - lastClickedTime >= MIN_CLICK_INTERVAL) {
                // 중복 클릭이 아닌 경우 처리할 내용 작성
                lastClickedTime = currentClickedTime
                IntentBasedActivitySwitcher(this@MainActivity).goToMyPageActivity()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
        // 스켈레톤 뷰의 반짝임 효과를 시작합니다.
        val shimmerContainer = binding.shimmerViewContainer
        val shimmerContainer1 = binding.toolbarSkeleton.shimmerViewContainer

        shimmerContainer.startShimmer()
        shimmerContainer1.startShimmer()
        lifecycleScope.launch(Dispatchers.IO) {

            try {
                withContext(Dispatchers.Main) {
                    val alarmResponse = RetrofitService.retrofitService.getPersonAlarm(userId)
                    val interiorProgressResponse = RetrofitService.retrofitService.getInteriorProgress(userId)
                    val tipResponse = RetrofitService.retrofitService.getTip()

                    val alarm = alarmResponse.body()
                    val progress = interiorProgressResponse.body()
                    val tip = tipResponse.body()
                    val title = progress?.progress?.title.toString()
                    val text = progress?.progress?.text.toString()
                    val color = progress?.progress?.color.toString()
                    val flg = progress?.progress?.flg ?: 0
                    val progressId = progress?.progress?.progressId
                    if(alarm?.result.toString() == "OK" && progress?.result.toString() == "OK" && tip?.result.toString() == "OK") {
                        if (alarm?.alarmFlg == true) {
                            binding.btnAlarm.setImageResource(R.drawable.menu_alarm_on)
                        } else {
                            binding.btnAlarm.setImageResource(R.drawable.menu_alarm_off)
                        }
                        //메인 화면으로 이동
                        if (progress?.result == "OK") {
                            shimmerContainer.stopShimmer()
                            shimmerContainer.visibility = GONE
                            if (flg != 0) {
                                //진행상황 화면으로 이동
                                binding.layoutProgress.setOnClickListener {
                                    IntentBasedActivitySwitcher(this@MainActivity).goToProgressActivity(flg, progressId, "MainActivity")
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                }
                            }
                            binding.tvMessageProgress.text = title
                            binding.tvMessageProgress.setTextColor(Color.parseColor(color))
                            binding.tvMessage.text = text
                        }

                        shimmerContainer1.stopShimmer()
                        shimmerContainer1.visibility = GONE
                        binding.rcTip.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                        binding.rcTip.adapter = tipResponse.body()?.images.orEmpty().let { ImageAdapter(this@MainActivity, it) }
                        binding.rcTip.visibility = VISIBLE
                    } else {
                        throw Exception("서버 접속에 실패하였습니다.")
                    }
                }
            } catch (e: Exception) {
                Log.d("에러가 발생 : ", e.toString())
                ExceptionAlertDialog.handleException(this@MainActivity)
            }
        }

        //인테리어 문의하기 화면으로 이동
        binding.btnAskInterior.setOnClickListener {
            IntentBasedActivitySwitcher(this@MainActivity).goToAskActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        //간편검사 화면으로 이동
        binding.btnSampleTest.setOnClickListener {
            IntentBasedActivitySwitcher(this@MainActivity).goToSampleTestActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.btnAlarm.setOnClickListener {
            IntentBasedActivitySwitcher(this@MainActivity).goToAlarmActivity()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.btnCommunity.setOnClickListener {
            IntentBasedActivitySwitcher(this@MainActivity).goToExternalLink(COMMUNITY_URL)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        // 뒤로가기 버튼 콜백 등록
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //        // Get current time in milliseconds
                val currentTime = System.currentTimeMillis()

                // Check if back button was pressed within 2 seconds
                if (currentTime - lastBackPressedTime < 2000) {
                    // Exit the app
                    finishAffinity()
                } else {
                    // Show a toast message
                    Toast.makeText(applicationContext, "뒤로가기 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show()
                    // Update last back button press time
                    lastBackPressedTime = currentTime
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }
}