package ru.lm.cheeseapp.model.pojo

import com.google.gson.annotations.SerializedName

enum class OfferType {
    @SerializedName("product")
    Product,
    @SerializedName("item")
    Item
}
