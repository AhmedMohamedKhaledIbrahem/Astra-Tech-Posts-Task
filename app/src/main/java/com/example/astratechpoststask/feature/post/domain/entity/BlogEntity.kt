package com.example.astratechpoststask.feature.post.domain.entity

import com.google.gson.annotations.SerializedName

data class BlogEntity(
    val id: Int,
    val title: String,
    val content: String,
    val photo: String,
    val created: String,
    val updated: String,
)
data class BlogCreateEntity(
    val title: String,
    val content: String,
    val photo: String,
)
data class BlogUpdateEntity(
    val id: Int,
    val title: String? = null,
    val content: String? = null,
    val photo: String?= null,
)