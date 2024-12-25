package app.data.repo

import app.data.datastore.AppDataStoreDataSource
import app.data.model.ThemeConfig
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getTheme(): Flow<ThemeConfig>
    suspend fun setTheme(themeConfig: ThemeConfig)

}

internal class SettingsRepositoryImpl(
    private val dataStore: AppDataStoreDataSource,
) : SettingsRepository {

    override fun getTheme(): Flow<ThemeConfig> = dataStore.theme()
    override suspend fun setTheme(themeConfig: ThemeConfig) = dataStore.setThemeMode(themeConfig)

}