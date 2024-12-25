package app.data.database.converter

import app.data.model.FavoriteStationParams
import app.data.sqldelight.FavoriteStationParamsEntity

internal fun FavoriteStationParams.asEntity(): FavoriteStationParamsEntity {
    return FavoriteStationParamsEntity(
        isAutoLocation = isAutoLocation,
        cityId = cityId,
        stationId = stationId,
        stationName = stationName,
        latitude = latitude,
        longitude = longitude,
        pcity = cityName,
        parea = district,
    )
}

internal fun FavoriteStationParamsEntity.asAppModel(): FavoriteStationParams {
    return FavoriteStationParams(
        isAutoLocation = isAutoLocation,
        cityId = cityId,
        stationId = stationId,
        stationName = stationName,
        latitude = latitude,
        longitude = longitude,
        cityName = pcity,
        district = parea,
    )
}