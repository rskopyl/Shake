package com.rskopyl.shake.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rskopyl.shake.R
import com.rskopyl.shake.ui.custom.ShakeNavigationBar
import com.rskopyl.shake.ui.custom.ShakeNavigationBarItem
import com.rskopyl.shake.ui.theme.ShakeTheme

private val topLevelScreens = listOf(
    ShakeScreen.Discover, ShakeScreen.Barshelf, ShakeScreen.Shaker
)

@Composable
fun ShakeApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val activeDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            if (activeDestination?.route in topLevelRoutes) {
                ShakeTopLevelNavigationBar(navController)
            }
        }
    ) { padding ->
        ShakeNavHost(
            navController = navController,
            startDestination = ShakeScreen.Discover,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
private fun ShakeTopLevelNavigationBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val activeDestination = navBackStackEntry?.destination

    ShakeNavigationBar(modifier = modifier) {
        for (screen in topLevelScreens) {
            val iconId = topLevelScreenToIconId.getValue(screen)
            val labelId = topLevelScreenToLabelId.getValue(screen)
            ShakeNavigationBarItem(
                selected = activeDestination?.hierarchy
                    ?.any { it.route == screen.route } == true,
                icon = {
                    Icon(
                        painter = painterResource(iconId),
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(labelId)) },
                onClick = {
                    navController.run {
                        navigate(screen.route) {
                            popUpTo(graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

private val topLevelRoutes = topLevelScreens.map { it.route }

private val topLevelScreenToIconId = mapOf(
    ShakeScreen.Discover to R.drawable.ic_discover_outlined,
    ShakeScreen.Barshelf to R.drawable.ic_barshelf_outlined,
    ShakeScreen.Shaker to R.drawable.ic_shaker_outlined
)

private val topLevelScreenToLabelId = mapOf(
    ShakeScreen.Discover to R.string.discover,
    ShakeScreen.Barshelf to R.string.barshelf,
    ShakeScreen.Shaker to R.string.shaker
)

@Preview
@Composable
private fun ShakeAppPreview() {
    ShakeTheme {
        ShakeApp()
    }
}