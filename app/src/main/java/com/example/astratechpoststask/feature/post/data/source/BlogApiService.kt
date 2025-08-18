package com.example.astratechpoststask.feature.post.data.source

import com.example.astratechpoststask.BuildConfig
import com.example.astratechpoststask.core.error.DataError
import com.example.astratechpoststask.core.utils.Result
import com.example.astratechpoststask.feature.post.data.model.BlogDto
import com.example.astratechpoststask.feature.post.data.model.MessageDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface BlogApiService {

    @GET(BuildConfig.ALL_BLOGS_ENDPOINT)
    suspend fun getAllBlogs(): Result<List<BlogDto>, DataError>

    @GET("${BuildConfig.SHOE_BLOG_ENDPOINT}/{id}")
    suspend fun getBlogById(@Path("id") id: Int): Result<BlogDto, DataError>

    @Multipart
    @POST(BuildConfig.STORE_BLOG_ENDPOINT)
    suspend fun storeBlog(
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part photo: MultipartBody.Part
    ): Result<BlogDto, DataError>

    @Multipart
    @POST("${BuildConfig.UPDATE_BLOG_ENDPOINT}/{id}")
    suspend fun updateBlog(
        @Path("id") id: Int,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody,
        @Part photo: MultipartBody.Part
    ): Result<BlogDto, DataError>

    @POST("${BuildConfig.DELETE_BLOG_ENDPOINT}/{id}")
    suspend fun deleteBlog(@Path("id") id: Int): Result<MessageDto, DataError>
}