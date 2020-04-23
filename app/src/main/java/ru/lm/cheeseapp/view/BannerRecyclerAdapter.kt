package ru.lm.cheeseapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import ru.lm.cheeseapp.R
import ru.lm.cheeseapp.model.pojo.Banner

class BannerRecyclerAdapter(private val bannerList: List<Banner>) :
    RecyclerView.Adapter<BannerRecyclerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.holder_banner_item, parent, false)
        return BannerViewHolder(view)
    }

    override fun getItemCount(): Int = bannerList.size

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {

        Glide.with(holder.image)
            .load(bannerList[position].image)
            .centerCrop()
            .into(holder.image)

        if (!bannerList[position].title.isNullOrBlank())
            holder.title.text = bannerList[position].title
        else
            holder.title.visibility = View.GONE

        if (!bannerList[position].desc.isNullOrBlank())
            holder.sign.text = bannerList[position].desc
        else
            holder.sign.visibility = View.GONE
    }

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.bannerTitle)
        lateinit var title: TextView
        @BindView(R.id.bannerSign)
        lateinit var sign: TextView
        @BindView(R.id.bannerImage)
        lateinit var image: ImageView

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}
