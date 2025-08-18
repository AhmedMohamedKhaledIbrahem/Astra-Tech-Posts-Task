package com.example.astratechpoststask.core.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun String.toMultipart(fieldName: String = "photo"): MultipartBody.Part {
    val file = File(this)
    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(fieldName, file.name, requestFile)
}