package com.example.astratechpoststask.core.error

import retrofit2.HttpException
import java.io.IOException


typealias Network = DataError.Network

fun Exception.toRemoteDataError(): DataError = when (this) {
    is HttpException -> {
        when (this.code()) {
            400 -> Network.BAD_REQUEST
            401 -> Network.UNAUTHORIZED
            403 -> Network.FORBIDDEN
            404 -> Network.NOT_FOUND
            413 -> Network.PAYLOAD_TOO_LARGE
            500 -> Network.SERVER_ERROR
            502 -> Network.BAD_GATEWAY
            503 -> Network.SERVICE_UNAVAILABLE
            504 -> Network.GATEWAY_TIMEOUT
            else -> Network.UNKNOWN_ERROR
        }
    }
    is IOException -> Network.NO_CONNECTION
    else -> Network.UNKNOWN_ERROR
}