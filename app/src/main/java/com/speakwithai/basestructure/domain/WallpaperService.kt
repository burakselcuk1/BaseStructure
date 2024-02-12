package com.speakwithai.basestructure.domain

import com.speakwithai.basestructure.common.api.WallpaperEndPoints
import com.speakwithai.basestructure.data.model.response.wallpaper.ResultApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface WallpaperService {

    @GET(WallpaperEndPoints.GET_CURATED_PHOTOS)
    suspend fun getCuratedPhotos(): ResultApiModel

    @GET(WallpaperEndPoints.GET_SEARCH_PHOTOS)
    suspend fun searchPhotos(@Query("query") query: String): ResultApiModel

}