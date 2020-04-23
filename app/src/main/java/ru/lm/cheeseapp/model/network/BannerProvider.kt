package ru.lm.cheeseapp.model.network

import androidx.lifecycle.MutableLiveData
import ru.lm.cheeseapp.model.pojo.Banner

interface BannerProvider {
    fun onObserveBannerLiveData()
    fun getBannerLiveData(): MutableLiveData<List<Banner>>
    fun dispose()
}
