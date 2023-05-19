package com.example.basestructure.ui.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.basestructure.R
import com.example.basestructure.base.BaseFragment
import com.example.basestructure.databinding.FragmentExploreBinding

class ExploreFragment : BaseFragment<FragmentExploreBinding, ExploreFragmentViewModel>(
    layoutId = R.layout.fragment_explore,
    viewModelClass = ExploreFragmentViewModel::class.java
) {
    override fun onInitDataBinding() {

    }


}