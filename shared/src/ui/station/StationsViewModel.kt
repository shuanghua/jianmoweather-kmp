package ui.station

import AppDispatcher
import UiMessage
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.data.model.SelectedStation
import app.data.model.Station
import app.data.repo.StationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 无网络获取
 * 根据 传递的区县名，向数据库 查询对应 站点列表
 */
class StationsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: StationRepository,
    private val dispatchers: AppDispatcher,
) : ViewModel() {

    //区县名 用于向 数据库 获取对应 站点列表
    private val districtName: String = checkNotNull(savedStateHandle[districtNameArg])

    private val viewModelState = MutableStateFlow(ViewModelState(isLoading = true))
    val uiState: StateFlow<StationsUiState> =
        viewModelState.map(ViewModelState::toUiState)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = viewModelState.value.toUiState(),
            )

    init {
        viewModelScope.launch(dispatchers.io) {
            repository.observerStations(districtName)
                .collect { stationList ->
                    if (stationList.isEmpty()) {
                        viewModelState.update {
                            val errorMessage =
                                it.errorMessage + UiMessage("数据库没有站点数据:$districtName")
                            it.copy(isLoading = false, errorMessage = errorMessage)
                        }
                    } else {
                        println("数据库有站点数据:$districtName->$stationList")
                        viewModelState.update {
                            it.copy(isLoading = false, stationList = stationList)
                        }
                    }
                }
        }
    }

    fun saveSelectedStation(station: Station) {
        viewModelScope.launch(dispatchers.io) {
            val selectedStation = SelectedStation(
                stationId = station.stationId,
                isLocation = if (station.districtName == "自动定位") "1" else "0",  //返回到首页定位则传1，完美情况应该根据定位是否成功来判定
                districtName = station.districtName,
                stationName = station.stationName,
            )
            repository.saveSelectedStation(selectedStation)
        }
    }
}

sealed interface StationsUiState {
    val isLoading: Boolean
    val errorMessage: List<UiMessage>

    data class NoData(
        override val isLoading: Boolean,
        override val errorMessage: List<UiMessage>,
    ) : StationsUiState

    data class HasData(
        override val isLoading: Boolean,
        override val errorMessage: List<UiMessage>,
        val stationList: List<Station>,
    ) : StationsUiState
}

private data class ViewModelState(
    val isLoading: Boolean = false,
    val errorMessage: List<UiMessage> = emptyList(),
    val stationList: List<Station> = emptyList(),
) {
    fun toUiState(): StationsUiState {
        return if (stationList.isEmpty()) {
            StationsUiState.NoData(
                isLoading = isLoading,
                errorMessage = errorMessage
            )
        } else {
            StationsUiState.HasData(
                isLoading = isLoading,
                errorMessage = errorMessage,
                stationList = stationList
            )
        }
    }
}