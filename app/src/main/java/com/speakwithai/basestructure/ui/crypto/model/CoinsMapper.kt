package com.speakwithai.basestructure.ui.crypto.model

import com.speakwithai.basestructure.data.model.response.coin.CoinApiModel

class CoinsMapper {


    fun  coinMapToUiModel(apiModel: CoinApiModel): CoinUiModel {
        return CoinUiModel(
            name = apiModel.name,
            id = apiModel.id,
            current_price = apiModel.current_price,
            image = apiModel.image,
            market_cap_rank = apiModel.market_cap_rank,
            symbol = apiModel.symbol
        )
    }

    fun coinListMapToUiModelList(apiModelList: List<CoinApiModel>): List<CoinUiModel> {
        return apiModelList.map { coinMapToUiModel(it) }
    }

}