package kr.koizi.koiziapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.koizi.koiziapp.constants.ApiConstants.PHOTO_URL
import kr.koizi.koiziapp.databinding.RecyclerImagesBinding
import com.bumptech.glide.Glide

class ProgressAdapter(private val image: List<String?>) : RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgressViewHolder {
        val binding = RecyclerImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProgressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProgressViewHolder, position: Int) {
        holder.bind(image[position])
    }

    override fun getItemCount(): Int {
        return image.size
    }

    class ProgressViewHolder(private val binding: RecyclerImagesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: String?) {
            with(binding.ivImage) {
                Glide.with(context)
                    .load("$PHOTO_URL$imgUrl")
                    .into(this)
            }
        }
    }
}