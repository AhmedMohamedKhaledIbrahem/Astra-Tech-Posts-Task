package com.example.astratechpoststask.core.utils

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


inline fun <D, reified E : RootError> ViewModel.performUseCaseOperation(
    crossinline useCase: suspend () -> Result<D, E>,
    crossinline onSuccess: suspend (D) -> Unit,
    crossinline onError: suspend (Result.Error<*, E>) -> Unit,
    scope: CoroutineScope
) {
    scope.launch {
        val result = useCase()
        when (result) {
            is Result.Success -> onSuccess(result.data)
            is Result.Error -> onError(result)
        }
    }
}

inline fun <D, reified E : RootError> ViewModel.performUseCaseOperation(
    crossinline useCase: () -> Result<D, E>,
    crossinline onSuccess: (D) -> Unit,
    crossinline onError: (Result.Error<*, E>) -> Unit,
) {
    val result = useCase()
    when (result) {
        is Result.Success -> onSuccess(result.data)
        is Result.Error -> onError(result)
    }
}