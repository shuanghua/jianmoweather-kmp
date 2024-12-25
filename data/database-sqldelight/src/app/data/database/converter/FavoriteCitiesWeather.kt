package app.data.database.converter

import app.data.model.FavoriteCityWeather
import app.data.sqldelight.FavoriteCitiesWeatherEntity

internal fun FavoriteCityWeather.asEntity(): FavoriteCitiesWeatherEntity {
    return FavoriteCitiesWeatherEntity(
        cityId = cityId,
        cityName = cityName,
        provinceName = provinceName,
        isAutoLocation = isAutoLocation,
        currentT = currentT,
        bgImageNew = bgImageNew,
        iconUrl = iconUrl,
    )
}

internal fun FavoriteCitiesWeatherEntity.asAppModel(): FavoriteCityWeather {
    return FavoriteCityWeather(
        cityId = cityId,
        cityName = cityName,
        provinceName = provinceName,
        isAutoLocation = isAutoLocation,
        currentT = currentT,
        bgImageNew = bgImageNew,
        iconUrl = iconUrl,
    )
}