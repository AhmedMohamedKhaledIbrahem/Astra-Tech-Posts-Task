package com.example.astratechpoststask.feature.post.presentation.viewmodel.state

import com.example.astratechpoststask.feature.post.domain.entity.BlogEntity

data class BlogState(
    val isLoading: Boolean = false,
    val createBlog: CreateBlog = CreateBlog(),
    val updateBlog: UpdateBlog = UpdateBlog(),
    val deleteBlog: DeleteBlog = DeleteBlog(),
    val id: Int = -1,
    val showBlog: BlogEntity? = null,
    val showBlogs: List<BlogEntity> = emptyList()
)

data class CreateBlog(
    val title: String = "",
    val content: String = "",
    val photo: String = "",
)

data class UpdateBlog(
    val id: Int? = null,
    val title: String? = null,
    val content: String? = null,
    val photo: String? = null,
)


data class DeleteBlog(
    val id: Int? = null
)