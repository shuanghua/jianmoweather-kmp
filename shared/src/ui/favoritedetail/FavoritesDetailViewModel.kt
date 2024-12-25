package ui.favoritedetail

import UiMessage
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.data.domain.favorite.GetFavoriteDetailWeatherUseCase
import app.data.model.Weather
import app.data.model.previewWeather
import dev.shuanghua.weather.shared.Result
import dev.shuanghua.weather.shared.asResult
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoritesDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getWeatherUseCase: GetFavoriteDetailWeatherUseCase,
) : ViewModel() {

    private val cityId: String = checkNotNull(savedStateHandle[cityIdArg])
    private val stationName: String = checkNotNull(savedStateHandle[stationNameArg])

    private val viewModelState = MutableStateFlow(WeatherViewModelState(isLoading = false))

    internal val uiState: StateFlow<WeatherUiState> = viewModelState
        .map(WeatherViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            viewModelState.value.toUiState()
        )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            viewModelState.update { it.copy(isLoading = true) }
            delay(400L)
            getWeatherUseCase(cityId, stationName).asResult().collect { result ->
                viewModelState.update {
                    when (result) {
                        is Result.Loading -> it.copy(isLoading = true)
                        is Result.Success -> it.copy(weather = result.data, isLoading = false)
                        is Result.Error -> {
                            Napier.e("---------->>${result.exception}")
                            val errorMessage =
                                if (result.exception.message!!.contains("Unable to resolve host")) {
                                    it.uiMessage + (UiMessage("需要使用中国地区网络环境"))
                                } else {
                                    it.uiMessage + (UiMessage(result.exception))
                                }
                            it.copy(weather = previewWeather, uiMessage = errorMessage, isLoading = false)
                        }
                    }
                }
            }
        }
    }


    fun clearMessage(id: Long) {
        viewModelState.update { currentUiState ->
            val errorMessages = currentUiState.uiMessage.filterNot { it.id == id }
            currentUiState.copy(uiMessage = errorMessages)
        }
    }

}

internal sealed interface WeatherUiState {
    val isLoading: Boolean
    val uiMessage: List<UiMessage>

    data class NoData(
        override val isLoading: Boolean,
        override val uiMessage: List<UiMessage>,
    ) : WeatherUiState

    data class HasData(
        val weather: Weather,
        override val isLoading: Boolean,
        override val uiMessage: List<UiMessage>,
    ) : WeatherUiState
}

internal data class WeatherViewModelState(
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val uiMessage: List<UiMessage> = emptyList(),
) {
    fun toUiState(): WeatherUiState = if (weather == null) {
        WeatherUiState.NoData(
            isLoading = isLoading,
            uiMessage = uiMessage
        )
    } else {
        WeatherUiState.HasData(
            weather = weather,
            isLoading = isLoading,
            uiMessage = uiMessage
        )
    }
}