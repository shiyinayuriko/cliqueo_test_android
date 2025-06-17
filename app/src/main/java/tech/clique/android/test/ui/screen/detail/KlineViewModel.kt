package tech.clique.android.test.ui.screen.detail

import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import tech.clique.android.test.data.DataRepository
import tech.clique.android.test.data.model.KlineData
import tech.clique.android.test.data.network.binance.KlineInterval
import tech.clique.android.test.data.network.binance.Symbol

class KlineViewModel(symbol: String, interval: String) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private var baseTime = 0L
    private var intervalTime = 1L

    init {
        DataRepository.fetchKline(symbol, interval)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { klines ->
                    allKlines.clear()

                    val sorted = klines.values.sortedBy { kline -> kline.openTime }

                    baseTime = sorted.firstOrNull()?.openTime ?: 0
                    //TODO can set with const
                    intervalTime = if (sorted.size >= 2) sorted[1].openTime - sorted[0].openTime else 1

                    val keyTrimmed = sorted.map { kline ->
                        val index = (kline.openTime - baseTime) / intervalTime
                        index to kline
                    }
                    allKlines.putAll(keyTrimmed)
                    _items.value = allKlines.toMap()
                }, {

                }
            ).also { compositeDisposable.add(it) }

        DataRepository.subscribeKline(symbol, interval)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { kline ->
                    val index = (kline.openTime - baseTime) / intervalTime
                    val isNew = allKlines.contains(index)
                    allKlines[index] = kline
                    viewModelScope.launch {
                        _updateEvent.emit(KlineUpdateEvent(isNew, index, kline))
                    }
                }, { e ->

                }).also { compositeDisposable.add(it) }
    }


    private val allKlines = mutableMapOf<Long, KlineData>()
    private val _items = MutableLiveData<Map<Long, KlineData>>(mapOf())
    val items: LiveData<Map<Long, KlineData>> = _items

    private val _updateEvent = MutableSharedFlow<KlineUpdateEvent>()
    val updateEvent = _updateEvent.asSharedFlow()


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

@Composable
fun klineViewModel(@Symbol symbol: String, @KlineInterval interval: String): KlineViewModel {
    return viewModel(
        key = "klineVM_${symbol}_$interval",
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return KlineViewModel(symbol, interval) as T
            }
        },
    )
}

data class KlineUpdateEvent(
    val isNew: Boolean,
    val index: Long,
    val klineData: KlineData,
)