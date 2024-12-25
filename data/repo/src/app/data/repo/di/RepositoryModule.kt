package app.data.repo.di

import TestRepo
import app.data.database.di.sqlDelightDatabaseModule
import app.data.datastore.di.datastoreModule
import app.data.location.di.locationModule
import app.data.network.di.networkModule
import app.data.repo.*
import org.koin.dsl.module

// repo common module
val repoModule = module {
    includes(sqlDelightDatabaseModule, networkModule, datastoreModule, locationModule)
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get(), get()) }
    single<FavoritesRepository> { FavoritesRepositoryImpl(get(), get(), get()) }
    single<StationRepository> { StationRepositoryImpl(get(), get()) }
    single<ProvinceCityRepository> { ProvinceCityRepositoryImpl(get(), get(), get()) }
    single<LocationRepository> { LocationRepositoryImpl(get(), get(), get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    single<TestRepo> { TestRepo(get()) }
}
