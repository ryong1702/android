package kr.koizi.koiziapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.koizi.koiziapp.R
import kr.koizi.koiziapp.data.response.Notice
import kr.koizi.koiziapp.databinding.RecyclerNoticeBinding
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher

class NoticeAdapter(private val noticeList: List<Notice>) : RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val binding = RecyclerNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoticeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(noticeList[position])
    }

    override fun getItemCount(): Int = noticeList.size

    inner class NoticeViewHolder(private val binding: RecyclerNoticeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notice: Notice) {
            val title = notice.title
            val updateAt = notice.updatedAt.toString()
            val content = notice.content
            binding.btnNotice.setOnClickListener {
                IntentBasedActivitySwitcher(binding.root.context).goToNoticeDetailActivity(title, updateAt, content)
                (binding.root.context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
            binding.tvTitle.text = notice.title
            binding.tvDate.text = updateAt
        }
    }
}