package app.data.database.di

import app.cash.sqldelight.coroutines.asFlow
import app.data.database.LocalDataSource
import app.data.database.converter.asAlarmIconEntity
import app.data.database.converter.asAppFavoriteCityEntity
import app.data.database.converter.asAppModel
import app.data.database.converter.asConditionEntityList
import app.data.database.converter.asEntity
import app.data.database.converter.asExponentEntityList
import app.data.database.converter.asOneDayEntityList
import app.data.database.converter.asOneHourEntityList
import app.data.database.converter.asWeatherEntity
import app.data.model.City
import app.data.model.District
import app.data.model.FavoriteCity
import app.data.model.FavoriteCityWeather
import app.data.model.FavoriteStationParams
import app.data.model.FavoriteStationWeather
import app.data.model.Province
import app.data.model.SelectedStation
import app.data.model.Station
import app.data.model.Weather
import app.data.model.previewWeather
import app.data.sqldelight.WeatherEntity
import dev.shuanghua.weather.shared.combine
import exception.AppLog
import exception.ModuleException
import exceptionCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


internal class LocalDataSourceImpl(
    private val database: AppDatabase
) : LocalDataSource, ModuleException {

    override fun observerWeather(): Flow<Weather> {
        database.apply {
            val weatherFlow = weatherDao.selectAll()
                .asFlow()
                .map {
                    it.executeAsOneOrNull()
                }
            val onDayFlow = oneDayDao.selectAll().asFlow()
            val onHourFlow = oneHourDao.selectAll().asFlow()
            val alarmIconFlow = alarmIconDao.selectAll().asFlow()
            val conditionFlow = conditionDao.selectAll().asFlow()
            val exceptionFlow = exponentDao.selectAll().asFlow()

            return combine(
                weatherFlow,
                onDayFlow,
                onHourFlow,
                alarmIconFlow,
                conditionFlow,
                exceptionFlow
            ) { weather: WeatherEntity?, onDay, onHour, alarmIcon, condition, exception ->
                if (weather == null) {
                    previewWeather
                } else {
                    Weather(
                        cityId = weather.cityId,
                        cityName = weather.cityName,
                        temperature = weather.temperature,
                        description = weather.description,
                        airQuality = weather.airQuality,
                        airQualityIcon = weather.airQualityIcon,
                        lunarCalendar = weather.lunarCalendar,
                        stationName = weather.stationName,
                        stationId = weather.stationId,
                        sunUp = weather.sunUp,
                        sunDown = weather.sunDown,
                        oneDays = onDay.executeAsList().asAppModel(),
                        oneHours = onHour.executeAsList().asAppModel(),
                        alarmIcons = alarmIcon.executeAsList().asAppModel(),
                        conditions = condition.executeAsList().asAppModel(),
                        exponents = exception.executeAsList().asAppModel()
                    )
                }
            }
        }
    }

    override fun insertWeather(weather: Weather) {
        database.apply {
            transaction {
                weatherDao.insertFullWeather(weather.asWeatherEntity())
                weather.asAlarmIconEntity().forEach { alarmIconDao.insertFull(it) }
                weather.asConditionEntityList().forEach { conditionDao.insertFull(it) }
                weather.asExponentEntityList().forEach { exponentDao.insertFull(it) }
                weather.asOneDayEntityList().forEach { oneDayDao.insertFull(it) }
                weather.asOneHourEntityList().forEach { oneHourDao.insertFull(it) }
            }
        }
    }

    // -------------------------省份、城市页面

    override fun insertProvinces(provinces: List<Province>) {
        database.provinceDao.apply {
            transaction {
                provinces.forEach { province ->
                    insertFull(province.asEntity())
                }
            }
        }
    }

    override fun insertCities(cities: List<City>) {
        database.cityDao.apply {
            transaction {
                cities.forEach { city ->
                    insertFull(city.asEntity())
                }
            }
        }
    }

    override fun observerProvinces(): Flow<List<Province>> {
        return database.provinceDao.selectAll().asFlow().map {
            it.executeAsList().map { provinceName -> Province(provinceName) }
        }
    }

    override fun observerCities(provinceName: String): Flow<List<City>> {
        return database.cityDao.selectByProvinceName(provinceName).asFlow().map {
            it.executeAsList().map { cityEntity -> cityEntity.asAppModel() }
        }
    }

    // -------------------------区域，观测站点页面

    override fun insertDistricts(districts: List<District>) {
        database.districtDao.apply {
            transaction {
                districts.forEach { district ->
                    insertFull(district.asEntity())
                }
            }
        }
    }

    override fun insertStations(stations: List<Station>) {
        database.stationDao.apply {
            transaction {
                stations.forEach { station ->
//                    insertFull(station.asEntity())
                    insertOrReplace(station.asEntity())
                }
            }
        }
    }

    // 订阅区域列表
    override fun observerDistricts(): Flow<List<District>> {
        return database.districtDao.selectAll().asFlow().map {
            it.executeAsList().map { districtName: String ->
                AppLog.e("database: observerDistricts: $districtName")

                District(districtName)
            }
        }
    }

    // 订阅观测站点列表
    override fun observerStationsByDistrictName(districtName: String): Flow<List<Station>> {
        return database.stationDao.selectByDistrictName(districtName).asFlow().map {
            it.executeAsList().map { stationEntity ->
                stationEntity.asAppModel()
            }
        }
    }

    // 根据观测站点名称获取观测站点ID
    override suspend fun getStationIdByName(stationName: String): String? = catchCall {
        val result = database.stationDao
            .selectByStationName(stationName).executeAsList()
        // 收藏站点永远不要存储 G0000 自动定位的站点id, 如果需要存储自动定位的非深圳城市,则存储其请求参数(不需要站点id)
        when {
            result.isEmpty() -> null
            result[0].stationId != "G0000" -> result[0].stationId
            else -> result[1].stationId
        }
    }

    // SelectedStation 用于记录上次使用的观测站点
    override fun insertSelectedStation(selectedStation: SelectedStation) {
        database.selectedStationDao.insertFull(selectedStation.asEntity())
    }

    override fun observerSelectedStation(): Flow<SelectedStation?> {
        return database.selectedStationDao
            .selectAll()
            .asFlow().map {
                it.executeAsOneOrNull()?.asAppModel()
            }
    }

    override fun insertFavoriteStationParam(favoriteStationsParam: FavoriteStationParams) {
        database.favoriteStationParamsDao.insertFull(favoriteStationsParam.asEntity())
    }

    override fun observerFavoriteStationParams(): Flow<List<FavoriteStationParams>> {
        return database.favoriteStationParamsDao
            .selectAll()
            .asFlow().map {
                it.executeAsList().map { favoriteStationParamsEntity ->
                    favoriteStationParamsEntity.asAppModel()
                }
            }
    }

    override fun deleteFavoriteStationParamByStationName(stationName: String) {
        database.favoriteStationParamsDao.deleteByStationName(stationName)
    }

    override fun getFavoriteStationParamByStationName(
        stationName: String
    ): FavoriteStationParams {
        return database.favoriteStationParamsDao
            .selectByStationName(stationName)
            .executeAsOne()
            .asAppModel()
    }

    override fun insertFavoriteStationsWeather(
        favoriteStationsWeather: List<FavoriteStationWeather>
    ) {
        database.favoriteStationWeatherDao.apply {
            transaction {
                favoriteStationsWeather.forEach { favoriteWeatherEntity ->
                    insertFull(favoriteWeatherEntity.asEntity())
                }
            }
        }
    }

    override fun observerFavoriteStationsWeather(): Flow<List<FavoriteStationWeather>> {
        return database.favoriteStationWeatherDao.selectAll().asFlow().map {
            it.executeAsList().map { favoriteWeatherEntity ->
                favoriteWeatherEntity.asAppModel()
            }
        }
    }

    override fun deleteFavoriteStationsWeatherByStationName(stationName: String) {
        database.favoriteStationWeatherDao.deleteByStationName(stationName)
    }

    override fun deleteAllFavoriteStationsWeather() {
        database.favoriteStationWeatherDao.deleteAll()
    }

    override fun insertCityToFavorite(city: City) {
        database.favoriteCitiesDao.insertFull(city.asAppFavoriteCityEntity())
    }

    override fun observerFavoriteCities(): Flow<List<FavoriteCity>> {
        return database.favoriteCitiesDao.selectAll().asFlow().map {
            it.executeAsList().map { favoriteCityEntity ->
                favoriteCityEntity.asAppModel()
            }
        }
    }

    override fun deleteFavoriteCityByCityId(cityId: String) {
        database.favoriteCitiesDao.deleteByCityId(cityId)
    }

    override fun insertFavoriteCitiesWeather(
        favoriteCitiesWeather: List<FavoriteCityWeather>
    ) {
        database.favoriteCitiesWeatherDao.apply {
            transaction {
                favoriteCitiesWeather.forEach { favoriteCityWeather ->
                    insertFull(favoriteCityWeather.asEntity())
                }
            }
        }
    }

    override fun observerFavoriteCityWeather(): Flow<List<FavoriteCityWeather>> {
        return database.favoriteCitiesWeatherDao.selectAll().asFlow().map {
            it.executeAsList().map { favoriteCityWeatherEntity ->
                favoriteCityWeatherEntity.asAppModel()
            }
        }
    }

    override fun deleteFavoriteCityWeatherByCityId(cityId: String) {
        return database.favoriteCitiesWeatherDao.deleteByCityId(cityId)
    }

    override fun deleteAllFavoriteCityWeather() {
        database.favoriteCitiesWeatherDao.deleteAll()
    }

    override fun deleteFavoriteCityWithWeather(cityId: String) {
        database.transaction {
            database.favoriteCitiesDao.deleteByCityId(cityId)
            database.favoriteCitiesWeatherDao.deleteByCityId(cityId)
        }
    }

    override fun deleteFavoriteStationWithWeather(stationName: String) {
        database.transaction {
            database.favoriteStationWeatherDao.deleteByStationName(stationName)
            database.favoriteStationParamsDao.deleteByStationName(stationName)
        }
    }

    override suspend fun <T> catchCall(block: suspend () -> T): T = exceptionCall("错误来自数据库模块", block)
}