package kr.koizi.koiziapp.alarm

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.adapter.AlarmAdapter
import kr.koizi.koiziapp.databinding.ActivityAlarmBinding
import kr.koizi.koiziapp.retrofit.RetrofitService
import kr.koizi.koiziapp.utils.BackPressHandler
import kr.koizi.koiziapp.utils.ExceptionAlertDialog
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kr.koizi.koiziapp.utils.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAlarm.tvTitle.text = getString(R.string.title_alarm)
        binding.toolbarAlarm.btnBack.setOnClickListener {
            IntentBasedActivitySwitcher(this@AlarmActivity).goToMainActivity()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val backPressHandler = BackPressHandler(this)
        backPressHandler.addBackPressedCallback()

        val preferencesManager = PreferencesManager(this)
        val userId = preferencesManager.getUserInfo().userId.toString()
        val skeletonView = binding.toolbarSkeleton.shimmerViewContainer
        skeletonView.startShimmer()
        lifecycleScope.launch(Dispatchers.IO) {

            try {
                withContext(Dispatchers.Main) {
                    val alarmResponse = RetrofitService.retrofitService.getAlarmStatus(userId)

                    skeletonView.stopShimmer()
                    skeletonView.visibility = GONE
                    if (!alarmResponse.body()?.alarmList.isNullOrEmpty()) {
                        binding.rcvAlarm.adapter = AlarmAdapter(alarmResponse.body()?.alarmList ?: emptyList())
                        binding.rcvAlarm.layoutManager = LinearLayoutManager(this@AlarmActivity)
                        binding.tvEmpty.visibility = GONE
                        binding.rcvAlarm.visibility = VISIBLE
                    } else {
                        binding.tvEmpty.visibility = VISIBLE
                        binding.rcvAlarm.visibility = GONE
                    }
                }
            } catch (e: Exception) {
                Log.d("에러가 발생 : ", e.toString())
                ExceptionAlertDialog.handleException(this@AlarmActivity)
            }
        }
    }
}