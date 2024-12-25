package app.data.location.di

import app.data.location.LocationDataSource
import app.data.location.getLocationDataSource
import org.koin.dsl.module

val locationModule = module {
    single<LocationDataSource> { getLocationDataSource() }
}