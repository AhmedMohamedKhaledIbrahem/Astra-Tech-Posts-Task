package com.example.astratechpoststask.feature.post.domain.entity

import com.google.gson.annotations.SerializedName

data class BlogEntity(
    val id: Int = -1,
    val title: String? = "",
    val content: String? = "",
    val photo: String?= "",
    val created: String?= "",
    val updated: String?= "",
)
