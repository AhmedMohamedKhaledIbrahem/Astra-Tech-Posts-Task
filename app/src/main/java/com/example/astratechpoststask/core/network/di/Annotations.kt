package com.example.astratechpoststask.core.network.di

import javax.inject.Qualifier

object Annotations {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CoreNetwork
}