package app.data.database.converter

import app.data.model.*
import app.data.sqldelight.WeatherEntity
import app.data.sqldelight.home.*
import kotlin.jvm.JvmName

@JvmName("asAppModelAlarmIconEntity")
internal fun List<AlarmIconEntity>.asAppModel(): List<AlarmIcon> {
    return this.map {
        AlarmIcon(
            id = it.id.toInt(), cityId = it._cityId, iconUrl = it.iconUrl, name = it.name
        )
    }
}

@JvmName("asAppModelOneDayEntity")
internal fun List<OneDayEntity>.asAppModel(): List<OneDay> {
    return map {
        OneDay(
            id = it.id.toInt(),
            cityId = it._cityId,
            date = it.date,
            week = it.week,
            desc = it.desc,
            t = it.t,
            minT = it.minT,
            maxT = it.maxT,
            iconUrl = it.iconName
        )
    }
}

@JvmName("asAppModelOneHourEntity")
internal fun List<OneHourEntity>.asAppModel(): List<OneHour> {
    return map {
        OneHour(
            id = it.id.toInt(),
            cityId = it._cityId,
            hour = it.hour,
            t = it.t,
            icon = it.icon,
            rain = it.rain
        )
    }
}

@JvmName("asAppModelConditionEntity")
internal fun List<ConditionEntity>.asAppModel(): List<Condition> {
    return map {
        Condition(
            id = it.id.toInt(), cityId = it._cityId, name = it.name, value = it.desc
        )
    }
}

@JvmName("asAppModelExponentEntity")
internal fun List<ExponentEntity>.asAppModel(): List<Exponent> {
    return map {
        Exponent(
            id = it.id.toInt(),
            cityId = it._cityId,
            title = it.title,
            level = it.level,
            levelDesc = it.levelDesc,
            levelAdvice = it.levelAdvice
        )
    }
}

// app model to entity model
//fun Weather.asEntities(): WeatherEntities {
//    return WeatherEntities(
//        weatherEntity = asWeatherEntity(),
//        alarmEntities = asAlarmEntityList(),
//        oneDayEntities = asOneDayEntityList(),
//        onHourEntities = asOneHourEntityList(),
//        conditionEntities = asConditionEntityList(),
//        exponentEntities = asExponentEntityList(),
//    )
//}

internal fun Weather.asWeatherEntity() = WeatherEntity(
    single = 1,
    cityId = cityId,
    cityName = cityName,
    temperature = temperature,
    stationName = stationName,
    stationId = stationId,
    description = description,
    lunarCalendar = lunarCalendar,
    sunUp = sunUp,
    sunDown = sunDown,
    airQuality = airQuality,
    airQualityIcon = airQualityIcon,
)


internal fun Weather.asAlarmIconEntity(): List<AlarmIconEntity> {
    return alarmIcons.map {
        AlarmIconEntity(
            id = it.id.toLong(),
            _cityId = it.cityId,
            iconUrl = it.iconUrl,
            name = it.name,
            weather_single = 1,
        )
    }
}

internal fun Weather.asOneHourEntityList(): List<OneHourEntity> {
    return oneHours.map {
        OneHourEntity(
            id = it.id.toLong(),
            _cityId = it.cityId,
            hour = it.hour,
            t = it.t,
            icon = it.icon,
            rain = it.rain,
            weather_single = 1
        )
    }
}

internal fun Weather.asOneDayEntityList(): List<OneDayEntity> {
    return oneDays.map {
        OneDayEntity(
            id = it.id.toLong(),
            _cityId = it.cityId,
            date = it.date,
            week = it.week,
            desc = it.desc,
            t = it.t,
            minT = it.minT,
            maxT = it.maxT,
            iconName = it.iconUrl,
            weather_single = 1,

            )
    }
}

internal fun Weather.asConditionEntityList(): List<ConditionEntity> {
    return conditions.map {
        ConditionEntity(
            id = it.id.toLong(),
            _cityId = it.cityId,
            name = it.name,
            desc = it.value,
            weather_single = 1,

            )
    }
}

internal fun Weather.asExponentEntityList(): List<ExponentEntity> {
    return exponents.map {
        ExponentEntity(
            id = it.id.toLong(),
            _cityId = it.cityId,
            title = it.title,
            level = it.level,
            levelDesc = it.levelDesc,
            levelAdvice = it.levelAdvice,
            weather_single = 1,
        )
    }
}
