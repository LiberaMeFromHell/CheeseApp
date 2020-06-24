package ru.lm.cheeseapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.lm.cheeseapp.R
import ru.lm.cheeseapp.databinding.HolderBannerItemBinding
import ru.lm.cheeseapp.model.pojo.Banner

class BannerRecyclerAdapter(private val bannerList: List<Banner>) :
    ListAdapter<Banner, BannerRecyclerAdapter.BannerViewHolder>(BannerDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BannerViewHolder(HolderBannerItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {

        Glide.with(holder.imageV.context)
            .load(bannerList[position].image)
            .centerCrop()
            .into(holder.imageV)

        if (!bannerList[position].title.isNullOrBlank())
            holder.title.text = bannerList[position].title
        else
            holder.title.visibility = View.GONE

        if (!bannerList[position].desc.isNullOrBlank())
            holder.sign.text = bannerList[position].desc
        else
            holder.sign.visibility = View.GONE
    }

    class BannerViewHolder(binding: HolderBannerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        var title: TextView
        var sign: TextView
        var imageV: ImageView

        init {
            binding.apply {
                title = bannerTitle
                sign = bannerSign
                imageV = bannerImage
            }
        }
    }

    class BannerDiffUtil : DiffUtil.ItemCallback<Banner>() {
        override fun areItemsTheSame(oldItem: Banner, newItem: Banner) = (oldItem.id == newItem.id)

        override fun areContentsTheSame(oldItem: Banner, newItem: Banner) = (oldItem == newItem)
    }
}
