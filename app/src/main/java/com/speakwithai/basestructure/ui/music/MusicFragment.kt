package com.speakwithai.basestructure.ui.music

import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.base.BaseFragment
import com.speakwithai.basestructure.common.AnalyticsHelper
import com.speakwithai.basestructure.common.Resoource
import com.speakwithai.basestructure.databinding.FragmentMusicBinding
import com.speakwithai.basestructure.ui.music.adapter.SongAdapter
import kotlinx.coroutines.launch


class MusicFragment : BaseFragment<FragmentMusicBinding, MusicViewModel>(
    layoutId = R.layout.fragment_music,
    viewModelClass = MusicViewModel::class.java
) {
    private var mymediaplayer = MediaPlayer()
    lateinit var editoradapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editoradapter = SongAdapter(requireContext(), mymediaplayer)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onInitDataBinding() {
        AnalyticsHelper.logScreenView("MusicFragment","MusicFragment",requireContext())
        viewModel.getMusic()
        setObservers()
        editoradapter = SongAdapter(requireContext(), mymediaplayer)
        search()
        setListeners()

    }

    private fun setListeners() {
        with(binding){
            backArrow.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun search() {
        binding.search1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString()
                viewModel.getSearchMusic(searchText, "song")

                if (searchText.isNullOrEmpty()) {
                    setObservers()
                    editoradapter.notifyDataSetChanged()
                    Toast.makeText(requireContext(), "bos", Toast.LENGTH_SHORT).show()
                } else {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.searchMusic.collect { resources ->
                            when (resources) {
                                is Resoource.Success -> {
                                    editoradapter.setmlist(resources.data.results)
                                    editoradapter.notifyDataSetChanged()
                                    binding.songRecyclerview.adapter = editoradapter
                                }

                                else -> {

                                }
                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Değişiklik sonrası durumu ele alabilirsiniz
            }
        })
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.music.collect { resources ->
                when (resources) {
                    is Resoource.Success -> {
                        with(binding) {
                            songRecyclerview.layoutManager = LinearLayoutManager(requireContext())
                            editoradapter.setmlist(resources.data.results)
                            editoradapter.notifyDataSetChanged()
                            songRecyclerview.adapter = editoradapter
                            loading.visibility = View.GONE

                        }
                    }

                    else -> {

                    }
                }
            }
        }
    }
}