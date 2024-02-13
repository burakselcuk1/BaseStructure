package com.speakwithai.basestructure.ui.wallpaper.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.speakwithai.basestructure.ui.wallpaper.detail.DetailFragmentArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.play.core.review.ReviewManagerFactory
import com.muratozturk.click_shrink_effect.applyClickShrink
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.common.utils.DownloadUtil
import com.speakwithai.basestructure.databinding.FragmentDetailBinding


class DetailFragment : BaseFragment<FragmentDetailBinding, DetailViewModel>(
    layoutId = R.layout.fragment_detail,
    viewModelClass = DetailViewModel::class.java
) {
    val args: DetailFragmentArgs by navArgs()

    override fun onInitDataBinding() {
        val photo = args.photo?.src?.original

        val progressBar = binding.prgrs

        Glide.with(requireContext())
            .load(photo)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }
            })
            .into(binding.mnwalpaper)
        setListeners()

    }

    private fun setListeners() {
        with(binding){
            backArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            download.applyClickShrink()
            download.setOnClickListener {
                downloadImage()
                val manager = ReviewManagerFactory.create(requireContext())

                val request = manager.requestReviewFlow()
                request.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val reviewInfo = task.result
                        manager.launchReviewFlow(requireActivity(), reviewInfo).addOnFailureListener {
                        }.addOnCompleteListener {
                        }
                    } else {
                    }
                }

            }
        }
    }

    private fun downloadImage() {
        args.photo?.src?.portrait?.let {
            DownloadUtil.downloadImage(requireContext(),
                it, R.string.super_ai_app_jpg.toString())
        }
    }
}