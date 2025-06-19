package tech.clique.android.test.ui.screen.market

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import tech.clique.android.test.R
import tech.clique.android.test.data.DataRepository
import tech.clique.android.test.data.Symbol
import tech.clique.android.test.ui.screen.order.OrderListAndFrom

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketListScreen(
    navController: NavController,
) {
    val tabs = listOf(
        R.string.market_list_tab_watch_list,
        R.string.market_list_tab_market,
        R.string.market_list_tab_orders,
    )
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()
    val watchList by DataRepository.watchListData.collectAsStateWithLifecycle(emptyList())
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    var orderSymbol by remember { mutableStateOf(Symbol.BTCUSDT) }

    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.getStateFlow("orderTarget", Symbol.UNKNOWN)
            ?.collect { symbol ->
                if (symbol != Symbol.UNKNOWN) {
                    orderSymbol = symbol
                    pagerState.animateScrollToPage(2, animationSpec = tween(300))
                }
                savedStateHandle.remove<Symbol>("orderTarget") // 清除状态
            }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary),
                title = { Text(stringResource(R.string.app_name)) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 0.dp,
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index, animationSpec = tween(300))
                            }
                        },
                        text = { Text(stringResource(title)) }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                when (tabs[page]) {
                    R.string.market_list_tab_watch_list -> MarketList(navController, filters = watchList)
                    R.string.market_list_tab_market -> MarketList(navController)
                    R.string.market_list_tab_orders -> OrderListAndFrom(orderSymbol)
                }
            }
        }
    }
}