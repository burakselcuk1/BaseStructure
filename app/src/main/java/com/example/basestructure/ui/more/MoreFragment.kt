package com.example.basestructure.ui.more

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.basestructure.R
import com.example.basestructure.base.BaseFragment
import com.example.basestructure.databinding.FragmentMoreBinding

class MoreFragment : BaseFragment<FragmentMoreBinding,MoreViewModel>(
    layoutId = R.layout.fragment_more,
    viewModelClass = MoreViewModel::class.java
) {
    override fun onInitDataBinding() {

    }
}