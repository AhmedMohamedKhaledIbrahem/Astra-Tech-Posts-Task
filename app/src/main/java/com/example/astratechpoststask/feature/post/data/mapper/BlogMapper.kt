package com.example.astratechpoststask.feature.post.data.mapper

import com.example.astratechpoststask.core.utils.formatTimestamp
import com.example.astratechpoststask.feature.post.data.model.BlogDto
import com.example.astratechpoststask.feature.post.domain.entity.BlogEntity

fun BlogDto.toBlog(): BlogEntity = BlogEntity(
    id = id,
    title = title,
    content = content,
    photo = photo,
    created = formatTimestamp(created),
    updated = formatTimestamp(updated)
)

fun List<BlogDto>.toBlogList(): List<BlogEntity> = map { it.toBlog() }



