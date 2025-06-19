package tech.clique.android.test.ui.screen.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tech.clique.android.test.R
import tech.clique.android.test.data.DataRepository
import tech.clique.android.test.data.model.OrderData
import tech.clique.android.test.ui.toDisplayName

@Composable
fun OrderList() {
    val orderList by DataRepository.orderListData.collectAsStateWithLifecycle(emptyList())
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(12.dp),

        ) {
        items(
            items = orderList,
            key = { order -> order.id },
        ) { order ->
            OrderItem(order)
        }
    }
}

@Composable
fun OrderItem(order: OrderData) {
    Row(
        modifier = Modifier
            .heightIn(min = 50.dp)
            .padding(horizontal = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.background
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            modifier = Modifier,
            text = order.symbol.toDisplayName(),
            style = MaterialTheme.typography.titleMedium
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)

        ) {
            Text(
                modifier = Modifier,
                text = stringResource(R.string.order_list_item_amount, order.amount),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                modifier = Modifier,
                text = stringResource(R.string.order_list_item_price, order.price),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Button(
            modifier = Modifier,
            shape = MaterialTheme.shapes.small,
            onClick = {
                DataRepository.orderList.removeOrder(order)
            }
        ) { Text("delete") }
    }
}