package ru.lm.cheeseapp.module

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import ru.lm.cheeseapp.model.network.*
import ru.lm.cheeseapp.model.network.api.CheeseAPI
import ru.lm.cheeseapp.model.network.api.CheeseNetworkAdapter
import ru.lm.cheeseapp.model.pojo.Banner
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModuleProvider::class, CheeseNetworkAdapter::class])
interface AppComponent {
    fun injectBannerProvider(): RetrofitBannerProvider
    fun injectOfferProvider(): RetrofitOfferProvider
    fun injectNetworkAdapter(): CheeseAPI
}
