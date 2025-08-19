package com.example.astratechpoststask.core.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.astratechpoststask.feature.post.presentation.screen.create.CreateBlogScreen
import com.example.astratechpoststask.feature.post.presentation.screen.details.DetailsScreenRoot
import com.example.astratechpoststask.feature.post.presentation.screen.home.HomeScreen
import com.example.astratechpoststask.feature.post.presentation.viewmodel.BlogViewModel

@Composable
fun NavHost(
    navController: NavHostController,
    snackBarHostState: SnackbarHostState,
) {
    val blogViewModel: BlogViewModel = hiltViewModel<BlogViewModel>()

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.HOME,
    ) {
        composable(NavigationRoute.HOME) {
            HomeScreen(
                blogViewModel = blogViewModel,
                snackBarHostState = snackBarHostState,
                onBlogClick = { blogId ->

                    navController.navigate(NavigationRoute.blogDetail(blogId))
                }
            )
        }
        composable(
            NavigationRoute.BLOG_DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val blogId = backStackEntry.arguments?.getInt("id")
            if (blogId != null) DetailsScreenRoot(
                blogId = blogId,
                blogViewModel = blogViewModel,
                snackbarHostState = snackBarHostState,
                onDeleteButtonClicked = {
                    navController.navigate(NavigationRoute.HOME)
                },
            )
        }

        composable(
            NavigationRoute.BLOG_CREATE
        ) {
            CreateBlogScreen(
                blogViewModel = blogViewModel,
                snackBarHostState = snackBarHostState,
                onCreateBlogButtonClicked = {
                    navController.navigate(NavigationRoute.HOME) {

                    }
                }
            )
        }
    }
}