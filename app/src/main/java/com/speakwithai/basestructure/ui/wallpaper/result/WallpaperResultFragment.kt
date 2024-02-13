package com.speakwithai.basestructure.ui.wallpaper.result

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.data.model.response.wallpaper.Photo
import com.speakwithai.basestructure.databinding.FragmentWallpaperResultBinding
import com.speakwithai.basestructure.ui.wallpaper.adapter.ResultAdapter
import com.speakwithai.basestructure.ui.wallpaper.adapter.ResultClickListener
import com.speakwithai.basestructure.ui.wallpaper.model.ResultUiModel
import com.speakwithai.basestructure.ui.wallpaper.navigation.WallpaperNavigation
import com.speakwithai.basestructure.ui.wallpaper.navigation.WallpaperNavigationImpl
import kotlinx.coroutines.launch


class WallpaperResultFragment : BaseFragment<FragmentWallpaperResultBinding, WallpaperResultViewModel>(
    layoutId = R.layout.fragment_wallpaper_result,
    viewModelClass = WallpaperResultViewModel::class.java
), ResultClickListener {
    val args: WallpaperResultFragmentArgs by navArgs()
    private lateinit var data: ResultUiModel
    private lateinit var adapter: ResultAdapter
    val navigator: WallpaperNavigation = WallpaperNavigationImpl()

    override fun onInitDataBinding() {
        navigator.bind(findNavController())
        val photo = args.url
        viewModel.getSearch(photo.toString())
        adapter = ResultAdapter(ArrayList())


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.search.collect{resources->
                when(resources){
                    is Resoource.Loading->{

                    }
                    is Resoource.Success->{
                        data = resources.data

                        with(binding){
                            resultRecyclerview.layoutManager = GridLayoutManager(requireContext(), 2)
                            adapter.setresult(data.photos)
                            adapter.setClickListener(this@WallpaperResultFragment)
                            adapter.notifyDataSetChanged()
                            resultRecyclerview.adapter = adapter
                        }
                    }

                    else -> {}
                }
            }
        }
        reflesh()
        search()
    }

    private fun search() {
        with(binding) {
            userSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.getSearch(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
    }


    private fun reflesh() {
        with(binding){
            val photo = args.url
            pulltorefresh.setOnRefreshListener {
                viewModel.getSearch(photo.toString())

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.search.collect { resources ->
                        when (resources) {
                            is Resoource.Success -> {
                                data = resources.data

                                val adapter = ResultAdapter(data.photos)
                                resultRecyclerview.adapter = adapter
                                adapter.notifyDataSetChanged()
                                pulltorefresh.isRefreshing = false

                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }

    override fun editorResultItemClick(photo: Photo) {
        navigator.navigateToWallpaperDetailFragment(photo)
    }
}