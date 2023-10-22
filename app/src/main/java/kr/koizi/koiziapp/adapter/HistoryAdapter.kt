package kr.koizi.koiziapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.data.HistoryList
import kr.koizi.koiziapp.databinding.RecyclerHistoryBinding
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher

class HistoryAdapter(private val historyList: List<HistoryList>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = RecyclerHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    class HistoryViewHolder(private val binding: RecyclerHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryList) {
            binding.tvTitle.text = history.title
            binding.tvDate.text = history.createdAt.toString()
            binding.btnHistoryDetail.setOnClickListener {
                IntentBasedActivitySwitcher(binding.root.context).goToConsultingHistoryDetailActivity(history.titleId, "ConsultingHistoryActivity")
                (binding.root.context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }
}