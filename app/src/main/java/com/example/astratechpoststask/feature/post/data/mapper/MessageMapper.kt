package com.example.astratechpoststask.feature.post.data.mapper

import com.example.astratechpoststask.feature.post.data.model.MessageDto
import com.example.astratechpoststask.feature.post.domain.entity.MessageEntity

fun MessageDto.toMessage(): MessageEntity = MessageEntity(
    message = message
)