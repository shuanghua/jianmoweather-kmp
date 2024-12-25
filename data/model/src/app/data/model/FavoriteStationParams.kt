package app.data.model

data class FavoriteStationParams(
	val isAutoLocation: String = "1",
	val cityId: String = "",
	val stationId: String = "",
	val stationName: String = "",
	val latitude: String = "",
	val longitude: String = "",
	val cityName: String = "",
	val district: String = "",
)