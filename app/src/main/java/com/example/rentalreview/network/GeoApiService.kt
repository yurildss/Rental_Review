package com.example.rentalreview.network

import com.example.rentalreview.model.City
import com.example.rentalreview.model.Country
import com.example.rentalreview.model.GeoResponse
import com.example.rentalreview.model.Region
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://wft-geo-db.p.rapidapi.com/v1/geo/"

val client = OkHttpClient
    .Builder()
    .addInterceptor {
        chain ->
        val newRequest = chain
            .request()
            .newBuilder()
            .addHeader("x-rapidapi-key", "12f8307d74msh521d46ebf01771ap1898e1jsnd9fe95d59af5")
            .addHeader("x-rapidapi-host", "wft-geo-db.p.rapidapi.com")
            .build()
        chain.proceed(newRequest)
    }.build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface GeoApiService{

    @GET("countries")
    suspend fun getCountry(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ) : GeoResponse<Country>

    @GET("countries/{countryCode}/regions")
    suspend fun getRegion( @Path("countryCode") countryCode: String): GeoResponse<Region>

    @GET("regions/{regionCode}/cities")
    suspend fun getCities( @Path("regionCode") regionCode: String) : GeoResponse<City>
}

object GeoApi{
    val retrofitService: GeoApiService by lazy{
        retrofit.create(GeoApiService::class.java)
    }
}