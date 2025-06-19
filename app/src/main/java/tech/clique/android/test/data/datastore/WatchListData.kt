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
import tech.clique.android.test.data.Symbol
import tech.clique.android.test.data.datastore.DateStoreUtil.dataStore
import tech.clique.android.test.utils.logD

object WatchListData {
    private const val WATCH_LIST_KEY = "watch_list"
    private val WATCH_LIST_KEY_DS = stringSetPreferencesKey(WATCH_LIST_KEY)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            loadWatchList().collect { symbols ->
                _data.value = symbols
            }
        }
    }

    private suspend fun updateWatchList(list: List<Symbol>) {
        logD("updateWatchList: [${list.joinToString(",") { it.symbol }}]")
        val context = ContextApplication.getAppContext()
        context.dataStore.edit {
            it[WATCH_LIST_KEY_DS] = list.map { symbol: Symbol -> symbol.symbol }.toSet()
        }
        _data.value = list
    }

    private fun loadWatchList(): Flow<List<Symbol>> {
        val context = ContextApplication.getAppContext()
        return context.dataStore.data.map { preferences ->
            val set = preferences[WATCH_LIST_KEY_DS] ?: emptySet()
            set.map { Symbol.fromSymbol(it) }.also { list ->
                logD("loadWatchList: [${list.joinToString(",") { it.symbol }}]")
            }
        }
    }

    private val _data = MutableStateFlow<List<Symbol>>(emptyList())
    val data: StateFlow<List<Symbol>> = _data.asStateFlow()
    fun addSymbol(symbol: Symbol) {
        logD("add symbol: ${symbol.symbol}")
        if (_data.value.contains(symbol)) return
        CoroutineScope(Dispatchers.IO).launch {
            updateWatchList(_data.value + symbol)
        }
    }

    fun removeSymbol(symbol: Symbol) {
        logD("remove symbol: ${symbol.symbol}")
        if (_data.value.contains(symbol)) {
            CoroutineScope(Dispatchers.IO).launch {
                updateWatchList(_data.value - symbol)
            }
        }
    }
}