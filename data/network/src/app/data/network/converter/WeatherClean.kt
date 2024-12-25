package app.data.network.converter

import app.data.network.api.Api
import app.data.network.model.NetworkWeatherModel

internal fun NetworkWeatherModel.cleanSunTime(): Pair<String, String> {
	var sunUp = sunTime?.sunup ?: ""
	var sunDown = sunTime?.sundown ?: ""
	if (sunUp[0] != '0') {//10点到晚上11：59
		sunUp = sunDown.also { sunDown = sunUp }
	}
	return Pair(sunUp, sunDown)
}

internal fun NetworkWeatherModel.cleanCalendar(): String {
	return lunar?.run { "$info1 $info2 $info3 $info4 $info5" } ?: ""
}

internal fun NetworkWeatherModel.cleanAirQuality(): String {
	return aqiObj?.aqi?.let { it.aqi + "·" + it.aqic } ?: ""
}

internal fun NetworkWeatherModel.cleanAirQualityIcon(): String {
	return aqiObj?.aqi?.let { Api.getImageUrl(it.icon) } ?: ""
}
