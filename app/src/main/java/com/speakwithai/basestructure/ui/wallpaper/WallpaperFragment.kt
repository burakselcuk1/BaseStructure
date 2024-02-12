package com.speakwithai.basestructure.ui.wallpaper

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.speakwithai.basestructure.ui.wallpaper.model.ResultUiModel
import com.corylustech.superai.presentation.wallpaper.model.setupCategory
import com.corylustech.superai.presentation.wallpaper.model.setupColors
import com.muratozturk.click_shrink_effect.applyClickShrink
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.common.AnalyticsHelper
import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.data.model.response.wallpaper.Photo
import com.speakwithai.basestructure.databinding.FragmentWallpaperBinding
import com.speakwithai.basestructure.ui.wallpaper.adapter.CategoryAdapter
import com.speakwithai.basestructure.ui.wallpaper.adapter.CategoryClickListener
import com.speakwithai.basestructure.ui.wallpaper.adapter.EditorPicksAdapter
import com.speakwithai.basestructure.ui.wallpaper.adapter.EditorPicksClickListener
import com.speakwithai.basestructure.ui.wallpaper.adapter.TonesAdapter
import com.speakwithai.basestructure.ui.wallpaper.adapter.TonsClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WallpaperFragment : BaseFragment<FragmentWallpaperBinding, WallpaperViewModel>(
    layoutId = R.layout.fragment_wallpaper,
    viewModelClass = WallpaperViewModel::class.java
), EditorPicksClickListener, TonsClickListener, CategoryClickListener {
    private lateinit var data: ResultUiModel
   // val navigator: WallpaperNavigation = WallpaperNavigationImpl()

    override fun onInitDataBinding() {
        AnalyticsHelper.logScreenView("WallpaperFragment","WallpaperFragment",requireContext())
      //  navigator.bind(findNavController())
        setObservers()
        viewModel.getCurated()
        setTonesRecyclerview()
        setListeners()

    }

    private fun setListeners() {
        with(binding){
            backArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
            backArrow.applyClickShrink()
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setTonesRecyclerview() {
        with(binding){
            colorsTones.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            val adapter = TonesAdapter(setupColors())
            adapter.setClickListener(this@WallpaperFragment)
            colorsTones.adapter = adapter


            categories.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            val adapterCategory = CategoryAdapter(setupCategory())
            adapterCategory.setClickListener(this@WallpaperFragment)

            categories.adapter = adapterCategory

        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.curated.collect { resources ->
                when (resources) {
                    is Resoource.Loading -> {
                        binding.editorPickProgressBara.visibility = View.VISIBLE
                    }

                    is Resoource.Success -> {
                        data = resources.data

                        with(binding) {
                            binding.editorPickProgressBara.visibility = View.GONE
                            editorPicks.layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            val adapter = EditorPicksAdapter(data.photos)
                            adapter.setClickListener(this@WallpaperFragment)
                            editorPicks.adapter = adapter
                        }
                    }

                    is Resoource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            R.string.unexpected_error,
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    else -> {

                    }
                }
            }
        }
    }

    override fun editorPicksItemClick(photo: Photo) {

     //   navigator.navigateToWallpaperDetailFragment(photo)

    }

    override fun tonsItemClick(url: String) {
       // navigator.navigateToWallpaperResultFragment(url)
    }

    override fun categoryItemClick(url: String) {
       // navigator.navigateToWallpaperResultFragment(url)
    }
}