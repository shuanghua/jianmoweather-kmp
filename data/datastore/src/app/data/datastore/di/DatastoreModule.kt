package app.data.datastore.di

import app.data.datastore.AppDataStoreDataSource
import app.data.datastore.getAppDataStore
import org.koin.dsl.module

val datastoreModule = module {
    single<AppDataStoreDataSource> { AppDataStoreDataSourceImpl(getAppDataStore()) }
}