package ui.district

import ObservableLoadingCounter
import UiMessage
import UiMessageManager
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.data.domain.station.ObserverDistrictUseCase
import app.data.domain.station.UpdateDistrictUseCase
import app.data.model.District
import collectStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 有网络获取
 * 从网络获取 区县 和 站点 的所有数据
 */
class DistrictsViewModel(
	savedStateHandle: SavedStateHandle,
	observerDistrict: ObserverDistrictUseCase,
	private val updateDistrictUseCase: UpdateDistrictUseCase
) : ViewModel() {

	private val cityId: String = checkNotNull(savedStateHandle[cityIdArg])
//	private val stationName: String = checkNotNull(savedStateHandle[stationNameArg])

	private val observerLoading = ObservableLoadingCounter()
	private val uiMessageManager = UiMessageManager()

	// 观察数据库
	// 为空->请求网络->缓存数据库
	// 不为空->直接显示
	val uiState: StateFlow<DistrictListUiState> = combine(
		observerDistrict(),
		uiMessageManager.flow,
		observerLoading.flow
	) { districtList, message, isLoading ->
		if (districtList.isEmpty()) {
			DistrictListUiState(
				districtList = emptyList(),
				uiMessage = message,
				isLoading = isLoading,
			)
		} else {
			DistrictListUiState(
				districtList = districtList,
				uiMessage = message,
				isLoading = isLoading,
			)
		}
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = DistrictListUiState.Empty,
	)

	init {
		refresh()
	}


	fun refresh() {
		viewModelScope.launch {
			updateDistrictUseCase(
				UpdateDistrictUseCase.Params(cityId = cityId)
			).collectStatus(observerLoading, uiMessageManager)
		}
	}
}

data class DistrictListUiState(
	val districtList: List<District> = emptyList(),
	val uiMessage: UiMessage? = null,
	val isLoading: Boolean = false,
) {
	companion object {// 默认值
		val Empty = DistrictListUiState()
	}
}

