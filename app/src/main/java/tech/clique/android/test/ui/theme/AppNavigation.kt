package tech.clique.android.test.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import tech.clique.android.test.pages.detail.DetailScreen
import tech.clique.android.test.pages.market.MarketListScreen
import tech.clique.android.test.ui.theme.NavigationRoutes.DETAIL
import tech.clique.android.test.ui.theme.NavigationRoutes.MARKET_LIST

object NavigationRoutes {
    const val MARKET_LIST = "marketList"
    const val DETAIL = "detail/{symbol}"
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController, startDestination = MARKET_LIST) {
        composable(MARKET_LIST) { MarketListScreen(navController) }
        composable(
            route = DETAIL,
            arguments = listOf(navArgument("symbol") { type = NavType.StringType })
        ) { backStackEntry ->
            val symbol = backStackEntry.arguments?.getString("symbol") ?: return@composable
            DetailScreen(navController, symbol)
        }
    }
}