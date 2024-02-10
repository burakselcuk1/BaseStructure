package com.speakwithai.basestructure.data.model.response.coin

import androidx.room.Entity


@Entity(
    tableName = "coins",
    primaryKeys = ["coinId", "type"]
)
data class StoredCoin(
    val coinId: String,
    val type: StoredCoinType
)