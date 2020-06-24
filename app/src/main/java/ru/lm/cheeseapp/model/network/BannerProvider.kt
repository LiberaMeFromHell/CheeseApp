package ru.lm.cheeseapp.model.network

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import ru.lm.cheeseapp.model.pojo.Banner

interface BannerProvider {
    fun onObserveBannerLiveData(): Observable<List<Banner>>
    //fun getBannerLiveData(): MutableLiveData<List<Banner>>
    //fun dispose()
}
