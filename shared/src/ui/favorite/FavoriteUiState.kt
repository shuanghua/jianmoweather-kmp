package ui.favorite

import UiMessage
import app.data.model.FavoriteCityWeather
import app.data.model.FavoriteStationWeather

sealed interface FavoriteUiState {

	val isLoading: Boolean
	val uiMessage: UiMessage?

	data class NoData(
		override val isLoading: Boolean,
		override val uiMessage: UiMessage?
	) : FavoriteUiState

	data class HasData(
		val cityWeather: List<FavoriteCityWeather>,
		val stationWeather: List<FavoriteStationWeather>,
		override val isLoading: Boolean,
		override val uiMessage: UiMessage?
	) : FavoriteUiState
}

data class ViewModelState(
	val cityWeather: List<FavoriteCityWeather> = emptyList(),
	val stationsWeather: List<FavoriteStationWeather> = emptyList(),
	val isLoading: Boolean = false,
	val uiMessage: UiMessage? = null
) {
	fun toUiState(): FavoriteUiState {
		return if (cityWeather.isEmpty() && stationsWeather.isEmpty()) {
			FavoriteUiState.NoData(
				isLoading = isLoading,
				uiMessage = uiMessage
			)
		} else {
			FavoriteUiState.HasData(
				cityWeather = cityWeather,
				stationWeather = stationsWeather,
				isLoading = isLoading,
				uiMessage = uiMessage
			)
		}
	}
}