package com.example.basestructure.ui.explore

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController

import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basestructure.R
import com.example.basestructure.base.BaseFragment
import com.example.basestructure.databinding.FragmentExploreBinding
import com.example.basestructure.model.Child
import com.example.basestructure.model.Parent
import com.example.basestructure.ui.explore.adapter.DatabaseAdapter
import com.example.basestructure.ui.explore.adapter.MyAdapter

class ExploreFragment : BaseFragment<FragmentExploreBinding, ExploreFragmentViewModel>(
    layoutId = R.layout.fragment_explore,
    viewModelClass = ExploreFragmentViewModel::class.java
) {
    private lateinit var databaseAdapter: DatabaseAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onInitDataBinding() {
        val child1 = Child(getString(R.string.house_rent_short))
        val child2 = Child(getString(R.string.take_ticket_short))
        val child3 = Child(getString(R.string.suggest_restaurant_short))
        val child4 = Child(getString(R.string.travel_short))


        val child5 = Child(getString(R.string.name_crator_short))
        val child6 = Child(getString(R.string.suggest_relationship_short))
        val child7 = Child(getString(R.string.suggest_title_short))
        val child8 = Child(getString(R.string.write_document_short))
        val child9 = Child(getString(R.string.job_posts_short))


        val child10 = Child(getString(R.string.cell_short))
        val child11 = Child(getString(R.string.climate_short))
        val child12 = Child(getString(R.string.evolution_short))

        val child13 = Child(getString(R.string.hair_short))
        val child14 = Child(getString(R.string.sleep_short))
        val child15 = Child(getString(R.string.routine_short))
        val child16 = Child(getString(R.string.book_short))


        val children = listOf(child1, child2, child3, child4)
        val children1 = listOf(child4, child5, child6, child7, child8, child9)
        val children2 = listOf(child10, child11, child12)
        val children3 = listOf(child13, child14, child15,child16)

        val parent1 = ResourcesCompat.getDrawable(resources, R.drawable.plane, null)
            ?.let { Parent(it, getString(R.string.travel_and_discovery), children) }
        val parent2 = ResourcesCompat.getDrawable(resources, R.drawable.creativity, null)
            ?.let { Parent(it, getString(R.string.creative_ideas), children1) }

        val parent3 = ResourcesCompat.getDrawable(resources, R.drawable.atom, null)
            ?.let { Parent(it, getString(R.string.science_and_learning), children2) }

        val parent4 = ResourcesCompat.getDrawable(resources, R.drawable.cosmetics, null)
            ?.let { Parent(it, getString(R.string.beauty_and_lifestyle), children3) }

        val parent5 = ResourcesCompat.getDrawable(resources, R.drawable.cosmetics, null)
            ?.let { Parent(it, getString(R.string.travel_and_discovery), children) }

        val parents = mutableListOf<Parent>()

        parent1?.let { parents.add(it) }
        parent2?.let { parents.add(it) }
        parent3?.let { parents.add(it) }
        parent4?.let { parents.add(it) }
        parent5?.let { parents.add(it) }

        val adapter = MyAdapter(parents.filterNotNull()){ clickedChild ->

            val bundle = Bundle().apply {
                putString("clickedChildName", clickedChild.text)
            }

            findNavController().navigate(R.id.action_exploreFragment_to_chatFragment2, bundle)

        }

        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView2.adapter = adapter



        databaseAdapter = DatabaseAdapter(mutableListOf())
        binding.dbRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.dbRecyclerview.adapter = databaseAdapter

        viewModel.fetchDailyUserMessages()
        viewModel.dailyUserMessages.observe(viewLifecycleOwner, Observer { dailyUserMessages ->
            databaseAdapter.updateData(dailyUserMessages)
        })
    }




}