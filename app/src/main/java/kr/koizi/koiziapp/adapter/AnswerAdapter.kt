package kr.koizi.koiziapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kr.koizi.koiziapp.constants.ApiConstants.PHOTO_URL
import kr.koizi.koiziapp.databinding.RecyclerAnswerBinding
import com.bumptech.glide.Glide
import kr.koizi.koiziapp.constants.ApiConstants
import kr.koizi.koiziapp.utils.IntentBasedActivitySwitcher

class AnswerAdapter(private val context: Context, private val itemList: List<String>, private val guides: List<List<String>>?) :
    RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val binding = RecyclerAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnswerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(itemList[position])
        holder.bindGuides(context, guides?.get(position) ?: emptyList())

    }

    override fun getItemCount() = itemList.size

    class AnswerViewHolder(private val binding:  RecyclerAnswerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tv1.text = item
        }

        fun bindGuides(context: Context, guides: List<String>) {
            binding.cvLine.visibility = if (guides.isEmpty()) GONE else VISIBLE
            binding.tvLink.setOnClickListener {
                IntentBasedActivitySwitcher(context).goToExternalLink(ApiConstants.BUY_URL)
            }
            if (guides.isNotEmpty()) {
                if (guides[0].startsWith("e")) {
                    binding.ivImage.visibility = VISIBLE
                    binding.tvLink.visibility = VISIBLE
                    loadImage("$PHOTO_URL${guides[0]}", binding.ivImage)
                    binding.arrowButton.animate().rotation(180f).start()
                    hideTextViews()
                }
            }

            setupButtonClickEvent(guides)
        }

        private fun hideTextViews() {
            binding.tv2.visibility = GONE
            binding.tv3.visibility = GONE
            binding.tv4.visibility = GONE
        }

        private fun showTextViews() {
            binding.tv2.visibility = VISIBLE
            binding.tv3.visibility = VISIBLE
            binding.tv4.visibility = VISIBLE
        }

        private fun setTextViewsText(guides: List<String>) {
            val numberedGuides = guides.mapIndexed { index, guide ->
                "${index + 1}. $guide"
            }
            binding.tv2.text = numberedGuides.getOrNull(0) ?: ""
            binding.tv3.text = numberedGuides.getOrNull(1) ?: ""
            binding.tv4.text = numberedGuides.getOrNull(2) ?: ""
            binding.tv2.visibility = if (binding.tv2.text.isEmpty()) GONE else VISIBLE
            binding.tv3.visibility = if (binding.tv3.text.isEmpty()) GONE else VISIBLE
            binding.tv4.visibility = if (binding.tv4.text.isEmpty()) GONE else VISIBLE
        }

        private fun loadImage(url: String, imageView: ImageView) {
            Glide.with(binding.root.context)
                .load(url)
                .fitCenter()
                .into(imageView)
        }

        private fun setupButtonClickEvent(guides: List<String>) {
            binding.btnClick.setOnClickListener {
                val textViewVisible = anyTextViewVisible()
                val imageViewVisible = binding.ivImage.visibility == VISIBLE

                if (textViewVisible || imageViewVisible) {
                    hideTextViews()
                    binding.ivImage.visibility = GONE
                    binding.tvLink.visibility = GONE
                    binding.arrowButton.animate().rotation(0f).start()
                } else {
                    if (guides[0].startsWith("e")) {
                        binding.tv2.visibility = GONE
                        binding.tv3.visibility = GONE
                        binding.tv4.visibility = GONE
                        binding.ivImage.visibility = VISIBLE
                        binding.tvLink.visibility = VISIBLE
                        loadImage("$PHOTO_URL${guides[0]}", binding.ivImage)
                        binding.arrowButton.animate().rotation(180f).start()
                    } else {
                        showTextViews()
                        setTextViewsText(guides)
                        binding.ivImage.visibility = GONE
                        binding.tvLink.visibility = GONE
                        binding.arrowButton.animate().rotation(180f).start()
                    }
                }
            }
        }

        private fun anyTextViewVisible(): Boolean {
            return binding.tv2.visibility == VISIBLE ||
                    binding.tv3.visibility == VISIBLE ||
                    binding.tv4.visibility == VISIBLE
        }

    }
}