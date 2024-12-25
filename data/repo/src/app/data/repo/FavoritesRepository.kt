package app.data.repo

import AppDispatcher
import app.data.database.LocalDataSource
import app.data.model.*
import app.data.network.NetworkDataSource
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

interface FavoritesRepository {
    suspend fun getFavoriteCityDetailWeather(cityId: String): Weather // 收藏城市天气详情页
    suspend fun getFavoriteStationWeather(cityId: String, stationId: String): Weather //点击收藏站点
    suspend fun getFavoriteLocationStationWeather(
        latitude: String,
        longitude: String,
        cityName: String,
        district: String,
    ): Weather // 如果站点的请求参数是自动定位，其中是没有站点id的，因此需要经纬度去请求

    suspend fun getFavoriteStationByName(stationName: String): FavoriteStationParams
    suspend fun saveFavoriteStation(favoriteStationParams: FavoriteStationParams)

    suspend fun deleteFavoriteCity(cityId: String)
    suspend fun deleteFavoriteStation(stationName: String)
    suspend fun clearAllFavoriteCitiesWeather()
    suspend fun clearAllFavoriteStationsWeather()

    fun observerFavoriteCities(): Flow<List<FavoriteCity>>
    fun observerFavoriteStations(): Flow<List<FavoriteStationParams>>
    fun observerFavoriteCitiesWeather(): Flow<List<FavoriteCityWeather>>
    fun observerFavoriteStationsWeather(): Flow<List<FavoriteStationWeather>>

    suspend fun updateFavoriteStationsWeather(stationParams: List<FavoriteStationParams>)
    suspend fun updateFavoriteCitiesWeather(
        favoriteCities: List<FavoriteCity>,
        latitude: String,
        longitude: String,
        cityIds: String,
    )
}

