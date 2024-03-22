package com.example.bondoman.ui.hub.stats

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.bondoman.R
import com.example.bondoman.database.entity.TransactionEntity
import com.example.bondoman.databinding.FragmentStatsBinding
import com.example.bondoman.ui.hub.HubActivity
import com.example.bondoman.viewmodel.transaction.TransactionViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

class StatsFragment : Fragment() {
    private lateinit var binding: FragmentStatsBinding
    private lateinit var tsViewModel: TransactionViewModel
    private lateinit var chartColors: ArrayList<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val header = requireActivity().findViewById<TextView>(R.id.nav_title)
            header.text = getString(R.string.hub_nav_stats)
        }

        tsViewModel = (requireActivity() as HubActivity).transactionViewModel
        chartColors = ArrayList()
        chartColors.add(ContextCompat.getColor(requireActivity(), R.color.purple_200))
        chartColors.add(ContextCompat.getColor(requireActivity(), R.color.purple_500))

        setupChart()
        tsViewModel.list.observe(viewLifecycleOwner) { tsList ->
            observeChart(tsList)
        }
    }

    private fun setupChart() {
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.description.isEnabled = false
        binding.pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        binding.pieChart.dragDecelerationFrictionCoef = 0.95f

        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.setHoleColor(ContextCompat.getColor(requireActivity(), R.color.white))
        binding.pieChart.holeRadius = 50f
        binding.pieChart.setDrawCenterText(true)

        binding.pieChart.rotationAngle = 0f
        binding.pieChart.isRotationEnabled = true
        binding.pieChart.isHighlightPerTapEnabled = true
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)

        binding.pieChart.legend.isEnabled = true
        binding.pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        binding.pieChart.setEntryLabelColor(ContextCompat.getColor(requireActivity(), R.color.white))
        binding.pieChart.setEntryLabelTextSize(12f)
    }

    private fun observeChart(tsList: List<TransactionEntity>) {
        var incomeCount = 0f
        tsList.forEach{
            if (it.category == "Income") incomeCount++
        }

        val incomePerc = incomeCount / tsList.size
        val expensePerc = 1 - incomePerc

        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(incomePerc, "Income"))
        entries.add(PieEntry(expensePerc, "Expenses"))
        val dataSet = PieDataSet(entries, "")

        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.colors = chartColors

        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter(binding.pieChart))
        pieData.setValueTextSize(15f)
        pieData.setValueTypeface(Typeface.DEFAULT_BOLD)
        pieData.setValueTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        binding.pieChart.data = pieData

        binding.pieChart.invalidate()
    }
}