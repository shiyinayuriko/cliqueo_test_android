package tech.clique.android.test.ui.screen.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import tech.clique.android.test.data.Symbol

@Composable
fun DetailScreen(
    navController: NavController,
    symbol: Symbol,
    modifier: Modifier = Modifier,
) {
    //                navController.navigate("detail/$BTCUSDT" )
//    Text(symbol.symbol)
    KLineChartScreen(symbol)
}