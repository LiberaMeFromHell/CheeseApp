package ru.lm.cheeseapp.model.network

import dagger.Module
import dagger.Provides
import ru.lm.cheeseapp.model.network.RetrofitBannerProvider
import ru.lm.cheeseapp.model.network.RetrofitOfferProvider

@Module
class NetworkModuleProvider {

    @Provides
    fun provideBannerProvider() = RetrofitBannerProvider()

    @Provides
    fun provideOfferProvider() = RetrofitOfferProvider()
}
