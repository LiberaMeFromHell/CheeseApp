package ru.lm.cheeseapp.model.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.lm.cheeseapp.model.createRecyclerOfferList
import ru.lm.cheeseapp.model.network.api.CheeseAPI
import ru.lm.cheeseapp.model.pojo.Offer
import ru.lm.cheeseapp.model.pojo.OfferRecyclerItem
import ru.lm.cheeseapp.model.pojo.OfferType
import ru.lm.cheeseapp.module.InjectionApplication
import javax.inject.Inject

class RetrofitOfferProvider : OfferProvider {

    private val disposable = CompositeDisposable()

    private val liveData: MutableLiveData<List<OfferRecyclerItem>> =
        MutableLiveData()

    @Inject
    lateinit var cheeseAPI: CheeseAPI

    init {
        cheeseAPI = InjectionApplication.appComponent.injectNetworkAdapter()
    }

    override fun onObserveOfferLiveData() {
        disposable.add(
        cheeseAPI.getOfferResponse()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                createRecyclerOfferList(it)
            }
            .subscribeWith(object: DisposableObserver<List<OfferRecyclerItem>>() {
                override fun onNext(t: List<OfferRecyclerItem>) {
                    liveData.postValue(t)
                }
                override fun onComplete() { }
                override fun onError(e: Throwable) {
                    Log.d("RetrofitOfferProvider", "onObserveOfferLiveData $e")
                }

            }))
    }

    override fun getOfferLiveData(): MutableLiveData<List<OfferRecyclerItem>> = liveData

    override fun dispose() {
        disposable.dispose()
    }
}
