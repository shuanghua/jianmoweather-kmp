package app.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import app.data.database.dao.*
import app.data.database.entity.*


@Database(
    entities = [
        WeatherEntity::class,
        AlarmIconEntity::class,
        ConditionEntity::class,
        ExponentEntity::class,
        OneHourEntity::class,
        OneDayEntity::class,
        HalfHourEntity::class,
        FavoriteCityEntity::class,
        FavoriteCityWeatherEntity::class,
        FavoriteStationWeatherEntity::class,
        FavoriteStationParamsEntity::class,
        ProvinceEntity::class,
        CityEntity::class,
        DistrictEntity::class,
        StationEntity::class,
        SelectedStationEntity::class
    ],
    version = 1,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun provinceDao(): ProvinceDao
    abstract fun cityDao(): CityDao
    abstract fun districtDao(): DistrictDao
    abstract fun stationDao(): StationDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppRoomDatabase> {
    override fun initialize(): AppRoomDatabase
}

internal const val dbFileName = "app.db"
