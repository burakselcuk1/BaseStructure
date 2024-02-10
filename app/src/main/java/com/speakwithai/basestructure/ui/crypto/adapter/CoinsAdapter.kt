package com.speakwithai.basestructure.ui.crypto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.databinding.ItemCoinBinding
import com.speakwithai.basestructure.ui.crypto.model.CoinUiModel


class CoinsAdapter(
    private val items: List<CoinUiModel>,
    private val onItemClickListener: ((CoinUiModel) -> Unit)? = null
) :
    RecyclerView.Adapter<CoinsAdapter.CoinViewHolder>() {

    class CoinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemCoinBinding

        init {
            binding = ItemCoinBinding.bind(view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsAdapter.CoinViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coin, parent, false)

        return CoinViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: CoinsAdapter.CoinViewHolder, position: Int) {
        val coin = items[position]

        viewHolder.binding.constraintLayout.setOnClickListener {
            onItemClickListener?.let { it -> it(coin) }
        }

        viewHolder.binding.coin = coin
    }

    override fun getItemCount() = items.size

}