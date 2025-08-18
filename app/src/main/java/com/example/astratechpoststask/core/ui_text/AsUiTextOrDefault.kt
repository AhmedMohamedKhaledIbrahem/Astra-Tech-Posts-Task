package com.example.astratechpoststask.core.ui_text

import com.example.astratechpoststask.core.error.DomainError
import com.example.astratechpoststask.core.utils.Result
import com.example.astratechpoststask.core.utils.RootError



inline fun <reified E : RootError> Result.Error<*,E>.asUiTextOrDefault(
    default: UiText = UiText.DynamicString(""),
): UiText {
    return when (val err = this.error) {
        is DomainError -> err.asUiText()
        else -> default
    }
}