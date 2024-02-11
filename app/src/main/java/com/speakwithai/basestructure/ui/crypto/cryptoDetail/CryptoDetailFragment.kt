package com.speakwithai.basestructure.ui.crypto.cryptoDetail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.speakwithai.basestructure.R
import com.speakwithai.basestructure.data.model.response.coin.CoinChartResponse
import com.speakwithai.basestructure.data.model.response.coin.SearchedCoin
import com.speakwithai.basestructure.databinding.FragmentCryptoDetailBinding
import com.speakwithai.basestructure.ui.crypto.model.CoinUiModel
import com.speakwithai.basestructure.ui.crypto.utilities.Response
import com.speakwithai.basestructure.ui.crypto.utilities.displayErrorSnackBar
import com.speakwithai.basestructure.ui.crypto.utilities.getSerializableArg
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CryptoDetailFragment : Fragment()  {


    private var _binding: FragmentCryptoDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel : CryptoDetailViewModel by viewModels();


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCryptoDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        val coinApiModel = arguments.getSerializableArg("coin", CoinUiModel::class.java)
        val searchedCoin = arguments.getSerializableArg("searched_coin", SearchedCoin::class.java)

        if (coinApiModel != null) {
            viewModel.coinApiModel = coinApiModel
        } else {
            viewModel.coinApiModel = CoinUiModel(searchedCoin!!.id, searchedCoin.symbol, searchedCoin.name, -1f, searchedCoin.large, searchedCoin.market_cap_rank)
        }

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        this.setupChart()
        this.setupObservers()

        return root;
    }

    private fun setupObservers() {
        viewModel.coinChartData.observe(viewLifecycleOwner) { response ->
            response.data?.let {
                processResponseIntoChart(it)
            }

            if (response is Response.Error) {
                displayErrorSnackBar(response, binding.root, requireContext(), viewModel::fetchCoinChartData)
            }
        }
    }

    private fun processResponseIntoChart(response: CoinChartResponse) {
        val entries = response.prices.map { Entry(it[0], it[1]) }
        val lineDataSet = LineDataSet(entries, "")

        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(false)

        binding.lineChart.data = LineData(lineDataSet)
        binding.lineChart.invalidate()
    }

    private fun setupChart() = binding.lineChart.apply {
        setOnChartTouchListener()

        legend.isEnabled = false
        description.isEnabled = false
        isHighlightPerTapEnabled = false
        isHighlightPerDragEnabled = true

        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(false)

        axisRight.setDrawAxisLine(false)
        axisRight.setDrawGridLines(true)
        axisRight.setDrawLabels(false)
        axisRight.gridColor = Color.WHITE

        axisLeft.setDrawAxisLine(false)
        axisLeft.setDrawGridLines(true)
        axisLeft.setLabelCount(2, true)
        axisLeft.textColor = Color.WHITE
        axisLeft.gridColor = Color.WHITE
        axisLeft.gridLineWidth = 0.2f
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnChartTouchListener() {
        binding.lineChart.apply {
            setOnTouchListener { v, event ->
                val x = event.x
                val y = event.y
                when (event.action) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                        val entry: Entry? = getEntryByTouchPoint(x, y)
                        if (entry != null) {
                            viewModel.displayPrice(entry.y)
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        // Zrušenie highlightu po uvoľnení prsta
                        highlightValue(null)
                        viewModel.displayDefaultPrice()
                    }
                }
                false
            }
        }
    }




}