package kr.koizi.koiziapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.data.response.SampleList
import kr.koizi.koiziapp.databinding.RecyclerSampleHistoryBinding
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher

class SampleHistoryAdapter(private val sampleList: List<SampleList>) : RecyclerView.Adapter<SampleHistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = RecyclerSampleHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(sampleList[position])
    }

    override fun getItemCount(): Int {
        return sampleList.size
    }

    class HistoryViewHolder(private val binding: RecyclerSampleHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sample: SampleList) {

            binding.btnHistory.setOnClickListener {
                IntentBasedActivitySwitcher(binding.root.context).goToTestHistoryActivityDetail(sample.answer)
                (binding.root.context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            binding.tvTitle.text = sample.title
            binding.tvDate.text = sample.createdAt
        }
    }
}