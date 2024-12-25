package app.data.database.di

import app.data.sqldelight.SqlDelightDatabase
import app.data.sqldelight.WeatherEntity

internal const val dbFileName = "app.db"

/**
 * 用于封装 数据库 对象以及 DAO 对象
 * 同时方便在业务代码之外进行测试
 *
 * 不建议在其它模块直接使用，避免模块外引用 sqldelight 相关代码，
 * 包括 sqldelight 生成的代码，方便以后迁移到其他数据库时无需修改外部代码
 */
internal class AppDatabase(driverFactory: SqlDriverFactory) {
    private val database = SqlDelightDatabase(driverFactory.createDriver())
    val weatherDao = database.weatherEntityQueries
    val alarmIconDao = database.alarmIconEntityQueries
    val oneDayDao = database.oneDayEntityQueries
    val oneHourDao = database.oneHourEntityQueries
    val conditionDao = database.conditionEntityQueries
    val exponentDao = database.exponentEntityQueries

    val districtDao = database.districtEntityQueries
    val stationDao = database.stationEntityQueries
    val selectedStationDao = database.selectedStationEntityQueries

    val favoriteStationParamsDao = database.favoriteStationParamsEntityQueries
    val favoriteStationWeatherDao = database.favoriteStationWeatherEntityQueries
    val favoriteCitiesDao = database.favoriteCitiesEntityQueries
    val favoriteCitiesWeatherDao = database.favoriteCitiesWeatherEntityQueries

    val provinceDao = database.provinceEntityQueries
    val cityDao = database.cityEntityQueries


    fun test() {
        val weatherDao = database.weatherEntityQueries
        val result: List<WeatherEntity> = weatherDao.selectAll().executeAsList()
        println("sqldelight database result: $result")
    }

    fun transaction(block:() -> Unit) = database.transaction {
        block()
    }
}

