package com.turelo.itunesbrowsersample.data.providers

import com.google.gson.Gson
import com.turelo.itunesbrowsersample.BuildConfig
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

open class RestProvider<SERVICE>(type: Class<SERVICE>) {

    protected var mApi: SERVICE

    companion object {
        const val CONNECTION_TIMEOUT_MS = 3000L
    }

    init {
        val mOkHttpClient = RestClientHttpClientFactory.createHttpClient()

        val builder = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(mOkHttpClient)

        val retrofit = builder.build()

        mApi = retrofit.create(type)
    }
}