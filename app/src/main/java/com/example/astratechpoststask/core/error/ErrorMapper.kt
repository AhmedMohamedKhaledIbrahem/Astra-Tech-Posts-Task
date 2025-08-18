package com.example.astratechpoststask.core.error

fun DataError.Network.toDomainError(): DomainError.Network = when (this) {
    DataError.Network.NO_CONNECTION -> DomainError.Network.NO_CONNECTION
    DataError.Network.REQUEST_TIMEOUT -> DomainError.Network.REQUEST_TIMEOUT
    DataError.Network.HOST_UNREACHABLE -> DomainError.Network.HOST_UNREACHABLE
    DataError.Network.BAD_REQUEST -> DomainError.Network.BAD_REQUEST
    DataError.Network.UNAUTHORIZED -> DomainError.Network.UNAUTHORIZED
    DataError.Network.FORBIDDEN -> DomainError.Network.FORBIDDEN
    DataError.Network.NOT_FOUND -> DomainError.Network.NOT_FOUND
    DataError.Network.PAYLOAD_TOO_LARGE -> DomainError.Network.PAYLOAD_TOO_LARGE
    DataError.Network.SERVER_ERROR -> DomainError.Network.SERVER_ERROR
    DataError.Network.SERVICE_UNAVAILABLE -> DomainError.Network.SERVICE_UNAVAILABLE
    DataError.Network.GATEWAY_TIMEOUT -> DomainError.Network.GATEWAY_TIMEOUT
    DataError.Network.UNKNOWN_ERROR -> DomainError.Network.UNKNOWN_ERROR
    DataError.Network.BAD_GATEWAY -> DomainError.Network.BAD_GATEWAY
}
fun DataError.toDomainError(): DomainError = when (this) {
    is DataError.Network -> this.toDomainError()
}

