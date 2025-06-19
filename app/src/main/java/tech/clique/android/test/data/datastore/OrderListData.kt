package tech.clique.android.test.data.datastore

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import tech.clique.android.test.application.ContextApplication
import tech.clique.android.test.data.datastore.DateStoreUtil.dataStore
import tech.clique.android.test.data.model.OrderData
import tech.clique.android.test.utils.GsonUtil
import tech.clique.android.test.utils.logD

object OrderListData {
    private const val ORDER_LIST_KEY = "order_list"
    private val ORDER_LIST_KEY_DS = stringSetPreferencesKey(ORDER_LIST_KEY)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            loadOrderList().collect { orderDataList ->
                _data.value = orderDataList
            }
        }
    }

    private suspend fun updateOrderList(list: List<OrderData>) {
        logD("updateOrderList: [${list.joinToString(",") { it.toString() }}]")
        val context = ContextApplication.getAppContext()
        context.dataStore.edit {
            it[ORDER_LIST_KEY_DS] = list.mapNotNull { order ->
                GsonUtil.toJson(order)
            }.toSet()
        }
        _data.value = list
    }

    private fun loadOrderList(): Flow<List<OrderData>> {
        val context = ContextApplication.getAppContext()
        return context.dataStore.data.map { preferences ->
            val set = preferences[ORDER_LIST_KEY_DS] ?: emptySet()
            set.map { GsonUtil.fromJson(it, OrderData::class.java) }.also { list ->
                logD("loadOrderList: [${list.joinToString(",") { it.toString() }}]")
            }
        }
    }

    private val _data = MutableStateFlow<List<OrderData>>(emptyList())
    val data: StateFlow<List<OrderData>> = _data.asStateFlow()
    fun addOrder(order: OrderData) {
        logD("add OrderData: $order")
        CoroutineScope(Dispatchers.IO).launch {
            updateOrderList(_data.value + order)
        }
    }

    fun removeOrder(order: OrderData) {
        logD("remove OrderData: $order")
        CoroutineScope(Dispatchers.IO).launch {
            updateOrderList(_data.value.filter { it.id != order.id })
        }
    }
}