package com.speakwithai.basestructure.ui.cryptoNews.adapter


import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.common.DateConverter
import com.speakwithai.basestructure.data.model.response.cryptoNews.NewsData
import com.speakwithai.basestructure.databinding.RvNewsItemBinding


class NewsRVAdapter(private var newsList : List<NewsData>) : RecyclerView.Adapter<NewsRVAdapter.NewsViewHolder>() {


    class NewsViewHolder(val binding: RvNewsItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvNewsItemBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = newsList[position]

        holder.binding.tvNewsTitle.text = currentItem.title
        holder.binding.tvNewsBody.text = currentItem.body
        holder.binding.tvSourceName.text = currentItem.sourceInfo?.name

        if (!currentItem.sourceInfo?.img.isNullOrEmpty()) {
            Glide.with(holder.binding.root.context).load(currentItem.sourceInfo?.img)
                .into(holder.binding.ivSourceImage)
        } else {
            Glide.with(holder.binding.root.context).load(R.drawable.logg).into(holder.binding.ivSourceImage)
        }

        holder.binding.tvNewsTime.text = DateConverter.getTimeAgo(currentItem.publishedOn!!.toLong())

        holder.binding.rlRootNewsItem.setOnClickListener {
            val bundle = Bundle().apply {
                putString("url", currentItem.url)
            }
            it.findNavController().navigate(R.id.action_cryptoNewsFragment_to_newsDetailFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }
}

