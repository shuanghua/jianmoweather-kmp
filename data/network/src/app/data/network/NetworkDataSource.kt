package app.data.network

import app.data.model.*


interface NetworkDataSource {
    suspend fun getWeatherByLocation(
        latitude: String,
        longitude: String,
        cityName: String,
        district: String,
    ): Weather

    suspend fun getWeatherByCityId(cityId: String): Weather

    suspend fun getWeatherByStationId(
        cityId: String,
        stationId: String,
    ): Weather

    suspend fun getProvincesWithCities(
        uid: String = "d6OIg9m36iZ4kri8sztq",
    ): ProvinceCity

    suspend fun getDistrictsWithStations(
        cityId: String,
        obtId: String,
        latitude: String,
        longitude: String
    ): DistrictStation

    suspend fun getFavoriteCityWeather(
        latitude: String,
        longitude: String,
        cityIds: String
    ): List<FavoriteCityWeather>

    suspend fun getFavoriteStationWeatherByStationId(
        cityId: String,
        stationId: String,
    ): FavoriteStationWeather

    suspend fun getFavoriteStationWeatherByLocation(
        latitude: String,
        longitude: String,
        cityName: String,
        district: String,
    ):FavoriteStationWeather
}