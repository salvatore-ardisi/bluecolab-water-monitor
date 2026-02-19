package com.bluecolab.watermonitor.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluecolab.watermonitor.domain.model.WaterQualityData
import com.bluecolab.watermonitor.domain.model.WaterQualityStatus
import com.bluecolab.watermonitor.domain.usecase.GetCurrentWaterQualityUseCase
import com.bluecolab.watermonitor.domain.usecase.RefreshWaterQualityUseCase
import com.bluecolab.watermonitor.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val waterQualityData: WaterQualityData? = null,
    val error: String? = null
) {
    val status: WaterQualityStatus
        get() = waterQualityData?.status ?: WaterQualityStatus.UNKNOWN
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentWaterQualityUseCase: GetCurrentWaterQualityUseCase,
    private val refreshWaterQualityUseCase: RefreshWaterQualityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadWaterQuality()
    }

    fun loadWaterQuality() {
        viewModelScope.launch {
            getCurrentWaterQualityUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                waterQualityData = resource.data,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }

            when (val result = refreshWaterQualityUseCase()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            waterQualityData = result.data,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            error = result.message
                        )
                    }
                }
                is Resource.Loading -> {
                    // Already handled above
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
