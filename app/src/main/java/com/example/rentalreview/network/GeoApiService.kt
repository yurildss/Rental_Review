package com.example.rentalreview.network

import com.example.rentalreview.model.City
import com.example.rentalreview.model.Country
import com.example.rentalreview.model.State
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.countrystatecity.in/v1/"

val client = OkHttpClient
    .Builder()
    .addInterceptor {
        chain ->
        val newRequest = chain
            .request()
            .newBuilder()
            .addHeader("X-CSCAPI-KEY", "WHhIaW1YRDlCV2I4dXNxUnBucGZ1emFPcmQ3dWpiOFFjZUhuSzB3RQ==")
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
    ) : List<Country>

    @GET("countries/{countryCode}/states")
    suspend fun getState( @Path("countryCode") countryCode: String): List<State>

    @GET("countries/{countryCode}/states/{stateCode}/cities")
    suspend fun getCities(
        @Path("countryCode") countryCode: String,
        @Path("stateCode") stateCode: String
    ): List<City>

}

object GeoApi {
    val retrofitService: GeoApiService by lazy {
        try {
            retrofit.create(GeoApiService::class.java)
        } catch (e: Exception) {
            throw RuntimeException("Erro ao criar o serviço Retrofit: ${e.message}", e)
        }
    }
}