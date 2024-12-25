package app.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
	tableName = "favorite_station_weather"
)
data class FavoriteStationWeatherEntity(
	val cityId: String = "",
	@PrimaryKey val stationName: String = "",
	val temperature: String = "",
	val weatherStatus: String = "",
	val weatherIcon: String = "",
	val rangeT: String = "",
)
