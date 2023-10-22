package kr.koizi.koiziapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.data.response.AlarmList
import kr.koizi.koiziapp.databinding.RecyclerAlarmBinding
import kr.koizi.koiziapp.retrofit.RetrofitService.retrofitService
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher
import kotlinx.coroutines.*

class AlarmAdapter (private val alarmList: List<AlarmList>) : RecyclerView.Adapter<AlarmAdapter.NewsHolder>() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmAdapter.NewsHolder {
        val binding = RecyclerAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmAdapter.NewsHolder, position: Int) {
        holder.bind(alarmList[position])
    }

    override fun getItemCount() = alarmList.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        coroutineScope.cancel()
    }

    inner class NewsHolder(private val binding: RecyclerAlarmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: AlarmList) {
            val title = alarm.title
            val alarmType = alarm.alarmType
            val keyId = alarm.keyId
            val confirmFlg = alarm.confirmFlg
            val createdAt = alarm.createdAt

            binding.tvTitle.text = title
            binding.tvDate.text = createdAt

            binding.btnAlarmDetail.setOnClickListener {
                coroutineScope.launch(Dispatchers.IO) {
                    retrofitService.getAlarmStatusUpdate(keyId)
                }
                when (alarmType) {
                    0 -> IntentBasedActivitySwitcher(binding.root.context).goToConsultingHistoryDetailActivity(keyId, "AlarmActivity")
                    else -> IntentBasedActivitySwitcher(binding.root.context).goToProgressActivity(alarmType, keyId, "AlarmActivity")
                }.also { (binding.root.context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left) }
            }
            if (confirmFlg == 1) {
                binding.ivConfirm.visibility = GONE
            }
        }
    }
}