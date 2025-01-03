package ui.province

import ObservableLoadingCounter
import UiMessage
import UiMessageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.data.domain.city.UpdateProvinceUseCase
import app.data.model.Province
import app.data.repo.ProvinceCityRepository
import collectStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProvincesViewModel(
	private val updateProvince: UpdateProvinceUseCase,
	private val provinceRepository: ProvinceCityRepository,
) : ViewModel() {
	private val observerLoading = ObservableLoadingCounter()
	private val uiMessageManager = UiMessageManager()

	init {
		provinceRepository.observerProvinces()
//		refresh()
	}

	val uiState: StateFlow<ProvinceUiState> = combine(
		provinceRepository.observerProvinces(),
		uiMessageManager.flow,
		observerLoading.flow
	) { provinces, message, isLoading ->
		if (provinces.isEmpty()) {
			ProvinceUiState(
				provinces = emptyList(),
				uiMessage = message,
				isLoading = isLoading
			)
		} else {
			ProvinceUiState(
				provinces = provinces,
				uiMessage = message,
				isLoading = isLoading
			)
		}
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000),
		initialValue = ProvinceUiState.Empty
	)

	fun refresh() {
		viewModelScope.launch {
			updateProvince(Unit).collectStatus(
				observerLoading,
				uiMessageManager
			)
		}
	}

	fun clearMessage(id: Long) {
		viewModelScope.launch {
			uiMessageManager.clearMessage(id)
		}
	}
}

class ProvinceUiState(
	val provinces: List<Province> = emptyList(),
	val uiMessage: UiMessage? = null,
	val isLoading: Boolean = false,
) {
	companion object {
		val Empty = ProvinceUiState()
	}
}