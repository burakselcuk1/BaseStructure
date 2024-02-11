package com.speakwithai.basestructure.ui.crypto.cryptoDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.speakwithai.basestructure.data.model.response.coin.CoinChartResponse
import com.speakwithai.basestructure.ui.crypto.model.CoinUiModel
import com.speakwithai.basestructure.ui.crypto.repository.CoinsRepository
import com.speakwithai.basestructure.ui.crypto.utilities.Response
import com.speakwithai.basestructure.ui.crypto.utilities.handleIfNotSuccessful
import com.speakwithai.basestructure.ui.crypto.utilities.handleNetworkCall
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject


@HiltViewModel
class CryptoDetailViewModel  @Inject constructor(
    private val coinsRepository: CoinsRepository
): ViewModel() {
    private val _coinChartData = MutableLiveData<Response<CoinChartResponse>>()
    val coinChartData: LiveData<Response<CoinChartResponse>> = _coinChartData

    private lateinit var _coinApiModel: CoinUiModel
    var coinApiModel: CoinUiModel
        get() = _coinApiModel
        set(value) {
            _coinApiModel = value
            currentPrice = value.current_price
            fetchCoinChartData()
        }
    private var selectedPeriod = "1"
    private val decimalFormat = DecimalFormat("#.##########")
    private var currentPrice = -1f;

    private var _isFavourite = MutableLiveData(false)
    val isFavourite: LiveData<Boolean> = _isFavourite

    private var _isWatchlisted = MutableLiveData(false)
    val isWatchlisted: LiveData<Boolean> = _isWatchlisted

    private var _priceToDisplay = MutableLiveData("0")
    val priceToDisplay: LiveData<String> = _priceToDisplay

    private var _priceChangePercentage = MutableLiveData("0%")
    val priceChangePercentage: LiveData<String> = _priceChangePercentage

    private var _priceChangePercentageGrowth = MutableLiveData(true)
    val priceChangePercentageGrowth: LiveData<Boolean> = _priceChangePercentageGrowth

    fun fetchCoinChartData() = viewModelScope.launch {
        _coinChartData.postValue(Response.Waiting())

        handleNetworkCall(_coinChartData) {
            val data = coinsRepository.getCoinChartData(coinApiModel.id, selectedPeriod)

            if (!handleIfNotSuccessful(data, _coinChartData)) {
                return@handleNetworkCall
            }

            _coinChartData.postValue(Response.Success(data.body()!!))

            val percent = (data.body()!!.prices.last()[1] / data.body()!!.prices.first()[1] - 1) * 100
            _priceChangePercentageGrowth.postValue(percent>=0)
            _priceChangePercentage.postValue(String.format("%.2f %%", percent))

            if (currentPrice == -1f) {
                currentPrice = data.body()!!.prices.last()[1]
            }

            displayPrice(currentPrice)
        }
    }



    fun displayPrice(price: Float) {
        val decimalPlaces = if (price < 1) 8 else if(price < 10) 4 else 2
        decimalFormat.maximumFractionDigits = decimalPlaces
        this._priceToDisplay.postValue(decimalFormat.format(price))
    }

    fun displayDefaultPrice() {
        this._coinChartData.value?.let {
            displayPrice(currentPrice)
        }
    }


    fun toggleSelectedPeriod(period: Int) {
        if (selectedPeriod == period.toString()) {
            return
        }
        selectedPeriod = period.toString()
        fetchCoinChartData()
    }

    fun toggleSelectedPeriodToMax() {
        if (selectedPeriod == "max") {
            return
        }
        selectedPeriod = "max"
        fetchCoinChartData()
    }





}