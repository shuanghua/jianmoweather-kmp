package app.data.repo

import AppDispatcher
import app.data.datastore.AppDataStoreDataSource
import app.data.location.LocationDataSource
import app.data.model.Location
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

interface LocationRepository {
    suspend fun getLocation(): Location
    suspend fun getNetworkLocationData(): Location?
    suspend fun getLocalLocationData(): Location
    suspend fun saveLocationToDataStore(location: Location)
}

class LocationRepositoryImpl(
    private val network: LocationDataSource,
    private val dataStore: AppDataStoreDataSource,
    private val dispatchers: AppDispatcher,
) : LocationRepository {

    override suspend fun getLocation(
    ): Location = supervisorScope {
        val newData = getNetworkLocationData()
        if (newData != null) {
            return@supervisorScope newData.also { saveLocationToDataStore(it) } // 网络定位成功，保存到本地
        }
        val oldData = getLocalLocationData()
        if (oldData.latitude.isNotEmpty()) {
            return@supervisorScope oldData
        } else {
            defaultLocation.also { saveLocationToDataStore(it) }
        }
    }

    override suspend fun getNetworkLocationData(
    ): Location? = withContext(dispatchers.io) {
        val result: Result<Location> = network.getLocationResult()
        result.getOrNull()
    }

    override suspend fun getLocalLocationData(
    ): Location = withContext(dispatchers.io) {
        dataStore.getLocationFlow().first()
    }

    override suspend fun saveLocationToDataStore(
        location: Location,
    ) = withContext(dispatchers.io) {
        dataStore.saveLocation(location)
    }

    companion object {
        private val defaultLocation = Location(
            latitude = "22.538854",
            longitude = "114.010341",
            cityName = "深圳市",
            district = "福田区",
        )
    }
}