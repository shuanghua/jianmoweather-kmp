package app.data.datastore

import app.data.model.Location
import app.data.model.ThemeConfig
import kotlinx.coroutines.flow.Flow

interface AppDataStoreDataSource {
    fun getLocationFlow(): Flow<Location>
    fun theme(): Flow<ThemeConfig>
    suspend fun setThemeMode(themeConfig: ThemeConfig)
    suspend fun saveLocation(location: Location)
}