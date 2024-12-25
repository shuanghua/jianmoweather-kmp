package app.data.network.converter

import app.data.model.*
import app.data.network.api.Api
import app.data.network.model.NetworkWeatherModel
import kotlin.jvm.JvmName

@JvmName("asAppWeatherModel")
fun NetworkWeatherModel.asAppModel(): Weather {
    val airQuality = cleanAirQuality()
    val airQualityIcon = cleanAirQualityIcon()
    val lunarCalendar = cleanCalendar()
    val (sunUp, sunDown) = cleanSunTime()
    return Weather(
        cityId = cityId,
        cityName = cityName,
        temperature = t,
        stationName = stationName,
        stationId = obtId,
        description = desc,
        lunarCalendar = lunarCalendar,
        sunUp = sunUp,
        sunDown = sunDown,
        airQuality = airQuality,
        airQualityIcon = airQualityIcon,
        alarmIcons = asAlarmIconList(),
        oneDays = asOneDayList(),
        oneHours = asOneHourList(),
        conditions = mapToConditionList(),
        exponents = mapToExponentList()
    )
}

internal fun Weather.asFavoriteStationWeather(): FavoriteStationWeather {
    return FavoriteStationWeather(
        cityId = cityId,
        stationName = stationName,
        temperature = temperature,
        weatherStatus = oneHours[0].rain,
        weatherIcon = Api.getImageUrl(oneHours[0].icon),
        rangeT = ""
    )
}


// network model to app model
private fun NetworkWeatherModel.asAlarmIconList(): List<AlarmIcon> {
    var alarmsIconUrl = ""
    return alarmList.mapIndexed { index, alarm ->
        if (alarm.icon != "") {
            alarmsIconUrl = Api.getImageUrl(alarm.icon)
        }
        AlarmIcon(
            id = index,
            cityId = cityId,
            iconUrl = alarmsIconUrl,
            name = alarm.name
        )
    }
}

private fun NetworkWeatherModel.asOneHourList(): List<OneHour> {
    return hourList.mapIndexed { index, oneHour ->
        OneHour(
            id = index,
            cityId = cityId,
            hour = oneHour.hour,
            t = oneHour.t,
            icon = oneHour.weatherpic,
            rain = oneHour.rain
        )
    }
}

private fun NetworkWeatherModel.asOneDayList(): List<OneDay> {
    return dayList.mapIndexed { index, oneDay ->
        OneDay(
            id = index,
            cityId = cityId,
            date = oneDay.date.orEmpty(),
            week = oneDay.week.orEmpty(),
            desc = oneDay.desc.orEmpty(),
            t = "${oneDay.minT}~${oneDay.maxT}",
            minT = oneDay.minT.orEmpty(),
            maxT = oneDay.maxT.orEmpty(),
            iconUrl = Api.getImageUrl(oneDay.wtype.orEmpty())
        )
    }
}

private fun NetworkWeatherModel.mapToConditionList(): List<Condition> {
    return element?.let {
        val conditions = ArrayList<Condition>()
        conditions.add(Condition(id = 0, cityId = cityId, name = "湿度", value = it.rh))
        conditions.add(Condition(id = 1, cityId = cityId, name = "气压", value = it.pa))
        conditions.add(Condition(id = 2, cityId = cityId, name = it.wd, value = it.ws))
        conditions.add(Condition(id = 3, cityId = cityId, name = "24H降雨量", value = it.r24h))
        conditions.add(Condition(id = 4, cityId = cityId, name = "1H降雨量", value = it.r01h))
        conditions
    } ?: emptyList()
}

private fun NetworkWeatherModel.mapToExponentList(): List<Exponent> {
    val healthExponents = ArrayList<Exponent>()
    livingIndex?.apply {
        shushidu?.apply {
            healthExponents += Exponent(
                id = 0,
                cityId = cityId,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
        }
        gaowen?.apply {
            healthExponents += Exponent(
                id = 1,
                cityId = cityId,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
        }
        ziwaixian?.apply {
            healthExponents += Exponent(
                id = 2,
                cityId = cityId,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
        }
        co?.apply {
            healthExponents += Exponent(
                id = 3,
                cityId = cityId,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
        }
        meibian?.apply {
            healthExponents += Exponent(
                id = 4,
                cityId = cityId,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
        }
        chenlian?.apply {
            healthExponents += Exponent(
                id = 5,
                cityId = cityId,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
        }
        luyou?.apply {
            healthExponents += Exponent(
                id = 6,
                cityId = cityId,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
        }
        liugan?.apply {
            healthExponents += Exponent(
                id = 7,
                cityId = cityId,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
        }
        chuanyi?.apply {
            healthExponents += Exponent(
                id = 8,
                cityId = cityId,
                title = title,
                level = level,
                levelDesc = level_desc,
                levelAdvice = level_advice
            )
        }
    }
    return healthExponents
}