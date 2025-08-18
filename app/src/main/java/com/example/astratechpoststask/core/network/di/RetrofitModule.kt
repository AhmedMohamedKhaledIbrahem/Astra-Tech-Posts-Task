package com.example.astratechpoststask.core.network.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.astratechpoststask.BuildConfig
import com.example.astratechpoststask.core.network.di.Annotations.CoreNetwork
import com.example.astratechpoststask.core.network.factory.NetworkAdapterFactory
import com.example.astratechpoststask.core.utils.Headers
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun provideHeaderInterceptor(): Interceptor =
        Interceptor { chain ->
            val newRequest =
                chain
                    .request()
                    .newBuilder()
                    .addHeader(Headers.Keys.ACCEPT, Headers.ACCEPT_VALUE)
                    .build()
            chain.proceed(newRequest)
        }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    @Singleton
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context
    ): ChuckerInterceptor = ChuckerInterceptor
        .Builder(context)
        .redactHeaders(Headers.Keys.AUTHORIZATION)
        .build()

    @CoreNetwork
    @Provides
    @Singleton
    fun provideOKHttpClient(
        headerInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .applyCommonConfiguration(
                headersInterceptor = headerInterceptor,
                loggingInterceptor = loggingInterceptor,
                chuckerInterceptor = chuckerInterceptor,
            )
            .retryOnConnectionFailure(false)
            .build()

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @CoreNetwork
    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson,
        apiCallAdapterFactory: NetworkAdapterFactory,
        @CoreNetwork okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit
            .Builder()
            .applyCommonConfiguration(apiCallAdapterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .build()


}