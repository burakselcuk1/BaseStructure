package com.example.basestructure.ui.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
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


        val child5 = Child("İsim Üretici")
        val child6 = Child("İlişki Tavsiyeleri")
        val child7 = Child("Başlık Fikirleri")
        val child8 = Child("Şiir Yazma")
        val child9 = Child("İş İlanı")


        val child10 = Child("Hücre Organelleri")
        val child11 = Child("İklim Değişikliği")
        val child12 = Child("Evrim Teorisi")

        val child13 = Child("Saç Uzatmak")
        val child14 = Child("Daha iyi uyku")
        val child15 = Child("Sabah Rutini")
        val child16 = Child("Kitap Önerileri")


        val children = listOf(child1, child2, child3, child4)
        val children1 = listOf(child4, child5, child6, child7, child8, child9)
        val children2 = listOf(child10, child11, child12)
        val children3 = listOf(child13, child14, child15,child16)

        val parent1 = ResourcesCompat.getDrawable(resources, R.drawable.plane, null)
            ?.let { Parent(it, "Seyehat Ve Keşif", children) }
        val parent2 = ResourcesCompat.getDrawable(resources, R.drawable.creativity, null)
            ?.let { Parent(it, "Yaratıcı Fikirler", children1) }

        val parent3 = ResourcesCompat.getDrawable(resources, R.drawable.atom, null)
            ?.let { Parent(it, "Bilim ve Öğrenme", children2) }

        val parent4 = ResourcesCompat.getDrawable(resources, R.drawable.cosmetics, null)
            ?.let { Parent(it, "Güzellik ve Yaşam Tarzı", children3) }

        val parent5 = ResourcesCompat.getDrawable(resources, R.drawable.cosmetics, null)
            ?.let { Parent(it, "Seyehat ve Keşif", children) }

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
    }


}