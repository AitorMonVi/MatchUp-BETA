package com.example.matchup_beta

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class StatsFragment : Fragment() {

    private lateinit var barChart: BarChart
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_stats, container, false)
        barChart = view.findViewById(R.id.barChart)
        preferencesManager = PreferencesManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val added = preferencesManager.itemsAdded.first()
            val removed = preferencesManager.itemsRemoved.first()
            setupChart(added, removed)
        }
    }

    private fun setupChart(added: Int, removed: Int) {
        val entries = ArrayList<BarEntry>().apply {
            add(BarEntry(0f, added.toFloat()))
            add(BarEntry(1f, removed.toFloat()))
        }

        val labels = listOf("Afegit", "Esborrat")
        val dataSet = BarDataSet(entries, "Actions de l'usuari")
        val data = BarData(dataSet)

        barChart.data = data
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.granularity = 1f
        barChart.xAxis.setDrawGridLines(false)
        barChart.axisLeft.setDrawGridLines(false)
        barChart.axisRight.isEnabled = false
        barChart.description.isEnabled = false
        barChart.invalidate()
    }
}
