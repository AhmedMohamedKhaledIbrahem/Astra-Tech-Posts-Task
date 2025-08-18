package com.example.astratechpoststask.feature.post.data.model

import com.google.gson.annotations.SerializedName

data class BlogDto(
    val id: Int,
    val title: String,
    val content: String,
    val photo: String,
    @SerializedName("created_at") val created: String,
    @SerializedName("updated_at") val updated: String
)
