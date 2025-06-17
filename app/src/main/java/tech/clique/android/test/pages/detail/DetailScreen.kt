package tech.clique.android.test.pages.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import tech.clique.android.test.data.network.binance.Symbol

@Composable
fun DetailScreen(
    navController: NavController,
    @Symbol symbol: String,
    modifier: Modifier = Modifier,
) {
    //                navController.navigate("detail/$BTCUSDT" )
    Text(symbol)
}