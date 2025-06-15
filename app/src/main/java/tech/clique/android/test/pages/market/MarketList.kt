package tech.clique.android.test.pages.market

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.clique.android.test.pages.symbolToDisplayName
import tech.clique.android.test.pages.trimmedPrice

@Composable
fun MarketList() {
    val viewModel: MarketListViewModel = viewModel()
    val items by viewModel.items.observeAsState(emptyMap())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            items = items.toList(),
            key = { (symbol, _) -> symbol }
        ) { (symbol, item) ->
            ItemCard(
                symbol = symbol,
                price = item.trimmedPrice,
                percentage = item.changePercentage,
            )
        }
    }
}

@Composable
fun ItemCard(
    symbol: String,
    price: String,
    percentage: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
//            .clickable { },
    ) {
        Text(
            text = symbol.symbolToDisplayName(LocalContext.current),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = price,
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = percentage,
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}