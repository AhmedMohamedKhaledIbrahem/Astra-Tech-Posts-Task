package com.example.astratechpoststask.feature.post.domain.repository

import com.example.astratechpoststask.core.error.DomainError
import com.example.astratechpoststask.core.utils.Result
import com.example.astratechpoststask.feature.post.domain.entity.BlogEntity
import com.example.astratechpoststask.feature.post.domain.entity.MessageEntity

interface BlogRepository {
    suspend fun getPosts(): Result<List<BlogEntity>, DomainError>
    suspend fun getPostById(id: Int): Result<BlogEntity, DomainError>
    suspend fun deletePostById(id: Int): Result<MessageEntity, DomainError>
    suspend fun createPost(blogParms: BlogEntity): Result<BlogEntity, DomainError>
    suspend fun updatePost(blogParms: BlogEntity?): Result<BlogEntity, DomainError>
}