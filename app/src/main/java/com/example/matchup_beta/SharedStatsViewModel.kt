package com.example.matchup_beta

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedStatsViewModel : ViewModel() {
    private val _refreshChart = MutableStateFlow(false)
    val refreshChart = _refreshChart.asStateFlow()

    fun triggerRefresh() {
        _refreshChart.value = true
    }

    fun resetRefreshFlag() {
        _refreshChart.value = false
    }
}
