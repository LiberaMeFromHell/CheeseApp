package ru.lm.cheeseapp.view

import android.graphics.Paint
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
import ru.lm.cheeseapp.model.pojo.OfferRecyclerItem
import kotlin.math.roundToInt

class OfferRecyclerAdapter(val offerList: List<OfferRecyclerItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int) =
        if (offerList[position].isHeader) 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        return if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.holder_offer_item, parent, false)
            OfferViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.holder_offer_header, parent, false)
            OfferViewHolder.HeaderViewHolder(view)
        }
    }

    override fun getItemCount() = offerList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (offerList[position].isHeader) {
            holder as OfferViewHolder.HeaderViewHolder
            holder.offerHeader.text = offerList[position].headerText
        } else {
            holder as OfferViewHolder
            Glide.with(holder.offerImageView).load(offerList[position].offer?.image)
                .into(holder.offerImageView)
            holder.titleOffer.text = offerList[position].offer?.name

            if (!offerList[position].offer?.desc.isNullOrEmpty())
                holder.descOffer.text = offerList[position].offer?.desc
            else
                holder.descOffer.visibility = View.INVISIBLE

            offerList[position].offer?.discount?.let {
                holder.discountOffer.text = "${it.roundToInt() * 100}%"
                holder.priceOffer.text = "${offerList[position].offer?.price?.roundToInt()}₽"
                holder.priceOffer.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.finalPriceOffer.text =
                    "${((offerList[position].offer?.price ?: 0f) * it).roundToInt()}₽"
            } ?: run {
                holder.discountOffer.visibility = View.INVISIBLE
                holder.priceOffer.visibility = View.INVISIBLE
                holder.finalPriceOffer.visibility = View.INVISIBLE
            }
        }
    }

    class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.descOffer)
        lateinit var descOffer: TextView
        @BindView(R.id.titleOffer)
        lateinit var titleOffer: TextView
        @BindView(R.id.discountOffer)
        lateinit var discountOffer: TextView
        @BindView(R.id.finalPriceOffer)
        lateinit var finalPriceOffer: TextView
        @BindView(R.id.priceOffer)
        lateinit var priceOffer: TextView
        @BindView(R.id.offerImageView)
        lateinit var offerImageView: ImageView

        init {
            ButterKnife.bind(this, itemView)
        }

        class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            @BindView(R.id.offerHeader)
            lateinit var offerHeader: TextView

            init {
                ButterKnife.bind(this, itemView)
            }
        }
    }
}
