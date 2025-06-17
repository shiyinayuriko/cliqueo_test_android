package tech.clique.android.test.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import tech.clique.android.test.data.Symbol
import tech.clique.android.test.ui.screen.NavigationRoutes.DETAIL
import tech.clique.android.test.ui.screen.NavigationRoutes.MARKET_LIST
import tech.clique.android.test.ui.screen.detail.DetailScreen
import tech.clique.android.test.ui.screen.market.MarketListScreen

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
            val symbolStr = backStackEntry.arguments?.getString("symbol") ?: return@composable
            val symbol = Symbol.fromSymbol(symbolStr)
            DetailScreen(navController, symbol)
        }
    }
}