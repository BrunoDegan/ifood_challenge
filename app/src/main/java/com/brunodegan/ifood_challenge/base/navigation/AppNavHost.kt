package com.brunodegan.ifood_challenge.base.navigation

import android.app.Activity
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.brunodegan.ifood_challenge.R
import com.brunodegan.ifood_challenge.base.routes.ScreenRoutes
import com.brunodegan.ifood_challenge.base.ui.CustomAppBar
import com.brunodegan.ifood_challenge.ui.screen.favoriteMovies.FavoriteMoviesScreen
import com.brunodegan.ifood_challenge.ui.screen.nowPlayingMovies.NowPlayingMoviesScreen
import com.brunodegan.ifood_challenge.ui.screen.popularMovies.PopularMoviesScreen
import com.brunodegan.ifood_challenge.ui.screen.topRatedMovies.TopRatedVideosScreen
import com.brunodegan.ifood_challenge.ui.screen.upComingMovies.UpComingMoviesScreen
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val activity = LocalView.current.context as? Activity
    val topbarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var selectedItem by remember {
        mutableStateOf(getBottomNavItems().first())
    }
    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    KoinContext {
        Scaffold(
            topBar = {
                CustomAppBar(
                    scrollBehavior = topbarScrollBehavior,
                    title = stringResource(R.string.app_name),
                    onBackButtonClicked = {
                        activity?.finish()
                    }
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.safeContentPadding()
                )
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primary)
                ) {
                    getBottomNavItems().forEach { navItem ->
                        NavigationBarItem(
                            selected = navItem.position == selectedItem.position,
                            onClick = {
                                selectedItem = navItem
                                navController.navigate(navItem.route) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(navItem.iconResourceId),
                                    contentDescription = "${navItem.label} icon",
                                )
                            },
                            label = {
                                Text(stringResource(navItem.label))
                            }
                        )
                    }
                }
            }
        ) { paddingValues ->
            SharedTransitionScope { modifier ->
                NavHost(
                    navController = navController,
                    startDestination = ScreenRoutes.NowPlayingScreen.route,
                    modifier = modifier.padding(paddingValues),
                    enterTransition = {
                        fadeIn(animationSpec = tween(300)) + slideInVertically(
                            initialOffsetY = { -it },
                            animationSpec = tween(300)
                        )
                    },
                    exitTransition = {
                        fadeOut(animationSpec = tween(500))
                    },
                    popEnterTransition = {
                        slideInHorizontally(animationSpec = tween(500)) { it }
                    },
                    popExitTransition = {
                        slideOutHorizontally(animationSpec = tween(500)) { -it }
                    }
                ) {
                    composable(ScreenRoutes.NowPlayingScreen.route) {
                        NowPlayingMoviesScreen(
                            animatedVisibilityScope = this,
                            listState = listState,
                            scrollBehavior = topbarScrollBehavior,
                            onShowSnackbar = { msg ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = msg,
                                        withDismissAction = true,
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            onNavigateUp = {
                                val popped = navController.popBackStack()
                                if (!popped) {
                                    activity?.finish()
                                }
                            }
                        )
                    }
                    composable(ScreenRoutes.PopularScreen.route) {
                        PopularMoviesScreen(
                            listState = listState,
                            scrollBehavior = topbarScrollBehavior,
                            onNavigateUp = {
                                val popped = navController.popBackStack()
                                if (!popped) {
                                    activity?.finish()
                                }
                            },
                            onShowSnackbar = { msg ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = msg)
                                }
                            }
                        )
                    }
                    composable(ScreenRoutes.TopRatedScreen.route) { _ ->
                        TopRatedVideosScreen(
                            listState = listState,
                            scrollBehavior = topbarScrollBehavior,
                            onNavigateUp = {
                                val popped = navController.popBackStack()
                                if (!popped) {
                                    activity?.finish()
                                }
                            },
                            onShowSnackbar = { msg ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = msg)
                                }
                            },
                        )
                    }
                    composable(ScreenRoutes.UpComingScreen.route) {
                        UpComingMoviesScreen(
                            listState = listState,
                            scrollBehavior = topbarScrollBehavior,
                            onNavigateUp = {
                                val popped = navController.popBackStack()
                                if (!popped) {
                                    activity?.finish()
                                }
                            },
                            onShowSnackbar = { msg ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = msg)
                                }
                            }
                        )
                    }
                    composable(ScreenRoutes.FavoritesScreen.route) {
                        FavoriteMoviesScreen(
                            listState = listState,
                            scrollBehavior = topbarScrollBehavior,
                            onNavigateUp = {
                                val popped = navController.popBackStack()
                                if (!popped) {
                                    activity?.finish()
                                }
                            },
                            onShowSnackbar = { msg ->
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = msg)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}