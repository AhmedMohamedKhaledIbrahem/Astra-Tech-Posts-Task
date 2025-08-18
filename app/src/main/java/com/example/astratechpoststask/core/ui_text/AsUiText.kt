package com.example.astratechpoststask.core.ui_text

import com.example.astratechpoststask.R
import com.example.astratechpoststask.core.error.DomainError
import com.example.astratechpoststask.core.ui_text.UiText.StringResource

fun DomainError.asUiText(): UiText {
    return when (this) {
        DomainError.Network.BAD_REQUEST -> StringResource(R.string.error_bad_request)
        DomainError.Network.UNAUTHORIZED -> StringResource(R.string.error_unauthorized)
        DomainError.Network.FORBIDDEN -> StringResource(R.string.error_forbidden)
        DomainError.Network.NOT_FOUND -> StringResource(R.string.error_not_found)
        DomainError.Network.PAYLOAD_TOO_LARGE -> StringResource(R.string.error_payload_too_large)
        DomainError.Network.SERVER_ERROR -> StringResource(R.string.error_server_error)
        DomainError.Network.BAD_GATEWAY -> StringResource(R.string.error_bad_gateway)
        DomainError.Network.SERVICE_UNAVAILABLE -> StringResource(R.string.error_service_unavailable)
        DomainError.Network.GATEWAY_TIMEOUT -> StringResource(R.string.error_gateway_timeout)
        DomainError.Network.NO_CONNECTION -> StringResource(R.string.error_no_connection)
        DomainError.Network.UNKNOWN_ERROR -> StringResource(R.string.error_unknown)
        DomainError.Network.REQUEST_TIMEOUT -> StringResource(R.string.error_request_timeout)
        DomainError.Network.HOST_UNREACHABLE -> StringResource(R.string.error_host_unreachable)

    }
}