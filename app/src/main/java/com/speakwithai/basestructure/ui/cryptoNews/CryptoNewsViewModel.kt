package com.speakwithai.basestructure.ui.cryptoNews

import androidx.lifecycle.viewModelScope
import com.speakwithai.basestructure.base.BaseViewModel
import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.ui.cryptoNews.model.NewsUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CryptoNewsViewModel @Inject constructor(
    private val useCase: CryptoNewsUseCase
): BaseViewModel() {

    private val _coinNewsResponse = MutableStateFlow<Resoource<NewsUiModel>?>(null)
    val coinNewsResponse: MutableStateFlow<Resoource<NewsUiModel>?> = _coinNewsResponse

    fun getCoinNews() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getCoinNews().collect { result ->
                _coinNewsResponse.emit(result)
            }
        }
    }
}