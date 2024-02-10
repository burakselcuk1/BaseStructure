package com.speakwithai.basestructure.data.model.response.cryptoNews

import com.google.gson.annotations.SerializedName

data class NewsData(
    @SerializedName("id")
    val id: String,

    @SerializedName("guid")
    val guid: String,

    @SerializedName("published_on")
    val publishedOn: Int,

    @SerializedName("imageurl")
    val imageUrl: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("body")
    val body: String,

    @SerializedName("tags")
    val tags: String,

    @SerializedName("lang")
    val lang: String,

    @SerializedName("upvotes")
    val upvotes: String,

    @SerializedName("downvotes")
    val downvotes: String,

    @SerializedName("categories")
    val categories: String,

    @SerializedName("source_info")
    val sourceInfo: SourceInfo,

    @SerializedName("source")
    val source: String
)

data class SourceInfo(
    @SerializedName("name")
    val name: String,

    @SerializedName("img")
    val img: String,

    @SerializedName("lang")
    val lang: String
)
