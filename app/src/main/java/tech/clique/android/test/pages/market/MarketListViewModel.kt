package tech.clique.android.test.pages.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import tech.clique.android.test.data.DataRepository
import tech.clique.android.test.data.TickerData


class MarketListViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    init {
        DataRepository.fetchTickers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { tickerList ->
                    tickerList.forEach { ticker ->
                        allTickersMap[ticker.symbol] = ticker
                    }
                    _items.value = allTickersMap.toMap()
                }, {

                }
            ).also { compositeDisposable.add(it) }

        DataRepository.subscribeTickers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { ticker ->
                    allTickersMap[ticker.symbol] = ticker
                    _items.value = allTickersMap.toMap()
                }, { e ->

                }).also { compositeDisposable.add(it) }
    }

    private val allTickersMap = mutableMapOf<String, TickerData>()
    private val _items = MutableLiveData<Map<String, TickerData>>(mapOf())
    val items: LiveData<Map<String, TickerData>> = _items

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}