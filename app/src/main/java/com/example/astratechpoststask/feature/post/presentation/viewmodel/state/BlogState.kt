package com.example.astratechpoststask.feature.post.presentation.viewmodel.state

import com.example.astratechpoststask.feature.post.domain.entity.BlogEntity

data class BlogState(
    val isLoading: Boolean = false,
    val createBlog: CreateBlog? = null,
    val updateBlog: UpdateBlog? = null,
    val deleteBlog: DeleteBlog? = null,
    val id: Int? = null,
    val showBlog : BlogEntity?=null,
    val showBlogs : List<BlogEntity>? = emptyList()
)

data class CreateBlog(
    val title: String? = null,
    val content: String? = null,
    val photo: String? = null,
    val update:String? = null,
    val create: String? = null
)

data class UpdateBlog(
    val id: Int? = null,
    val title: String? = null,
    val content: String? = null,
    val photo: String? = null,
    val update:String? = null,
    val create: String? = null
)


data class DeleteBlog(
    val id: Int? = null
)