package app.data.model

data class FavoriteCityWeather(
	val cityName: String,
	val cityId: String,
	val provinceName: String = "",
	val isAutoLocation: String = "",
	val currentT: String,
	val bgImageNew: String,
	val iconUrl: String
)
