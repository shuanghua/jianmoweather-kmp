package app.data.database.converter

import app.data.model.FavoriteCity
import app.data.sqldelight.FavoriteCitiesEntity

internal fun FavoriteCitiesEntity.asAppModel(): FavoriteCity {
    return FavoriteCity(
        cityId = cityId,
        cityName = cityName,
        provinceName = provinceName,
    )
}

internal fun FavoriteCity.toEntity(): FavoriteCitiesEntity {
    return FavoriteCitiesEntity(
        cityId = cityId,
        cityName = cityName,
        provinceName = provinceName,
    )
}

