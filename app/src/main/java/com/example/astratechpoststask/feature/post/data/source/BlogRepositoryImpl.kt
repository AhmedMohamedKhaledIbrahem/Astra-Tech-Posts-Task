package com.example.astratechpoststask.feature.post.data.source

import com.example.astratechpoststask.core.error.DomainError
import com.example.astratechpoststask.core.error.toDomainError
import com.example.astratechpoststask.core.utils.Result
import com.example.astratechpoststask.core.utils.fold
import com.example.astratechpoststask.core.utils.toMultipart
import com.example.astratechpoststask.feature.post.data.mapper.toBlog
import com.example.astratechpoststask.feature.post.data.mapper.toBlogList
import com.example.astratechpoststask.feature.post.data.mapper.toMessage
import com.example.astratechpoststask.feature.post.domain.entity.BlogEntity
import com.example.astratechpoststask.feature.post.domain.entity.MessageEntity
import com.example.astratechpoststask.feature.post.domain.repository.BlogRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class BlogRepositoryImpl @Inject constructor(
    private val remoteDataSource: BlogApiService
) : BlogRepository {
    override suspend fun getPosts(): Result<List<BlogEntity>, DomainError> =
        remoteDataSource.getAllBlogs().fold(
            onFailure = { Result.Error(it.toDomainError()) },
            onSuccess = { Result.Success(it.toBlogList()) }
        )


    override suspend fun getPostById(id: Int): Result<BlogEntity, DomainError> =
        remoteDataSource.getBlogById(id).fold(
            onFailure = { Result.Error(it.toDomainError()) },
            onSuccess = { Result.Success(it.toBlog()) }
        )

    override suspend fun deletePostById(id: Int): Result<MessageEntity, DomainError> =
        remoteDataSource.deleteBlog(id).fold(
            onFailure = { Result.Error(it.toDomainError()) },
            onSuccess = { Result.Success(it.toMessage()) }
        )

    override suspend fun createPost(blogParms: BlogEntity): Result<BlogEntity, DomainError> {
        val titleBody = blogParms.title.toRequestBody("text/plain".toMediaTypeOrNull())
        val contentBody = blogParms.content.toRequestBody("text/plain".toMediaTypeOrNull())
        val photo = blogParms.photo.toMultipart()
        return remoteDataSource.storeBlog(titleBody, contentBody, photo).fold(
            onFailure = { Result.Error(it.toDomainError()) },
            onSuccess = { Result.Success(it.toBlog()) },
        )
    }

    override suspend fun updatePost(blogParms: BlogEntity): Result<BlogEntity, DomainError> {
        val id = blogParms.id
        val titleBody = blogParms.title.toRequestBody("text/plain".toMediaTypeOrNull())
        val contentBody = blogParms.content.toRequestBody("text/plain".toMediaTypeOrNull())
        val photo = blogParms.photo.toMultipart()

        return remoteDataSource.updateBlog(id, titleBody, contentBody, photo).fold(
            onFailure = { Result.Error(it.toDomainError()) },
            onSuccess = { Result.Success(it.toBlog()) }
        )
    }
}