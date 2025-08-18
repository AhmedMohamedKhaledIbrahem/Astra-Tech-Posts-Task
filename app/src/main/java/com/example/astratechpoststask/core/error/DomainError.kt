package com.example.astratechpoststask.core.error

sealed interface DomainError: Error {
    enum class Network: DomainError{
        NO_CONNECTION,
        REQUEST_TIMEOUT,
        HOST_UNREACHABLE,

        BAD_REQUEST,
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        PAYLOAD_TOO_LARGE,

        SERVER_ERROR,
        BAD_GATEWAY,
        SERVICE_UNAVAILABLE,
        GATEWAY_TIMEOUT,


        UNKNOWN_ERROR
    }
}