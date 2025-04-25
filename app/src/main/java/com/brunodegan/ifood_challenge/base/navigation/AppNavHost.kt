package com.brunodegan.ifood_challenge.base.navigation

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.brunodegan.ifood_challenge.R
import com.brunodegan.ifood_challenge.base.routes.ScreenRoutes
import com.brunodegan.ifood_challenge.base.ui.BaseScreen
import com.brunodegan.ifood_challenge.base.ui.CustomAppBar
import com.brunodegan.ifood_challenge.ui.screen.nowPlayingMovies.NowPlayingMoviesScreen
import com.brunodegan.ifood_challenge.ui.screen.popularMovies.PopularMoviesScreen
import com.brunodegan.ifood_challenge.ui.screen.topRatedMovies.TopRatedVideosScreen
import com.brunodegan.ifood_challenge.ui.screen.upComingMovies.UpComingMoviesScreen
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val activity = LocalView.current.context as? Activity
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    KoinContext {
        NavHost(navController, startDestination = ScreenRoutes.NowPlayingScreen.route) {
            composable(ScreenRoutes.NowPlayingScreen.route) { navBackStackEntry ->
                navBackStackEntry.id
                BaseScreen(
                    snackbarHostState = snackbarHostState,
                    snackbar = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                                .background(color = MaterialTheme.colorScheme.onError)
                                .safeContentPadding()
                        )
                    },
                    topBar = {
                        CustomAppBar(
                            scrollBehavior = scrollBehavior,
                            title = stringResource(R.string.app_name),
                            onBackButtonClicked = {
                                val popped = navController.popBackStack()
                                if (!popped) {
                                    activity?.finish()
                                }
                            }
                        )
                    },
                    onNavBarSelected = {
                        navController.navigate(it.route, navOptions = navOptions {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.id)
                        })
                    },
                ) { paddingValue ->
                    NowPlayingMoviesScreen(
                        scrollBehavior = scrollBehavior,
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
                        },
                        modifier = Modifier.padding(paddingValue),
                    )
                }
            }
            composable(ScreenRoutes.PopularScreen.route) { navBackStackEntry ->
                BaseScreen(
                    snackbarHostState = snackbarHostState,
                    snackbar = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                                .background(color = MaterialTheme.colorScheme.onError)
                                .safeContentPadding()
                        )
                    },
                    topBar = {
                        CustomAppBar(
                            scrollBehavior = scrollBehavior,
                            title = stringResource(R.string.app_name),
                            onBackButtonClicked = {
                                navController.popBackStack()
                            }
                        )
                    },
                    onNavBarSelected = {
                        navController.navigate(
                            it.route,
                            navOptions = navOptions {
                                popUpTo(navController.graph.id)
                                restoreState = true
                                launchSingleTop = true
                            })
                    }
                ) { paddingValue ->
                    PopularMoviesScreen(
                        scrollBehavior = scrollBehavior,
                        onNavigateUp = {
                            navController.popBackStack()
                        },
                        onShowSnackbar = { msg ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = msg
                                )
                            }
                        },
                        modifier = Modifier.padding(paddingValue),
                    )
                }
            }
            composable(ScreenRoutes.TopRatedScreen.route) { navBackStackEntry ->
                BaseScreen(
                    snackbarHostState = snackbarHostState,
                    snackbar = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                                .background(color = MaterialTheme.colorScheme.onSurface)
                                .safeContentPadding()
                        )
                    },
                    topBar = {
                        CustomAppBar(
                            scrollBehavior = scrollBehavior,
                            title = stringResource(R.string.app_name),
                            onBackButtonClicked = {
                                navController.popBackStack()
                            }
                        )
                    },
                    onNavBarSelected = {
                        navController.navigate(
                            it.route,
                            navOptions = navOptions {
                                popUpTo(navController.graph.id)
                                restoreState = true
                                launchSingleTop = true
                            })
                    }
                ) { paddingValue ->
                    TopRatedVideosScreen(
                        scrollBehavior = scrollBehavior,
                        onNavigateUp = {
                            navController.popBackStack()
                        },
                        onShowSnackbar = { msg ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = msg
                                )
                            }
                        },
                        modifier = Modifier.padding(paddingValue),
                    )
                }
            }
            composable(ScreenRoutes.UpComingScreen.route) { navBackStackEntry ->
                BaseScreen(
                    snackbarHostState = snackbarHostState,
                    snackbar = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                                .background(color = MaterialTheme.colorScheme.onSurface)
                                .safeContentPadding()
                        )
                    },
                    topBar = {
                        CustomAppBar(
                            scrollBehavior = scrollBehavior,
                            title = stringResource(R.string.app_name),
                            onBackButtonClicked = {
                                navController.popBackStack()
                            }
                        )
                    },
                    onNavBarSelected = {
                        navController.navigate(it.route, navOptions = navOptions {
                            popUpTo(navController.graph.id)
                            restoreState = true
                        })
                    }
                ) { paddingValue ->
                    UpComingMoviesScreen(
                        scrollBehavior = scrollBehavior,
                        onNavigateUp = {
                            navController.popBackStack()
                        },
                        onShowSnackbar = { msg ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = msg
                                )
                            }
                        },
                        modifier = Modifier.padding(paddingValue),
                    )
                }
            }
        }
    }
}