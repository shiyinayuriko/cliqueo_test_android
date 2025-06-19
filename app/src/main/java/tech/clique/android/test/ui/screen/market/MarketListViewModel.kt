package tech.clique.android.test.ui.screen.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import tech.clique.android.test.data.DataRepository
import tech.clique.android.test.data.Symbol
import tech.clique.android.test.data.model.TickerData
import tech.clique.android.test.ui.screen.common.ViewStatus
import tech.clique.android.test.ui.screen.common.ViewStatus.StatusComplete
import tech.clique.android.test.ui.screen.common.ViewStatus.StatusError
import tech.clique.android.test.ui.screen.common.ViewStatus.StatusLoading
import tech.clique.android.test.utils.logD
import tech.clique.android.test.utils.logE


class MarketListViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val allTickersMap = mutableMapOf<Symbol, TickerData>()
    private val _items = MutableLiveData<Map<Symbol, TickerData>>(mapOf())
    val items: LiveData<Map<Symbol, TickerData>> = _items

    private val _loadingStatus = MutableStateFlow<ViewStatus>(ViewStatus.StatusIdle)
    val loadingStatus: StateFlow<ViewStatus> = _loadingStatus.asStateFlow()


    init {
        DataRepository.fetchTickers()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{
                _loadingStatus.value = StatusLoading()
            }
            .subscribe(
                { tickerList ->
                    tickerList.forEach { ticker ->
                        allTickersMap[ticker.symbol] = ticker
                    }
                    _items.value = allTickersMap.toMap()
                    _loadingStatus.value = StatusComplete
                    logD("fetchTickers success ${tickerList.size}" )
                }, { e ->
                    logE("fetchTickers fail" , e)
                    _loadingStatus.value = StatusError("Load Market List fail:\n${e.message}")
                }
            ).also { compositeDisposable.add(it) }

        DataRepository.subscribeTickers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { ticker ->
                    allTickersMap[ticker.symbol] = ticker
                    _items.value = allTickersMap.toMap()
                }, { e ->
                    logE("subscribeTickers fail" , e)
                }).also { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}