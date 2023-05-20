package com.example.basestructure.ui.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basestructure.R
import com.example.basestructure.base.BaseFragment
import com.example.basestructure.databinding.FragmentExploreBinding
import com.example.basestructure.model.Child
import com.example.basestructure.model.Parent
import com.example.basestructure.ui.explore.adapter.MyAdapter

class ExploreFragment : BaseFragment<FragmentExploreBinding, ExploreFragmentViewModel>(
    layoutId = R.layout.fragment_explore,
    viewModelClass = ExploreFragmentViewModel::class.java
) {
    override fun onInitDataBinding() {
        val child1 = Child("Ev Kiralama")
        val child2 = Child("Bilet Alma")
        val child3 = Child("Restoran Tevsiyesi")
        val child4 = Child("Gezilecek Yerler")

        val children = listOf(child1, child2, child3, child4)

        val parent1 = ResourcesCompat.getDrawable(resources, R.drawable.logo, null)
            ?.let { Parent(it, "Seyehat Ve Keşif", children) }
        val parent2 = ResourcesCompat.getDrawable(resources, R.drawable.logo, null)
            ?.let { Parent(it, "Parent 2", children) }

        val parents = parent1?.let { listOf(it, parent2) } ?: listOf()

        val adapter = MyAdapter(parents.filterNotNull())

        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView2.adapter = adapter
    }


}