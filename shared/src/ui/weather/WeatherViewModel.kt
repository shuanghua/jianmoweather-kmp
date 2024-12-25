package ui.weather

import ObservableLoadingCounter
import UiMessage
import UiMessageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.data.domain.weather.*
import app.data.model.Weather
import collectStatus
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WeatherViewModel(
    observerWeather: ObserverWeather,
    private val observerSelectedStation: ObserverSelectedStation,
    private val updateLocationCityWeather: UpdateLocationCityWeather,
    private val updateStationWeather: UpdateStationWeather,
    private val addStationToFavorite: SaveStationToFavorite,
) : ViewModel() {

    private var cityId: String = "28060159493"
    private var stationId: String = ""
    private var stationName: String = ""
    private var isLocation: Boolean = false
    private val isLoading = ObservableLoadingCounter()
    private val messages = UiMessageManager()

    private val viewModelState = MutableStateFlow(
        WeatherViewModelState(isLoading = false)
    )

    val uiState: StateFlow<WeatherUiState> = viewModelState
        .map(WeatherViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            viewModelState.value.toUiState()
        )

    init {
        observerWeatherData(
            observerWeather(),
            isLoading.flow,
            messages.flow
        )
        observerSelectedStationData() // 内部根据站点变化 触发网络请求来获取天气数据
    }


    fun refresh() {
        viewModelScope.launch {
            if (isLocation) {
                updateLocationCityWeather(Unit).collectStatus(isLoading, messages)
            } else {
                updateStationWeather(
                    UpdateStationWeather.Params(cityId, stationId)
                ).collectStatus(isLoading, messages)
            }
        }
    }

    private fun observerWeatherData(
        weatherFlow: Flow<Weather>,
        isLoadingFlows: Flow<Boolean>,
        errorMessageFlow: Flow<UiMessage?>,
    ) = viewModelScope.launch {
        combine(  // coroutines Zip.kt 最多允许 5 个 flow，超过需要自定义
            weatherFlow,
            isLoadingFlows,
            errorMessageFlow
        ) { weather, loadingStatus, errorMessage ->
            stationName = weather.stationName
            cityId = weather.cityId
            WeatherViewModelState(
                weather = weather,
                isLoading = loadingStatus,
                uiMessage = errorMessage
            )
        }.collect { newData ->
            viewModelState.update {
                it.copy(
                    weather = newData.weather,
                    isLoading = newData.isLoading,
                    uiMessage = newData.uiMessage
                )
            }
        }
    }

    /**
     * 1. 自动定位 包含 经纬度，不用含有城市id, 站点id
     * 2. 手动选择站点，需要含有 城市id, 站点id
     * 3. 数据库没有站点，且自动定位失败，使用 手动选择城市，只需要 传城市id
     */
    private fun observerSelectedStationData() = viewModelScope.launch {
        observerSelectedStation().collect {
            if (it == null || it.isLocation == "1") {//数据库没有站点，按自动定位
                isLocation = true
                updateLocationCityWeather(Unit).collectStatus(isLoading, messages)
            } else {
                isLocation = false
                stationId = it.stationId
                updateStationWeather(
                    UpdateStationWeather.Params(cityId, it.stationId)
                ).collectStatus(isLoading, messages)
            }
        }
    }

    fun clearMessage(id: Long) = viewModelScope.launch {
        messages.clearMessage(id)
    }

    fun saveToFavorite() = viewModelScope.launch {
        try {
            addStationToFavorite(cityId, stationName)
        } catch (e: Exception) {
            Napier.e(e.message.toString())
            messages.emitMessage(UiMessage("不要重复收藏哦"))
        }
    }
}

sealed interface WeatherUiState {
    val isLoading: Boolean
    val uiMessage: UiMessage?

    data class NoData(
        override val isLoading: Boolean,
        override val uiMessage: UiMessage?,
    ) : WeatherUiState

    data class HasData(
        val weather: Weather,
        override val isLoading: Boolean,
        override val uiMessage: UiMessage?,
    ) : WeatherUiState
}

private data class WeatherViewModelState(
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val uiMessage: UiMessage? = null,
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
