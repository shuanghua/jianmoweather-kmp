package app.data.network.converter

import app.data.model.FavoriteCityWeather
import app.data.network.api.Api
import app.data.network.model.NetworkFavoriteCityWeather

internal fun NetworkFavoriteCityWeather.asAppModel(): FavoriteCityWeather {
    return FavoriteCityWeather(
        cityName = cityName,
        cityId = cityId,
        currentT = currentT,
        bgImageNew = Api.getImageUrl(bgImage),
        iconUrl = Api.getImageUrl(icon)
    )
}