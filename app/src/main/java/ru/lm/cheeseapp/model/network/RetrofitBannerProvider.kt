package ru.lm.cheeseapp.model.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import ru.lm.cheeseapp.model.network.api.CheeseAPI
import ru.lm.cheeseapp.model.pojo.Banner
import ru.lm.cheeseapp.module.InjectionApplication
import javax.inject.Inject

class RetrofitBannerProvider : BannerProvider {

    private val disposable = CompositeDisposable()

    private val liveData: MutableLiveData<List<Banner>> =
        MutableLiveData()

    @Inject
    lateinit var cheeseAPI: CheeseAPI

    init {
        cheeseAPI = InjectionApplication.appComponent.injectNetworkAdapter()
    }

    override fun onObserveBannerLiveData() {
        disposable.add(
            cheeseAPI.getBannerResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<Banner>>() {
                    override fun onNext(t: List<Banner>) {
                        liveData.postValue(t)
                    }
                    override fun onComplete() {}
                    override fun onError(e: Throwable) {
                        Log.d("RetrofitBannerProvider", "onObserveBannerLiveData $e")
                    }
                })
        )
    }

    override fun getBannerLiveData(): MutableLiveData<List<Banner>> = liveData

    override fun dispose() {
        disposable.dispose()
    }
}
