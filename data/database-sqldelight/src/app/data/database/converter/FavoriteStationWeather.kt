package app.data.database.converter

import app.data.model.FavoriteStationWeather
import app.data.sqldelight.FavoriteStationWeatherEntity

fun FavoriteStationWeatherEntity.asAppModel() = FavoriteStationWeather(
    cityId = cityId,
    stationName = stationName,
    temperature = temperature,
    weatherStatus = weatherStatus,
    weatherIcon = weatherIcon,
    rangeT = rangeT
)

fun FavoriteStationWeather.asEntity() = FavoriteStationWeatherEntity(
    cityId = cityId,
    stationName = stationName,
    temperature = temperature,
    weatherStatus = weatherStatus,
    weatherIcon = weatherIcon,
    rangeT = rangeT
)