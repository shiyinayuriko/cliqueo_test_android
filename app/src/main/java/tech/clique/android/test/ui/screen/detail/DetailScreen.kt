package tech.clique.android.test.ui.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import tech.clique.android.test.data.Symbol
import tech.clique.android.test.data.KlineInterval
import tech.clique.android.test.ui.toDisplayName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    symbol: Symbol,
    modifier: Modifier = Modifier,
) {
    val tabs = KlineInterval.entries
    var currentSelected by remember { mutableIntStateOf(tabs.indexOf(KlineInterval.INTERVAL_15m)) }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary),
                title = { Text(symbol.toDisplayName()) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            ScrollableTabRow(
                selectedTabIndex = currentSelected,
                edgePadding = 0.dp,
            ) {
                tabs.forEachIndexed { index, interval ->
                    Tab(
                        selected = currentSelected == index,
                        onClick = {
                            currentSelected = index
                        },
                        text = { Text(stringResource(interval.display)) }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 300.dp),
            ) {
                KLineChart(
                    modifier = Modifier.fillMaxSize(),
                    symbol = symbol,
                    interval = tabs[currentSelected],
                )
            }
        }
    }
}