class FavoritesRepositoryImpl(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource,
    private val dispatchers: AppDispatcher,
) : FavoritesRepository {

    override suspend fun updateFavoriteStationsWeather(
        stationParams: List<FavoriteStationParams>,
    ) = try {
        val networkDeferred: List<Deferred<FavoriteStationWeather>> = stationParams
            .map { stationParam: FavoriteStationParams ->
                if (stationParam.isAutoLocation == "1") { // 定位站点 使用首页请求定位城市的请求参数
                    getStationWeatherByLocation(
                        latitude = stationParam.latitude,
                        longitude = stationParam.longitude,
                        cityName = stationParam.cityName,
                        district = stationParam.district
                    )
                } else {
                    getStationWeatherByStationId(
                        cityId = stationParam.cityId,
                        stationId = stationParam.stationId
                    )
                }
            }
        val favoriteStationWeatherList = networkDeferred.awaitAll()
        localDataSource.insertFavoriteStationsWeather(favoriteStationWeatherList)
    } catch (e: Exception) {
        // TODO 需待处理: 当没有网络情况下，用户添加城市到收藏，应该如何给用户展示新加入的城市天气数据
        // 目前用假数据代替，包括之前添加的城市的数据都会被换成假数据
        // 保留实现的处理方式，建一张新表记录城市数据是否通过网络更新过，在添加城市或站点时将标记为false，
        // false 没有更新过，则在创建假数据时使用假数据，并将该条数据插入到数据库，
        // true 已经更新过，则不插入假数据，直接使用数据库中的数据
        // 网络正常，并请求完成，将该表的该条数据都更新为true
        val fakeData = createStationWeatherFakeData(stationParams)
        localDataSource.insertFavoriteStationsWeather(fakeData)
//		throwAndCastException(e)
    }

    private suspend fun getStationWeatherByStationId(
        cityId: String,
        stationId: String,
    ) = withContext(dispatchers.io) {
        async {
            networkDataSource.getFavoriteStationWeatherByStationId(
                cityId = cityId,
                stationId = stationId
            )
        }
    }

    private suspend fun getStationWeatherByLocation(
        latitude: String,
        longitude: String,
        cityName: String,
        district: String,
    ) = withContext(dispatchers.io) {
        async {
            networkDataSource.getFavoriteStationWeatherByLocation(
                latitude = latitude,
                longitude = longitude,
                cityName = cityName,
                district = district
            )
        }
    }

    /**
     * 点击 收藏城市 打开天气详情页
     * 不保存到数据库
     */
    override suspend fun getFavoriteCityDetailWeather(
        cityId: String,
    ): Weather = networkDataSource.getWeatherByCityId(cityId)

    override suspend fun getFavoriteStationWeather(
        cityId: String,
        stationId: String,
    ): Weather = networkDataSource.getWeatherByStationId(cityId, stationId)

    override suspend fun getFavoriteLocationStationWeather(
        latitude: String,
        longitude: String,
        cityName: String,
        district: String,
    ): Weather = networkDataSource.getWeatherByLocation(
        latitude = latitude,
        longitude = longitude,
        cityName = cityName,
        district = district
    )

    override fun observerFavoriteStations(): Flow<List<FavoriteStationParams>> {
        return localDataSource.observerFavoriteStationParams()
    }

    override suspend fun saveFavoriteStation(
        favoriteStationParams: FavoriteStationParams,
    ) {
        localDataSource.insertFavoriteStationParam(favoriteStationParams)
    }

    override suspend fun getFavoriteStationByName(
        stationName: String,
    ): FavoriteStationParams = localDataSource
        .getFavoriteStationParamByStationName(stationName)


//--------------City---------------------------------------------------------------------------//

    override fun observerFavoriteCities(): Flow<List<FavoriteCity>> {
        return localDataSource.observerFavoriteCities()
    }

    override fun observerFavoriteCitiesWeather(): Flow<List<FavoriteCityWeather>> {
        return localDataSource.observerFavoriteCityWeather()
    }

    override fun observerFavoriteStationsWeather(): Flow<List<FavoriteStationWeather>> {
        return localDataSource.observerFavoriteStationsWeather()
    }

    /**
     * 更新所有城市天气
     * 对于收藏城市接口，只需要拼接id参数，请求一次即可
     * 当没有网络时，添加城市到收藏页面没有数据（增加交互体验）
     * 之前已收藏的城市天气数据也会被重置为默认数据
     */
    override suspend fun updateFavoriteCitiesWeather(
        favoriteCities: List<FavoriteCity>,
        latitude: String,
        longitude: String,
        cityIds: String,
    ) {
        try {
            val citiesWeathers = networkDataSource.getFavoriteCityWeather(
                latitude = latitude,
                longitude = longitude,
                cityIds = cityIds
            )
            localDataSource.insertFavoriteCitiesWeather(citiesWeathers) // 保存到数据库
        } catch (e: Exception) {
            // 无网络下， 会走这里
            localDataSource.insertFavoriteCitiesWeather(createCityWeatherEntitiesFakeData(favoriteCities))
//			throwAndCastException(e)
        }
    }

    override suspend fun deleteFavoriteCity(cityId: String) {
        localDataSource.deleteFavoriteCityWithWeather(cityId)
    }

    override suspend fun deleteFavoriteStation(stationName: String) {
        localDataSource.deleteFavoriteStationWithWeather(stationName)
    }

    override suspend fun clearAllFavoriteCitiesWeather() {
        localDataSource.deleteAllFavoriteCityWeather()
    }

    override suspend fun clearAllFavoriteStationsWeather() {
        localDataSource.deleteAllFavoriteStationsWeather()
    }

    private fun createCityWeatherEntitiesFakeData(
        favoriteCities: List<FavoriteCity>,
    ): List<FavoriteCityWeather> {
        return favoriteCities.map {
            FavoriteCityWeather(
                cityName = it.cityName,
                cityId = it.cityId,
                provinceName = it.provinceName,
                isAutoLocation = "",
                currentT = "",
                bgImageNew = "",
                iconUrl = "",
            )
        }
    }

    private fun createStationWeatherFakeData(
        favoriteStations: List<FavoriteStationParams>,
    ): List<FavoriteStationWeather> {
        return favoriteStations.map {
            FavoriteStationWeather(
                stationName = it.stationName,
                cityId = it.cityId,
                temperature = "",
                weatherStatus = "",
                weatherIcon = "",
                rangeT = "",
            )
        }
    }
}