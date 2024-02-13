package com.speakwithai.basestructure.ui.wallpaper.result

import androidx.lifecycle.viewModelScope
import com.speakwithai.basestructure.base.BaseViewModel
import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.ui.wallpaper.WallpaperUseCase
import com.speakwithai.basestructure.ui.wallpaper.model.ResultUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperResultViewModel @Inject constructor(
    private val wallpaperUseCase: WallpaperUseCase

): BaseViewModel() {
    private val _serach: MutableStateFlow<Resoource<ResultUiModel>?> = MutableStateFlow(null)
    val search: MutableStateFlow<Resoource<ResultUiModel>?> = _serach



    fun getSearch(query: String){
        viewModelScope.launch {
            wallpaperUseCase.getSearchPhotos(query).collect{result->
                _serach.emit(result)
            }
        }
    }
}