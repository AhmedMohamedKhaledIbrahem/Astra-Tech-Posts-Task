package com.example.astratechpoststask.core.navigation

object NavigationRoute {
    const val HOME = "home"
    const val BLOG_DETAIL = "blog_detail/{id}"
    const val BLOG_CREATE = "blog_create"

    fun blogDetail(id: Int) = "blog_detail/$id"
}