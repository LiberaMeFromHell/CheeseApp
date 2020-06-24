package ru.lm.cheeseapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.lm.cheeseapp.model.network.BannerProvider
import ru.lm.cheeseapp.model.network.OfferProvider
import ru.lm.cheeseapp.model.pojo.Banner
import ru.lm.cheeseapp.model.pojo.Offer
import ru.lm.cheeseapp.model.pojo.OfferRecyclerItem
import ru.lm.cheeseapp.module.InjectionApplication
import javax.inject.Inject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val bannerLiveData: MutableLiveData<List<Banner>> =
        MutableLiveData()

    val offerLiveData: MutableLiveData<List<OfferRecyclerItem>> =
        MutableLiveData()

    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var bannerProvider: BannerProvider

    @Inject
    lateinit var offerProvider: OfferProvider

    init {
        bannerProvider = InjectionApplication.appComponent.injectBannerProvider()
        offerProvider = InjectionApplication.appComponent.injectOfferProvider()
    }

    fun onObserveBannerList() {
        compositeDisposable.add(
            bannerProvider.onObserveBannerLiveData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<Banner>>() {
                    override fun onNext(t: List<Banner>) {
                        bannerLiveData.postValue(t)
                    }
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        Log.d("RetrofitBannerProvider", "onObserveBannerLiveData $e")
                    }
                })
        )
    }

    fun onObserveOfferList() {
        //offerProvider.onObserveOfferLiveData()
        compositeDisposable.add(
            offerProvider.onObserveOfferRecyclerList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<OfferRecyclerItem>>() {
                    override fun onNext(t: List<OfferRecyclerItem>) {
                        offerLiveData.postValue(t)
                    }

                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        Log.d("RetrofitOfferProvider", "onObserveOfferLiveData $e")
                    }
                })
        )
    }

    //fun getBannerList() = bannerLiveData.getBannerLiveData()

/*    fun getOfferList(): MutableLiveData<List<OfferRecyclerItem>> {
        offerProvider
        return offerProvider.getOfferLiveData()
    }*/

    override fun onCleared() {
        compositeDisposable.dispose()
        /*bannerProvider.dispose()
        offerProvider.dispose()*/
        super.onCleared()
    }
}
