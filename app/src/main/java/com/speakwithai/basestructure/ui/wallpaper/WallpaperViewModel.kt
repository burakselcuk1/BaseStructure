package com.speakwithai.basestructure.ui.wallpaper

import androidx.lifecycle.viewModelScope
import com.speakwithai.basestructure.ui.wallpaper.model.ResultUiModel
import com.speakwithai.basestructure.base.BaseViewModel
import com.speakwithai.basestructure.common.Resoource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(
    private val wallpaperUseCase: WallpaperUseCase
): BaseViewModel() {

    private val _curated:MutableStateFlow<Resoource<ResultUiModel>?> = MutableStateFlow(null)
    val curated:MutableStateFlow<Resoource<ResultUiModel>?> = _curated

    fun getCurated(){
        viewModelScope.launch {
            wallpaperUseCase.getCuratedPhotos().collect { result ->
                _curated.emit(result)
            }
        }
    }
}