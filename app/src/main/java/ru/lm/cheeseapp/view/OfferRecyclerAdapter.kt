package ru.lm.cheeseapp.view

import android.graphics.Paint
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
import ru.lm.cheeseapp.databinding.HolderOfferHeaderBinding
import ru.lm.cheeseapp.databinding.HolderOfferItemBinding
import ru.lm.cheeseapp.model.pojo.OfferRecyclerItem
import kotlin.math.roundToInt

class OfferRecyclerAdapter(private val offerList: List<OfferRecyclerItem>) :
    ListAdapter<OfferRecyclerItem, RecyclerView.ViewHolder>(OfferDiffUtil()) {

    override fun getItemViewType(position: Int) =
        if (offerList[position].isHeader) 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        return if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.holder_offer_item, parent, false)

            OfferViewHolder(HolderOfferItemBinding.bind(view))
        } else {
            view = layoutInflater.inflate(R.layout.holder_offer_header, parent, false)
            HeaderViewHolder(HolderOfferHeaderBinding.bind(view))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (offerList[position].isHeader) {
            holder as HeaderViewHolder
            holder.offerHeader.text = offerList[position].headerText
        } else {
            holder as OfferViewHolder
            Glide.with(holder.image).load(offerList[position].offer?.image)
                .into(holder.image)
            holder.title.text = offerList[position].offer?.name

            if (!offerList[position].offer?.desc.isNullOrEmpty())
                holder.desc.text = offerList[position].offer?.desc
            else
                holder.desc.visibility = View.INVISIBLE

            offerList[position].offer?.discount?.let {
                holder.discount.text = "${it.roundToInt() * 100}%"
                holder.price.text = "${offerList[position].offer?.price?.roundToInt()}₽"
                holder.price.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.finalPrice.text =
                    "${((offerList[position].offer?.price ?: 0f) * it).roundToInt()}₽"
            } ?: run {
                holder.discount.visibility = View.INVISIBLE
                holder.price.visibility = View.INVISIBLE
                holder.finalPrice.visibility = View.INVISIBLE
            }
        }
    }

    class OfferViewHolder(binding: HolderOfferItemBinding) : RecyclerView.ViewHolder(binding.root) {

        var desc: TextView
        var title: TextView
        var discount: TextView
        var finalPrice: TextView
        var price: TextView
        var image: ImageView

        init {
            binding.apply {
                desc = offerDesc
                title = offerTitle
                discount = offerDiscount
                finalPrice = offerFinalPrice
                price = offerPrice
                image = offerImageView
            }
        }
    }

    class HeaderViewHolder(binding: HolderOfferHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var offerHeader: TextView = binding.offerHeader
    }

    class OfferDiffUtil : DiffUtil.ItemCallback<OfferRecyclerItem>() {

        override fun areItemsTheSame(
            oldItem: OfferRecyclerItem,
            newItem: OfferRecyclerItem
        ): Boolean {
            if (oldItem.isHeader == newItem.isHeader)
                return true
            return (oldItem.offer?.id == newItem.offer?.id)
        }

        override fun areContentsTheSame(
            oldItem: OfferRecyclerItem,
            newItem: OfferRecyclerItem
        ): Boolean {
            if (oldItem.isHeader)
                return oldItem.headerText == newItem.headerText
            return (oldItem.offer == newItem.offer)
        }
    }
}
