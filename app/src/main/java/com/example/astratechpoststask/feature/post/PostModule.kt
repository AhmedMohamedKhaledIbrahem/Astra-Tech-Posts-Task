package com.example.astratechpoststask.feature.post

import com.example.astratechpoststask.core.network.di.Annotations
import com.example.astratechpoststask.feature.post.data.source.BlogApiService
import com.example.astratechpoststask.feature.post.data.repository.BlogRepositoryImpl
import com.example.astratechpoststask.feature.post.domain.repository.BlogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostModule {
    @Provides
    @Singleton
    fun provideBlogApiService(
        @Annotations.CoreNetwork retrofit: Retrofit
    ): BlogApiService {
        return retrofit.create(BlogApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideBlogRepository(remoteDataSource: BlogApiService): BlogRepository =
        BlogRepositoryImpl(remoteDataSource)

}