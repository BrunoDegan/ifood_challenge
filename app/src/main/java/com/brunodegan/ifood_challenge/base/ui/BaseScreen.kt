package com.brunodegan.ifood_challenge.base.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntSize
import com.brunodegan.ifood_challenge.R
import com.brunodegan.ifood_challenge.base.navigation.BottomNavItem
import com.brunodegan.ifood_challenge.base.navigation.getBottomNavItems

@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    snackbar: @Composable () -> Unit,
    topBar: @Composable () -> Unit,
    onNavBarSelected: (BottomNavItem) -> Unit = {},
    body: @Composable (PaddingValues) -> Unit,
) {
    val buttonSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current
    val offsetDp =
        with(density) { buttonSize.height.toDp() + dimensionResource(R.dimen.double_padding) }

    var selectedItem by remember {
        mutableStateOf(getBottomNavItems().first())
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.onError)
                    .offset(y = -offsetDp)
            ) {
                snackbar()
            }
        },
        topBar = topBar,
        content = { innerPadding ->
            body(innerPadding)
        }, bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary)
            ) {
                getBottomNavItems().forEach { navItem ->
                    NavigationBarItem(
                        selected = navItem.position == selectedItem.position,
                        onClick = {
                            selectedItem = navItem
                            onNavBarSelected(selectedItem)
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
    )
}