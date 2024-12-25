package app.data.repo

import app.data.database.LocalDataSource
import app.data.model.District
import app.data.model.DistrictStation
import app.data.model.SelectedStation
import app.data.model.Station
import app.data.network.NetworkDataSource
import exception.AppLog
import kotlinx.coroutines.flow.Flow

interface StationRepository {
    suspend fun updateStationWithDistrict(
        cityId: String,
        stationId: String,
        latitude: String,
        longitude: String
    )

    fun observerDistricts(): Flow<List<District>>
    fun observerStations(districtName: String): Flow<List<Station>>

    suspend fun saveSelectedStation(selectedStation: SelectedStation)
    fun observerSelectedStation(): Flow<SelectedStation?>
    suspend fun getStationIdByName(stationName: String): String?
}

class StationRepositoryImpl(
    private val network: NetworkDataSource,
    private val database: LocalDataSource,
) : StationRepository {

    override suspend fun updateStationWithDistrict(
        cityId: String,
        stationId: String,
        latitude: String,
        longitude: String
    ) {
        val districts: DistrictStation = network.getDistrictsWithStations(
            cityId = cityId,
            obtId = stationId,
            latitude = latitude,
            longitude = longitude
        )
        AppLog.d("database: ${districts.districts}")
        database.insertDistricts(districts.districts)
        database.insertStations(districts.stations)
    }

    // 区县
    override fun observerDistricts(): Flow<List<District>> {
        return database.observerDistricts()
    }

    override fun observerStations(districtName: String): Flow<List<Station>> {
        return database.observerStationsByDistrictName(districtName)
    }

    // 手动选择观测站，
    override suspend fun saveSelectedStation(selectedStation: SelectedStation) {
        database.insertSelectedStation(selectedStation)
    }

    // 首页调用
    override fun observerSelectedStation(): Flow<SelectedStation?> {
        return database.observerSelectedStation()
    }

    // 首页 menu 调用
    override suspend fun getStationIdByName(stationName: String): String? {
        return database.getStationIdByName(stationName)
    }
}