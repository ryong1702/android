package kr.koizi.koiziapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.data.response.InteriorHistoryList
import kr.koizi.koiziapp.databinding.RecyclerInteriorHistoryBinding
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher

class ProgressHistoryAdapter(private val historyList: List<InteriorHistoryList>) : RecyclerView.Adapter<ProgressHistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = RecyclerInteriorHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    class HistoryViewHolder(private val binding: RecyclerInteriorHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: InteriorHistoryList) {
            binding.tvDate.text = history.title

            for (i in 0 until history.progress.size) {
                when (i) {
                    0 -> binding.btn25.isEnabled = history.progress[i].id!!.isNotEmpty()
                    1 -> binding.btn50.isEnabled = history.progress[i].id!!.isNotEmpty()
                    2 -> binding.btn75.isEnabled = history.progress[i].id!!.isNotEmpty()
                    3 -> binding.btn100.isEnabled = history.progress[i].id!!.isNotEmpty()
                }
            }

            binding.btn25.setOnClickListener {showProgressDetail(1, history.progress[0].id.toString())}
            binding.btn50.setOnClickListener {showProgressDetail(2, history.progress[1].id.toString())}
            binding.btn75.setOnClickListener { showProgressDetail(3, history.progress[2].id.toString())}
            binding.btn100.setOnClickListener {showProgressDetail(4, history.progress[3].id.toString())}
        }

        private fun showProgressDetail(flg: Int, id: String) {
            IntentBasedActivitySwitcher(binding.root.context).goToProgressActivity(flg, id, "ProgressHistoryActivity")
            (binding.root.context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}