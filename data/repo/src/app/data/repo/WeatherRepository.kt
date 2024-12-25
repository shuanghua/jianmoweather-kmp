package app.data.repo

import AppDispatcher
import app.data.database.LocalDataSource
import app.data.model.Location
import app.data.model.Weather
import app.data.model.previewWeather
import app.data.network.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

interface WeatherRepository {
    fun observerWeather(): Flow<Weather>
    suspend fun updateLocationCityWeather(location: Location)
    suspend fun updateCityOrStationWeather(
        cityId: String,
        stationId: String,
    )
}

internal class WeatherRepositoryImpl(
    private val database: LocalDataSource,
    private val network: NetworkDataSource,
    private val appDispatcher: AppDispatcher,
) : WeatherRepository {

    override fun observerWeather(): Flow<Weather> {
        return database.observerWeather().filterNotNull().onStart {
            emit(previewWeather)
        }
    }

    /**
     * 自动定位城市更新天气
     */
    override suspend fun updateLocationCityWeather(
        location: Location,
    ) = withContext(appDispatcher.io) {
        val weather = network.getWeatherByLocation(
            latitude = location.latitude,
            longitude = location.longitude,
            cityName = location.cityName,
            district = location.district,
        )
        database.insertWeather(weather)
    }

    /**
     * 手动 选择城市 或者手动选择 站点
     */
    override suspend fun updateCityOrStationWeather(
        cityId: String,
        stationId: String,
    ) = withContext(appDispatcher.io) {
        val weather = network.getWeatherByStationId(cityId, stationId)
        database.insertWeather(weather)
    }
}