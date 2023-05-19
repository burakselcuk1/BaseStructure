package com.example.basestructure.ui.mainFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.basestructure.R
import com.example.basestructure.base.BaseFragment
import com.example.basestructure.base.BaseViewModel
import com.example.basestructure.databinding.FragmentMainBinding


class MainFragment : BaseFragment<FragmentMainBinding, MainFragmentViewModel>(
    layoutId = R.layout.fragment_main,
    viewModelClass = MainFragmentViewModel::class.java
) {
    override fun onInitDataBinding() {

    }


}