package app.data.database.dao

import androidx.room.*
import app.data.database.entity.SelectedStationEntity
import app.data.database.entity.StationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<StationEntity>)

    @Transaction
    @Query("SELECT * FROM station WHERE districtName = :districtName")
    fun observerStations(districtName: String): Flow<List<StationEntity>>

    @Query("SELECT stationId FROM station WHERE stationName = :stationName")
    suspend fun queryStationId(stationName: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSelectedStation(selectedStation: SelectedStationEntity)

    @Query("SELECT * FROM selected_station")
    fun getLastStation(): Flow<SelectedStationEntity?>//判定上次站点是否是自动定位站点

    @Query("SELECT stationId FROM station WHERE stationName = :stationName AND districtName != :districtName")
    suspend fun getStationIdByName(stationName: String, districtName: String): String?
}