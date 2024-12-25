package app.data.database.converter

import app.data.model.City
import app.data.model.FavoriteCity
import app.data.sqldelight.CityEntity
import app.data.sqldelight.FavoriteCitiesEntity


internal fun City.asEntity(): CityEntity {
    return CityEntity(
        city_id = cityId,
        province_name = provinceName,
        city_name = cityName
    )
}

internal fun CityEntity.asAppModel(): City {
    return City(
        cityId = city_id,
        provinceName = province_name,
        cityName = city_name
    )
}

internal fun City.asAppFavoriteCityEntity(): FavoriteCitiesEntity {
    return FavoriteCitiesEntity(
        cityId = cityId,
        cityName = cityName,
        provinceName = provinceName,
    )
}