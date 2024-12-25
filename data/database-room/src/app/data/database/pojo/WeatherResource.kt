package app.data.database.pojo

import androidx.room.Embedded
import androidx.room.Relation
import app.data.database.entity.*
import app.data.model.Weather

/**
 * 首页的所需要的信息,
 * 此数据类称为 pojo，通常用来存放从数据库读取的多表数据
 * 普通 bean 类: 需要通过 setter 来设置,所以需要公开的 val
 *
 * Embedded : 用于对象
 * Relation : 用于集合
 */
data class WeatherResource(
    @Embedded
    val weatherEntity: WeatherEntity,

    @Relation(
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val alarmIcons: List<AlarmIconEntity>,


    @Relation(
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val oneDays: List<OneDayEntity>,


    @Relation(
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val conditions: List<ConditionEntity>,


    @Relation(
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val oneHours: List<OneHourEntity>,


    @Relation(
        entityColumn = "_cityId",
        parentColumn = "cityId"
    )
    val exponents: List<ExponentEntity>,
)

/**
 * 将数据库查询出的数据转换成 ui Model
 * 通常在 Repository 中调用
 */
fun WeatherResource.asExternalModel(): Weather = Weather(
    cityId = weatherEntity.cityId,
    cityName = weatherEntity.cityName,
    temperature = weatherEntity.temperature,
    description = weatherEntity.description,
    airQuality = weatherEntity.airQuality,
    airQualityIcon = weatherEntity.airQualityIcon,
    lunarCalendar = weatherEntity.lunarCalendar,
    stationName = weatherEntity.stationName,
    stationId = weatherEntity.stationId,
    sunUp = weatherEntity.sunUp,
    sunDown = weatherEntity.sunDown,

    alarmIcons = alarmIcons.map(AlarmIconEntity::asExternalModel),
    oneDays = oneDays.map(OneDayEntity::asExternalModel),
    oneHours = oneHours.map(OneHourEntity::asExternalModel),
    conditions = conditions.map(ConditionEntity::asExternalModel),
    exponents = exponents.map(ExponentEntity::asExternalModel)
)