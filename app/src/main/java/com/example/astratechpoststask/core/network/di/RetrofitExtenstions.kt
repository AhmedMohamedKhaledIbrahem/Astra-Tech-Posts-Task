package com.example.astratechpoststask.core.network.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.astratechpoststask.BuildConfig
import com.example.astratechpoststask.core.network.factory.NetworkAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val REQUEST_TIMEOUT = 60L

fun OkHttpClient.Builder.applyCommonConfiguration(
    headersInterceptor: Interceptor,
    loggingInterceptor: HttpLoggingInterceptor,
    chuckerInterceptor: ChuckerInterceptor
): OkHttpClient.Builder {
    this
        .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(headersInterceptor)
    if (BuildConfig.DEBUG) {
        this
            .addInterceptor(loggingInterceptor)
            .addInterceptor(chuckerInterceptor)
    }
    return this
}

fun Retrofit.Builder.applyCommonConfiguration(
    apiCallAdapterFactory: NetworkAdapterFactory
): Retrofit.Builder {
    addCallAdapterFactory(apiCallAdapterFactory)
    return this
}