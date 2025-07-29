package ui.favorite

import ObservableLoadingCounter
import UiMessageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.data.domain.favorite.UpdateFavoriteCitiesWeatherUseCase
import app.data.domain.favorite.UpdateFavoriteStationsWeatherUseCase
import app.data.model.FavoriteCity
import app.data.model.FavoriteStationParams
import app.data.repo.FavoritesRepository
import collectStatus
import exception.AppLog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * 此页面请求地址中的 cityids 不能为空，必须至少有一个城市id
 * 城市天气请求参数中 isauto = 0 ,只有首页定位页面 isauto = 1
 */
class FavoritesViewModel(
    private val favoriteRepository: FavoritesRepository,
    private val updateStationsWeather: UpdateFavoriteStationsWeatherUseCase,
    private val updateCitiesWeather: UpdateFavoriteCitiesWeatherUseCase,
) : ViewModel() {

    private val viewModelState = MutableStateFlow(ViewModelState(isLoading = false))
    private val isLoading = ObservableLoadingCounter()
    private val messages = UiMessageManager()
    private val cities = mutableListOf<FavoriteCity>()
    private val stations = mutableListOf<FavoriteStationParams>()

    val uiState: StateFlow<FavoriteUiState> = viewModelState
        .map(ViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            viewModelState.value.toUiState()
        )

    // 回退返回，不会再次触发 init
    init {
        observerFavoriteScreenData()       // ui 订阅
        updateFavoriteStationsWeather()   // 更新 站点天气数据
        updateFavoriteCitiesWeather()     // 更新 城市天气数据
    }


    fun refresh() {
        viewModelScope.launch {
            updateStationsWeather(stations).collectStatus(isLoading, messages)
            updateCitiesWeather(cities).collectStatus(isLoading, messages)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun updateFavoriteStationsWeather() = viewModelScope.launch {
        favoriteRepository.observerFavoriteStations()
            .onEach { // 每次向下游发送数据之前，先执行清空操作，该操作不影响发送给下游的值
                this@FavoritesViewModel.stations.clear()
                this@FavoritesViewModel.stations.addAll(it)
            }.flatMapLatest { stationsParams ->
                updateStationsWeather(stationsParams)
            }.collectStatus(isLoading, messages)
    }

    private fun updateFavoriteCitiesWeather() = viewModelScope.launch {
        favoriteRepository.observerFavoriteCities().collect { cities ->
            // 将 cities 保存到全局变量中，供下拉刷新使用
            this@FavoritesViewModel.cities.clear()
            this@FavoritesViewModel.cities.addAll(cities)
            updateCitiesWeather(cities).collectStatus(isLoading, messages)
        }
    }

    private fun observerFavoriteScreenData() = viewModelScope.launch {
        combine(
            favoriteRepository.observerFavoriteCitiesWeather(),
            favoriteRepository.observerFavoriteStationsWeather(),
            isLoading.flow,
            messages.flow,
        ) { citiesWeather, stationsWeather, isLoading, messages ->
            ViewModelState(
                cityWeather = citiesWeather,
                stationsWeather = stationsWeather,
                isLoading = isLoading,
                uiMessage = messages
            )
        }.collect { newData ->
            viewModelState.update {
                it.copy(
                    cityWeather = newData.cityWeather,
                    stationsWeather = newData.stationsWeather,
                    isLoading = newData.isLoading,
                    uiMessage = newData.uiMessage
                )
            }
        }
    }

    fun deleteStation(stationName: String) {
        AppLog.d("deleteStation: $stationName")
        viewModelScope.launch {
            val newStations = stations.filterNot {
                it.stationName == stationName
            }
            AppLog.d("newStations: $newStations")
            stations.clear()
            stations.addAll(newStations)
            favoriteRepository.deleteFavoriteStation(stationName)
        }
    }

    fun deleteCity(cityId: String) {
        viewModelScope.launch {
            val newCities = cities.filterNot { it.cityId == cityId }
            cities.clear()
            cities.addAll(newCities)
            favoriteRepository.deleteFavoriteCity(cityId)
        }
    }

    fun clearMessage(id: Long) = viewModelScope.launch {
        messages.clearMessage(id)
    }
}