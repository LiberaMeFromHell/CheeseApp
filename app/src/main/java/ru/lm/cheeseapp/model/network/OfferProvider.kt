package ru.lm.cheeseapp.model.network

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import ru.lm.cheeseapp.model.pojo.OfferRecyclerItem

interface OfferProvider {
    fun onObserveOfferRecyclerList(): Observable<List<OfferRecyclerItem>>
    //fun getOfferList(): List<OfferRecyclerItem>
    //fun dispose()
}
