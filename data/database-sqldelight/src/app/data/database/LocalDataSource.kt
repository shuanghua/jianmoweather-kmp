package app.data.database

import app.data.model.*
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    // 主页
    fun observerWeather(): Flow<Weather>
    fun insertWeather(weather: Weather)

    // 省份和城市页面
    fun insertProvinces(provinces: List<Province>)
    fun insertCities(cities: List<City>)
    fun observerProvinces(): Flow<List<Province>>
    fun observerCities(provinceName: String): Flow<List<City>>


    // 观测站页面
    fun insertDistricts(districts: List<District>)
    fun insertStations(stations: List<Station>)
    fun observerDistricts(): Flow<List<District>>
    fun observerStationsByDistrictName(districtName: String): Flow<List<Station>>

    fun insertSelectedStation(selectedStation: SelectedStation)
    fun observerSelectedStation(): Flow<SelectedStation?>
    suspend fun getStationIdByName(stationName: String): String?


    // 收藏站点
    fun insertFavoriteStationParam(favoriteStationsParam: FavoriteStationParams)
    fun observerFavoriteStationParams(): Flow<List<FavoriteStationParams>>
    fun deleteFavoriteStationParamByStationName(stationName: String)
    fun getFavoriteStationParamByStationName(stationName: String): FavoriteStationParams

    fun insertFavoriteStationsWeather(favoriteStationsWeather: List<FavoriteStationWeather>)
    fun observerFavoriteStationsWeather(): Flow<List<FavoriteStationWeather>>
    fun deleteFavoriteStationsWeatherByStationName(stationName: String)
    fun deleteAllFavoriteStationsWeather()

    // 收藏城市
    fun insertCityToFavorite(city: City)
    fun observerFavoriteCities(): Flow<List<FavoriteCity>>
    fun deleteFavoriteCityByCityId(cityId: String)

    fun insertFavoriteCitiesWeather(favoriteCitiesWeather: List<FavoriteCityWeather>)
    fun observerFavoriteCityWeather(): Flow<List<FavoriteCityWeather>>
    fun deleteFavoriteCityWeatherByCityId(cityId: String)
    fun deleteAllFavoriteCityWeather()


    fun deleteFavoriteCityWithWeather(cityId: String)
    fun deleteFavoriteStationWithWeather(stationName: String)


}