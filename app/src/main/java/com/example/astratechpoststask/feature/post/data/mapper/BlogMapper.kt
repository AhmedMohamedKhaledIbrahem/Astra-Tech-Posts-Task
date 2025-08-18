package com.example.astratechpoststask.feature.post.data.mapper

import com.example.astratechpoststask.feature.post.data.model.BlogDto
import com.example.astratechpoststask.feature.post.domain.entity.BlogEntity

fun BlogDto.toBlog(): BlogEntity = BlogEntity(
    id = id,
    title = title,
    content = content,
    photo = photo,
    created = created,
    updated = updated
)

fun List<BlogDto>.toBlogList(): List<BlogEntity> = map { it.toBlog() }

fun BlogEntity.toBlogDto(): BlogDto = BlogDto(
    id = id,
    title = title,
    content = content,
    photo = photo,
    created = created,
    updated = updated
)

