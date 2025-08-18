package com.example.astratechpoststask.feature.post

import com.example.astratechpoststask.feature.post.data.source.BlogApiService
import com.example.astratechpoststask.feature.post.data.source.BlogRepositoryImpl
import com.example.astratechpoststask.feature.post.domain.repository.BlogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostModule {
    @Provides
    @Singleton
    fun provideBlogRepository(remoteDataSource: BlogApiService): BlogRepository =
        BlogRepositoryImpl(remoteDataSource)

}