package ru.lm.cheeseapp.model.network.api

import io.reactivex.Observable
import retrofit2.http.GET
import ru.lm.cheeseapp.model.pojo.Banner
import ru.lm.cheeseapp.model.pojo.Offer

interface CheeseAPI {

    @GET("/sl.files/banners.json")
    fun getBannerResponse(): Observable<List<Banner>>

    @GET("/sl.files/offers.json")
    fun getOfferResponse(): Observable<List<Offer>>
}
