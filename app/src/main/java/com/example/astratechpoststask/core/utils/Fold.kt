package com.example.astratechpoststask.core.utils

inline fun <D, E : RootError, R> Result<D, E>.fold(
    onSuccess: (D) -> R,
    onFailure: (E) -> R
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onFailure(error)
}