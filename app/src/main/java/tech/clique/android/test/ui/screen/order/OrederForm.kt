package tech.clique.android.test.ui.screen.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.clique.android.test.R
import tech.clique.android.test.data.DataRepository
import tech.clique.android.test.data.Symbol
import tech.clique.android.test.data.model.OrderData
import tech.clique.android.test.data.model.OrderType
import tech.clique.android.test.data.model.OrderType.ORDER_TYPE_LIMIT
import tech.clique.android.test.data.model.OrderType.ORDER_TYPE_MARKET
import tech.clique.android.test.ui.screen.common.FloatInputTextField
import tech.clique.android.test.ui.screen.market.MarketListViewModel
import tech.clique.android.test.ui.theme.DecreasingColor
import tech.clique.android.test.ui.theme.IncreasingColor
import tech.clique.android.test.ui.toDisplayName

@Composable
fun OrderForm(
    modifier: Modifier = Modifier,
    defaultSymbol: Symbol = Symbol.UNKNOWN
) {
    val marketListViewModel = viewModel<MarketListViewModel>()
    val isBuy = remember { mutableStateOf(true) }
    val orderType = remember { mutableStateOf(ORDER_TYPE_LIMIT) }
    val symbol = remember { mutableStateOf(defaultSymbol) }
    var defaultPrice by remember(symbol) {
        val priceStr = marketListViewModel.items.value?.get(symbol.value)?.price
        mutableFloatStateOf(priceStr?.toFloat() ?: 0f)
    }
    val currentPrice = remember(defaultPrice) { mutableFloatStateOf(defaultPrice) }

    val amount = remember { mutableFloatStateOf(0f) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OrderFormSymbolsMenu(symbol)
        OrderFormIsBuyButton(isBuy)
        OrderFormOrderTypeMenu(orderType)
        FloatInputTextField(defaultPrice, currentPrice, stringResource(R.string.order_form_order_price_label, symbol.value.quoteAsset))
        FloatInputTextField(amount.floatValue, amount, stringResource(R.string.order_form_order_amount_label, symbol.value.baseAsset))
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            enabled = symbol.value != Symbol.UNKNOWN && amount.floatValue != 0f,
            onClick = {
                val order = OrderData(
                    symbol = symbol.value,
                    amount = amount.floatValue,
                    price = currentPrice.floatValue,
                    isBuy = isBuy.value,
                    type = orderType.value
                )
                DataRepository.orderList.addOrder(order)

                val priceStr = marketListViewModel.items.value?.get(symbol.value)?.price
                defaultPrice = priceStr?.toFloat() ?: 0f
                amount.floatValue = 0f
                keyboardController?.hide()
            }
        ) {
            Text("submit")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderFormSymbolsMenu(symbol: MutableState<Symbol>) {
    val items = remember { Symbol.entries.filter { it != Symbol.UNKNOWN } }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor(PrimaryNotEditable)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            readOnly = true,
            value = symbol.value.toDisplayName(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,  // 聚焦时透明
                unfocusedIndicatorColor = Color.Transparent,  // 未聚焦时透明
                disabledIndicatorColor = Color.Transparent   // 禁用时透明
            ),
            onValueChange = {},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { s ->
                DropdownMenuItem(
                    text = {
                        Text(s.toDisplayName())
                    },
                    onClick = {
                        symbol.value = s
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun OrderFormIsBuyButton(
    isBuy: MutableState<Boolean>,
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth(),
    ) {
        SegmentedButton(
            modifier = Modifier
                .defaultMinSize(minHeight = 0.dp)
                .fillMaxWidth()
                .weight(1f),
            shape = SegmentedButtonDefaults.itemShape(
                index = 0, count = 2, baseShape = MaterialTheme.shapes.small
            ),
            selected = isBuy.value,
            onClick = { isBuy.value = true },
            colors = SegmentedButtonDefaults.colors(
                activeContainerColor = IncreasingColor,
                activeContentColor = MaterialTheme.colorScheme.onPrimary,
                activeBorderColor = IncreasingColor,
                inactiveContentColor = IncreasingColor,
                inactiveBorderColor = IncreasingColor,
            ),
            icon = {}
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(R.string.order_form_buy)
            )
        }
        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(
                index = 1, count = 2, baseShape = MaterialTheme.shapes.small
            ),
            selected = !isBuy.value,
            onClick = { isBuy.value = false },
            colors = SegmentedButtonDefaults.colors(
                activeContainerColor = DecreasingColor,
                activeContentColor = MaterialTheme.colorScheme.onPrimary,
                activeBorderColor = DecreasingColor,
                inactiveContentColor = DecreasingColor,
                inactiveBorderColor = DecreasingColor,
            ),
            icon = {}
        ) {
            Box(
                modifier = Modifier
                    .size(width = 50.dp, height = 20.dp),
            ) {
                Text(text = stringResource(R.string.order_form_sell))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderFormOrderTypeMenu(orderType: MutableState<OrderType>) {
    @Composable
    fun OrderType.displayName(): String {
        return stringResource(
            when (this) {
                ORDER_TYPE_LIMIT -> R.string.order_form_order_type_limit
                ORDER_TYPE_MARKET -> R.string.order_form_order_type_market
            }
        )
    }

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor(PrimaryNotEditable)
                .fillMaxWidth(),
            readOnly = true,
            value = orderType.value.displayName(),
            onValueChange = {},
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,  // 聚焦时透明
                unfocusedIndicatorColor = Color.Transparent,  // 未聚焦时透明
                disabledIndicatorColor = Color.Transparent   // 禁用时透明
            ),
//            label = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
//            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(ORDER_TYPE_LIMIT.displayName())
                },
                onClick = {
                    orderType.value = ORDER_TYPE_LIMIT
                    expanded = false
                },
//                can add icon here
//                leadingIcon = {}
            )
            DropdownMenuItem(
                text = {
                    Text(ORDER_TYPE_MARKET.displayName())
                },
                onClick = {
                    orderType.value = ORDER_TYPE_MARKET
                    expanded = false
                },
//                can add icon here
//                leadingIcon = {}
            )
        }
    }
}

