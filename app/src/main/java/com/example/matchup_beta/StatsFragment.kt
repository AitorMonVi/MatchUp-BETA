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
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.formatter.ValueFormatter

class StatsFragment : Fragment() {

    private lateinit var barChart: BarChart
    private lateinit var preferencesManager: PreferencesManager
    private val sharedStatsViewModel: SharedStatsViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_stats, container, false)
        barChart = view.findViewById(R.id.barChart)
        barChart.setNoDataText("Encara no hi ha accions registrades.")
        preferencesManager = PreferencesManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateChart()

        lifecycleScope.launch {
            sharedStatsViewModel.refreshChart.collect { shouldRefresh ->
                if (shouldRefresh) {
                    updateChart()
                    sharedStatsViewModel.resetRefreshFlag()
                }
            }
        }

        lifecycleScope.launch {
            preferencesManager.incrementFragmentEntries()
        }
    }

    private fun updateChart() {
        lifecycleScope.launch {
            val added = preferencesManager.itemsAdded.first()
            val removed = preferencesManager.itemsRemoved.first()
            val totalEntries = preferencesManager.fragmentEntries.first()
            println("DEBUG: added=$added, removed=$removed, totalEntries=$totalEntries")
            setupChart(added, removed, totalEntries)
        }
    }


    private fun setupChart(added: Int, removed: Int, entriesCount: Int) {
        val entries = listOf(
            BarEntry(0f, added.toFloat()),
            BarEntry(1f, removed.toFloat()),
            BarEntry(2f, entriesCount.toFloat())
        )

        val labels = listOf("Afegit", "Esborrat", "Entrades")
        val dataSet = BarDataSet(entries, "Accions de l'usuari")

        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        val data = BarData(dataSet)

        barChart.apply {
            this.data = data
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.granularity = 1f
            xAxis.setLabelCount(labels.size)
            xAxis.setDrawGridLines(false)

            axisLeft.setDrawGridLines(false)
            axisLeft.granularity = 1f
            axisLeft.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            }

            axisRight.isEnabled = false
            description.isEnabled = false
            setFitBars(true)
            notifyDataSetChanged()
            invalidate()
        }
    }
}
