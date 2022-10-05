package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.Api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiAdapter{
    private var apiconf: ApiConfig = ApiConfig()

    val retrofit: Api = Retrofit.Builder()
        .baseUrl(apiconf.getBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)
}