package com.example.astratechpoststask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.astratechpoststask.core.navigation.NavHost
import com.example.astratechpoststask.core.navigation.NavigationRoute
import com.example.astratechpoststask.ui.theme.AstraTechPostsTaskTheme
import com.example.astratechpoststask.ui.theme.backgroundLight
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(0)
        )
        setContent {
            AstraTechPostsTaskTheme {
                val snackBarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = backgroundLight,
                        ),
                    snackbarHost = { SnackbarHost(snackBarHostState) },
                    floatingActionButton = {
                        when (currentRoute) {
                            NavigationRoute.HOME -> {
                                FloatingActionButton(
                                    onClick = {
                                        navController.navigate(NavigationRoute.BLOG_CREATE)
                                    }
                                ) {
                                    Icon(Icons.Default.Create, contentDescription = "Add Blog")
                                }
                            }
                        }

                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .clip(
                                RoundedCornerShape(
                                    topStart = 15.dp,
                                    topEnd = 15.dp,
                                )
                            )
                            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                            .consumeWindowInsets(WindowInsets.navigationBars)

                    ) {
                        NavHost(
                            navController = navController,
                            snackBarHostState = snackBarHostState,
                        )
                    }
                }
            }
        }
    }
}

