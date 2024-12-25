package app.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteCityWeatherList(@SerialName("list") val list: List<NetworkFavoriteCityWeather>)

@Serializable
data class NetworkFavoriteCityWeather(
	@SerialName("cityName") val cityName: String,
	@SerialName("cityId") val cityId: String,
	@SerialName("t") val currentT: String,
	@SerialName("wtypeCn") val wtypeCn: String, // 多云
	@SerialName("wtypeIcon") val icon: String, // /webcache/appimagesnew/weatherIcon/09.png
	@SerialName("cityWeatherSmallbg") val bgImage: String // /webcache/appimagesnew/bgSmall/city_smallBg2_10.png
)