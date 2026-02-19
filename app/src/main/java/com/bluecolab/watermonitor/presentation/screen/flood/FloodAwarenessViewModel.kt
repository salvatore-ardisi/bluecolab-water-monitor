package com.bluecolab.watermonitor.presentation.screen.flood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluecolab.watermonitor.domain.model.FloodImpactLocation
import com.bluecolab.watermonitor.domain.model.StreamGage
import com.bluecolab.watermonitor.domain.usecase.GetFloodDataUseCase
import com.bluecolab.watermonitor.util.Constants
import com.bluecolab.watermonitor.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FloodAwarenessUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "NY",
    val searchByState: Boolean = true,
    val streamGages: List<StreamGage> = emptyList(),
    val floodImpacts: List<FloodImpactLocation> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class FloodAwarenessViewModel @Inject constructor(
    private val getFloodDataUseCase: GetFloodDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FloodAwarenessUiState())
    val uiState: StateFlow<FloodAwarenessUiState> = _uiState.asStateFlow()

    init {
        searchByState("NY")
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun toggleSearchMode() {
        _uiState.update { it.copy(searchByState = !it.searchByState) }
    }

    fun searchByState(stateCode: String) {
        val code = stateCode.uppercase().take(2)
        _uiState.update { it.copy(searchQuery = code, searchByState = true) }

        viewModelScope.launch {
            getFloodDataUseCase.byState(code).collect { resource ->
                handleResource(resource)
            }
        }
    }

    fun searchByLocation(lat: Double = Constants.CHOATE_POND_LAT, lon: Double = Constants.CHOATE_POND_LONG) {
        _uiState.update { it.copy(searchByState = false) }

        viewModelScope.launch {
            getFloodDataUseCase.byLocation(lat, lon).collect { resource ->
                handleResource(resource)
            }
        }
    }

    fun search() {
        val state = _uiState.value
        if (state.searchByState) {
            searchByState(state.searchQuery)
        } else {
            searchByLocation()
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    private fun handleResource(resource: Resource<com.bluecolab.watermonitor.domain.model.FloodAwarenessData>) {
        when (resource) {
            is Resource.Loading -> {
                _uiState.update { it.copy(isLoading = true, error = null) }
            }
            is Resource.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        streamGages = resource.data.streamGages,
                        floodImpacts = resource.data.floodImpactLocations,
                        error = null
                    )
                }
            }
            is Resource.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = resource.message
                    )
                }
            }
        }
    }
}
