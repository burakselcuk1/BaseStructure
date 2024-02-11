package com.speakwithai.basestructure.ui.crypto.converter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.databinding.FragmentConverterBinding
import com.speakwithai.basestructure.ui.crypto.utilities.Response
import com.speakwithai.basestructure.ui.crypto.utilities.displayErrorSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConverterFragment : Fragment() {

    private var _binding: FragmentConverterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ConverterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConverterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        binding.autoCompleteFrom.setOnItemClickListener { parent, view, position, id ->
            viewModel.changeFromExchangeRate(parent.getItemAtPosition(position) as String)
        }

        binding.autoCompleteTo.setOnItemClickListener { parent, view, position, id ->
            viewModel.changeToExchangeRate(parent.getItemAtPosition(position) as String)
        }

        viewModel.fetchExchangeRates()
        this.setupObservers()

        return root
    }


    /**
     * Nastaví observer, ktorý sleduje odpoveď zo servera. Ak zo servera prídu dáta
     * skryje indikátor načítavania, nastaví skratky mien ako adaptéer do
     * dvoch autoComplete listov a nastaví default hodnoty. Ak čakáme
     * na odpoveď zobrazí indikátor načítavania. Ak nastane chyba
     * zobrazí chybovú hlášku.
     */
    private fun setupObservers() {
        viewModel.response.observe(viewLifecycleOwner) { response ->
            response.data?.let {
                binding.cpiLoadingIndicator.visibility = View.GONE

                val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, it.map { it.key.uppercase() }.sorted() )
                binding.autoCompleteFrom.setAdapter(arrayAdapter)
                binding.autoCompleteTo.setAdapter(arrayAdapter)

                binding.autoCompleteFrom.setText(viewModel.selectedFromExchangeRate, false)
                binding.autoCompleteTo.setText(viewModel.selectedToExchangeRate, false)
            }

            if (response is Response.Waiting) {
                binding.cpiLoadingIndicator.visibility = View.VISIBLE
            }

            if (response is Response.Error) {
                binding.cpiLoadingIndicator.visibility = View.GONE
                displayErrorSnackBar(response, binding.root, requireContext(), viewModel::fetchExchangeRates)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}