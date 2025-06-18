package tech.clique.android.test.ui.screen.market

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.map
import tech.clique.android.test.R
import tech.clique.android.test.data.DataRepository
import tech.clique.android.test.data.Symbol
import tech.clique.android.test.ui.theme.DecreasingColor
import tech.clique.android.test.ui.theme.IncreasingColor
import tech.clique.android.test.ui.theme.NeutralColor
import tech.clique.android.test.ui.toDisplayName
import tech.clique.android.test.ui.toTrimmedPrice

@Composable
fun MarketList(
    navController: NavController, filters: List<Symbol>? = null
) {
    val viewModel: MarketListViewModel = viewModel()
    val items by viewModel.items.observeAsState(emptyMap())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            items = items.toList().filter { (symbol, _) -> filters == null || symbol in filters },
            key = { (symbol, _) -> symbol }
        ) { (symbol, item) ->
            ItemCard(
                symbol = symbol,
                price = item.price,
                askPrice = item.askPrice,
                bidPrice = item.bidPrice,
                percentage = item.changePercentage,
                navController = navController,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemCard(
    symbol: Symbol,
    price: String,
    askPrice: String,
    bidPrice: String,
    percentage: String,
    navController: NavController,
) {
    var showContextMenu by remember { mutableStateOf(false) }
    // may wrapper a box outside to make clickable fill row
    Row(
        modifier = Modifier
            .heightIn(min = 50.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .combinedClickable(onLongClick = {
                showContextMenu = true
            }, onClick = {
                navController.navigate("detail/${symbol.symbol}")
            }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        //might add ICON here
        Text(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            text = symbol.toDisplayName(),
            style = MaterialTheme.typography.titleMedium
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                text = "\$${price.toTrimmedPrice()}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = "${bidPrice.toTrimmedPrice()} / ${askPrice.toTrimmedPrice()}",
                style = MaterialTheme.typography.bodySmall,
            )
        }

        val p = percentage.toFloat()
        val color = when {
            p > 0 -> IncreasingColor
            p < 0 -> DecreasingColor
            else -> NeutralColor
        }
        Box(
            modifier = Modifier
                .background(
                    color = color, shape = RoundedCornerShape(8.dp)
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

        val isFav by DataRepository.watchListData.map { it.contains(symbol) }.collectAsStateWithLifecycle(false)
        DropdownMenu(
            modifier = Modifier,
            expanded = showContextMenu,
            onDismissRequest = { showContextMenu = false }
        ) {
            DropdownMenuItem(modifier = Modifier.align(Alignment.CenterHorizontally), text = {
                Text(
                    text = stringResource(if (isFav) R.string.remove_from_watch_list else R.string.add_into_watch_list),
                    style = MaterialTheme.typography.labelLarge,
                )
            }, onClick = {
                showContextMenu = false
                if (isFav) DataRepository.watchList.removeSymbol(symbol)
                else DataRepository.watchList.addSymbol(symbol)
            })
        }
    }
}