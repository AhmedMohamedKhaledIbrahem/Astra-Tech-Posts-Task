package com.example.astratechpoststask.core.event

import com.example.astratechpoststask.core.ui_text.UiText


typealias NavigateEvent = UiEvent.NavEvent
typealias ShowSnackBarEvent = UiEvent.ShowSnackBar
typealias CombinedEvents = UiEvent.CombinedEvents

sealed interface UiEvent {
    sealed interface NavEvent : UiEvent{
        data object SplashScreen : NavEvent
        data class BlogDetailsScreen(val id: Int): NavEvent
        data object HomeScreen: NavEvent
    }
    data class  ShowSnackBar(val message: UiText) : UiEvent
    data class CombinedEvents(val event:List<UiEvent>): UiEvent
}

