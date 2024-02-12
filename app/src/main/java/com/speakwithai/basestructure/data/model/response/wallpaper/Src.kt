package com.speakwithai.basestructure.data.model.response.wallpaper

import java.io.Serializable

data class Src(
    val landscape: String,
    val large: String,
    val large2x: String,
    val medium: String,
    val original: String,
    val portrait: String,
    val small: String,
    val tiny: String
): Serializable
