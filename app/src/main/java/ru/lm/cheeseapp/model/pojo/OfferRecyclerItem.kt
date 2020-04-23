package ru.lm.cheeseapp.model.pojo

data class OfferRecyclerItem(
    val offer: Offer?,
    var isHeader: Boolean = false,
    var headerText: String? = null
)
