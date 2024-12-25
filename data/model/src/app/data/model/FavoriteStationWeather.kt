package app.data.model

data class FavoriteStationWeather(
    val cityId: String,
    val stationName: String,
    val temperature: String,
    val weatherStatus: String,
    val weatherIcon: String,
    val rangeT: String
)
