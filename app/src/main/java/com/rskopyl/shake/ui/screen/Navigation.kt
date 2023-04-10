package com.rskopyl.shake.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rskopyl.shake.ui.screen.barshelf.BarshelfScreen
import com.rskopyl.shake.ui.screen.details.DetailsScreen
import com.rskopyl.shake.ui.screen.discover.DiscoverScreen
import com.rskopyl.shake.ui.screen.shaker.ShakerScreen

sealed class ShakeScreen(
    val name: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    object Discover : ShakeScreen(name = "discover")

    object Barshelf : ShakeScreen(name = "barshelf")

    object Shaker : ShakeScreen(name = "shaker")

    object Details : ShakeScreen(
        name = "details",
        arguments = listOf(
            navArgument(name = "cocktailId") {
                type = NavType.StringType
            }
        )
    )
}

@Composable
fun ShakeNavHost(
    modifier: Modifier = Modifier,
    startDestination: ShakeScreen = ShakeScreen.Discover,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        composable(ShakeScreen.Discover.route) {
            DiscoverScreen(
                onNavigateToDetails = { cocktailId ->
                    navController.navigate(
                        ShakeScreen.Details.routeOf(cocktailId)
                    )
                }
            )
        }
        composable(ShakeScreen.Barshelf.route) {
            BarshelfScreen(
                onNavigateToDetails = { cocktailId ->
                    navController.navigate(
                        ShakeScreen.Details.routeOf(cocktailId)
                    )
                }
            )
        }
        composable(ShakeScreen.Shaker.route) {
            ShakerScreen()
        }
        composable(
            ShakeScreen.Details.route,
            ShakeScreen.Details.arguments
        ) { entry ->
            val values = entry.arguments ?: return@composable
            DetailsScreen(
                cocktailId = values
                    .getString("cocktailId")
                    ?: return@composable,
                onNavigateBack = navController::popBackStack
            )
        }
    }
}

val ShakeScreen.route: String get() = listOf(name)
    .plus(arguments.map { "{${it.name}}" })
    .joinToString(separator = "/")

fun ShakeScreen.routeOf(vararg values: Any): String {
    var route = route
    for ((index, argument) in arguments.withIndex()) {
        route = route.replace(
            "{${argument.name}}",
            values[index].toString()
        )
    }
    return route
}