package kr.koizi.koiziapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.koizi.koiziapp.constants.ApiConstants.PHOTO_URL
import kr.koizi.koiziapp.data.response.Images
import kr.koizi.koiziapp.databinding.RecyclerMainImagesBinding
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher

class ImageAdapter(private val mainContext: Context, private val images: List<Images>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = RecyclerMainImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(mainContext, images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ImageViewHolder(private val binding: RecyclerMainImagesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mainContext: Context, item: Images) {
            binding.ivImage.setOnClickListener {
                IntentBasedActivitySwitcher(mainContext).goToExternalLink(item.t_url)
            }
            with(binding.ivImage) {
                Glide.with(context)
                    .load("$PHOTO_URL${item.img_id}")
                    .into(this)
            }
        }
    }
}