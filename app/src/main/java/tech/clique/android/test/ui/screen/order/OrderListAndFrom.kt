package tech.clique.android.test.ui.screen.order

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import tech.clique.android.test.data.Symbol

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun OrderListAndFrom(
    targetSymbol: Symbol
) {
    val configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .border(
                        width = 0.5.dp,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                OrderList()
            }
            OrderForm(
                Modifier.padding(top = 6.dp, bottom = 10.dp, start = 16.dp, end = 20.dp),
                targetSymbol
            )
        }
    } else {
        Row {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .border(
                        width = 0.5.dp,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                OrderList()
            }
            OrderForm(
                Modifier
                    .padding(top = 6.dp, bottom = 10.dp, start = 16.dp, end = 20.dp)
                    .fillMaxWidth(.5f)
                    .verticalScroll(rememberScrollState()),
                targetSymbol
            )
        }
    }
}