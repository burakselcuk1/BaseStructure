package com.speakwithai.basestructure.ui.music

import androidx.lifecycle.viewModelScope
import com.speakwithai.basestructure.base.BaseViewModel
import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.ui.music.model.SongSampleUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val useCase: MusicUseCase
): BaseViewModel() {

    private var _music : MutableStateFlow<Resoource<SongSampleUiModel>?> = MutableStateFlow(null)
    var music : MutableStateFlow<Resoource<SongSampleUiModel>?> = _music

    private var _searchMusic : MutableStateFlow<Resoource<SongSampleUiModel>?> = MutableStateFlow(null)
    var searchMusic : MutableStateFlow<Resoource<SongSampleUiModel>?> = _searchMusic


    fun getMusic(){
        viewModelScope.launch {
            useCase.getMusic().collect { result ->
                _music.emit(result)
            }
        }
    }

    fun getSearchMusic(query: String, entity: String){
        viewModelScope.launch {
            useCase.getSearchMusic(query,entity).collect{result ->
                _searchMusic.emit(result)
            }
        }
    }

}