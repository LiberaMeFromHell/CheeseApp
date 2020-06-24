package ru.lm.cheeseapp.module

import dagger.Component
import ru.lm.cheeseapp.model.network.NetworkModuleProvider
import ru.lm.cheeseapp.model.network.RetrofitBannerProvider
import ru.lm.cheeseapp.model.network.RetrofitOfferProvider
import ru.lm.cheeseapp.model.network.api.CheeseAPI
import ru.lm.cheeseapp.model.network.api.CheeseNetworkAdapter
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModuleProvider::class, CheeseNetworkAdapter::class])
interface AppComponent {
    fun injectBannerProvider(): RetrofitBannerProvider
    fun injectOfferProvider(): RetrofitOfferProvider
    fun injectNetworkAdapter(): CheeseAPI
}
