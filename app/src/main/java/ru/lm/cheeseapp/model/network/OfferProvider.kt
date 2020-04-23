package ru.lm.cheeseapp.model.network

import androidx.lifecycle.MutableLiveData
import ru.lm.cheeseapp.model.pojo.OfferRecyclerItem

interface OfferProvider {
    fun onObserveOfferLiveData()
    fun getOfferLiveData(): MutableLiveData<List<OfferRecyclerItem>>
    fun dispose()
}
