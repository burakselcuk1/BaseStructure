package com.speakwithai.basestructure.ui.crypto.cryptoHome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.speakwithai.basestructure.ui.crypto.utilities.Response
import com.speakwithai.basestructure.base.BaseViewModel
import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.data.model.response.coin.CoinApiModel
import com.speakwithai.basestructure.ui.crypto.cryptoMain.CoinUseCase
import com.speakwithai.basestructure.ui.crypto.model.CoinUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoHomePageViewModel @Inject constructor(
    private val coinsUseCase: CoinUseCase,
): BaseViewModel() {

    private lateinit var _allCoinApiModels: MutableList<CoinUiModel>

    private val _coinToDisplayFlow: MutableStateFlow<Resoource<List<CoinUiModel>>> = MutableStateFlow(Resoource.Loading)
    val coinToDisplayFlow: MutableStateFlow<Resoource<List<CoinUiModel>>> = _coinToDisplayFlow

    private val _coinsToDisplay = MutableLiveData<Response<List<CoinUiModel>>>()
    val coinsToDisplay: LiveData<Response<List<CoinUiModel>>> = _coinsToDisplay

    private val _favouriteCoins = MutableLiveData<List<CoinApiModel>>()
    val favouriteCoins: LiveData<List<CoinApiModel>> = _favouriteCoins

    var selectedAvailableCoins = MutableLiveData(true)

    init {
        fetchCoins()
    }

    fun toggleAvailableCoinsList(value: Boolean) {
        if (selectedAvailableCoins.value == value) {
            return
        }
        selectedAvailableCoins.postValue(value)

        displayCoinsInList(value)
    }



    fun fetchCoins() = viewModelScope.launch {
        viewModelScope.launch {
            coinsUseCase.getCoins().collect{ result ->

                when(result){
                    is Resoource.Success->{
                        _coinToDisplayFlow.value = result
                    }
                    else -> {

                    }
                }
            }

        }
    }

    private fun displayCoinsInList(displayAvailableCoins: Boolean? = null) {
        if (displayAvailableCoins == true) {
            this._coinsToDisplay.postValue(Response.Success(_allCoinApiModels))
        } else {
            viewModelScope.launch {
            }
        }
    }
}