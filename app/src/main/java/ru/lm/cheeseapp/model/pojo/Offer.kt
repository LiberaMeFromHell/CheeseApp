package ru.lm.cheeseapp.model.pojo

data class Offer (
    val id: String,
    val name: String,
    val desc: String?,
    val groupName: String,
    val type: OfferType,
    val image: String,
    val price: Float?,
    val discount: Float?
)
