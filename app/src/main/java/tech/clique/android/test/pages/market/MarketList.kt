package tech.clique.android.test.pages.market

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import tech.clique.android.test.pages.symbolToDisplayName
import tech.clique.android.test.pages.trimmedPrice
import tech.clique.android.test.ui.theme.DecreasingColor
import tech.clique.android.test.ui.theme.IncreasingColor
import tech.clique.android.test.ui.theme.NeutralColor

@Composable
fun MarketList(
    navController: NavController,
    filters: List<String> = emptyList()
) {
    val viewModel: MarketListViewModel = viewModel()
    val items by viewModel.items.observeAsState(emptyMap())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            items = items.toList().filter { (symbol, _) -> filters.isEmpty() || symbol in filters },
            key = { (symbol, _) -> symbol }
        ) { (symbol, item) ->
            ItemCard(
                symbol = symbol,
                price = item.trimmedPrice,
                percentage = item.changePercentage,
                navController = navController,
            )
        }
    }
}

@Composable
fun ItemCard(
    symbol: String,
    price: String,
    percentage: String,
    navController: NavController,
) {
    // may wrapper a box outside to make clickable fill row
    Row(
        modifier = Modifier
            .heightIn(min = 50.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable() {
                navController.navigate("detail/$symbol")
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        //might add ICON here
        Text(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            text = symbol.symbolToDisplayName(LocalContext.current),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            text = price,
            style = MaterialTheme.typography.bodyLarge,
        )

        val p = percentage.toFloat()
        val color = when {
            p > 0 -> IncreasingColor
            p < 0 -> DecreasingColor
            else -> NeutralColor
        }
        Box(
            modifier = Modifier
                .background(
                    color = color,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 5.dp, horizontal = 20.dp),
            contentAlignment = Alignment.Center,

            ) {
            Text(
                text = "$percentage%",
                modifier = Modifier.padding(),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}