package app.data.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import app.data.datastore.AppDataStoreDataSource
import app.data.model.Location
import app.data.model.ThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AppDataStoreDataSourceImpl(
    private val dataStore: DataStore<Preferences>
) : AppDataStoreDataSource {

    // key and type
    private val latitudeKey = stringPreferencesKey("latitude")
    private val longitudeKey = stringPreferencesKey("longitude")
    private val cityNameKey = stringPreferencesKey("city_name")
    private val districtKey = stringPreferencesKey("district")
    private val addressKey = stringPreferencesKey("address")
    private val themeModeKey = intPreferencesKey("theme_mode")


    override fun getLocationFlow(): Flow<Location> {
        return dataStore.data.map {
            Location(
                latitude = it[latitudeKey] ?: "22.546054",
                longitude = it[longitudeKey] ?: "114.025974",
                cityName = it[cityNameKey] ?: "深圳市",
                district = it[districtKey] ?: "福田区",
                address = it[addressKey] ?: ""
            )
        }
    }


    override fun theme(): Flow<ThemeConfig> {
        return dataStore.data.map {
            when (it[themeModeKey]) {
                0 -> ThemeConfig.FOLLOW_SYSTEM
                1 -> ThemeConfig.LIGHT
                2 -> ThemeConfig.Dark
                else -> ThemeConfig.FOLLOW_SYSTEM
            }
        }
    }

    override suspend fun setThemeMode(themeConfig: ThemeConfig) {
        dataStore.edit {
            it[themeModeKey] = when (themeConfig) {
                ThemeConfig.FOLLOW_SYSTEM -> 0
                ThemeConfig.LIGHT -> 1
                ThemeConfig.Dark -> 2
            }
        }
    }

    override suspend fun saveLocation(location: Location) {
        dataStore.edit {
            it[latitudeKey] = location.latitude
            it[longitudeKey] = location.longitude
            it[cityNameKey] = location.cityName
            it[districtKey] = location.district
            it[addressKey] = location.address
        }
    }
}