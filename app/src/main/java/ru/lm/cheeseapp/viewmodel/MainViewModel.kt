package ru.lm.cheeseapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.lm.cheeseapp.model.network.BannerProvider
import ru.lm.cheeseapp.model.network.OfferProvider
import ru.lm.cheeseapp.model.pojo.Offer
import ru.lm.cheeseapp.model.pojo.OfferRecyclerItem
import ru.lm.cheeseapp.module.InjectionApplication
import javax.inject.Inject

class MainViewModel(application: Application): AndroidViewModel(application) {

    @Inject
    lateinit var bannerProvider: BannerProvider

    @Inject
    lateinit var offerProvider: OfferProvider

    init {
        bannerProvider = InjectionApplication.appComponent.injectBannerProvider()
        offerProvider = InjectionApplication.appComponent.injectOfferProvider()
    }

    fun onObserveBannerList() {
        bannerProvider.onObserveBannerLiveData()
    }

    fun onObserveOfferList() {
        offerProvider.onObserveOfferLiveData()
    }

    fun getBannerList() = bannerProvider.getBannerLiveData()

    fun getOfferList(): MutableLiveData<List<OfferRecyclerItem>> {
        offerProvider
        return offerProvider.getOfferLiveData()
    }

    override fun onCleared() {
        bannerProvider.dispose()
        offerProvider.dispose()
        super.onCleared()
    }
}
