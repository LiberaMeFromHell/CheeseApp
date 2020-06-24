package ru.lm.cheeseapp.model

import ru.lm.cheeseapp.model.pojo.Offer
import ru.lm.cheeseapp.model.pojo.OfferRecyclerItem

fun createRecyclerOfferList(offers: List<Offer>) : List<OfferRecyclerItem> {

    val recyclerItems = ArrayList<OfferRecyclerItem>()
    offers.sortedWith(compareBy { it.groupName })
        .forEach { recyclerItems.add(OfferRecyclerItem(it)) }
    recyclerItems.add(0, OfferRecyclerItem(null, true, recyclerItems[0].offer?.groupName))
    var i = 1
    while (i < recyclerItems.size-1) {
        if (recyclerItems[i].offer?.groupName != recyclerItems[i + 1].offer?.groupName) {
            recyclerItems.add(i+1,
                OfferRecyclerItem(null, true, recyclerItems[i+1].offer?.groupName)
            )
            i+=2
        } else {
            i++
        }
    }
    return recyclerItems
}
