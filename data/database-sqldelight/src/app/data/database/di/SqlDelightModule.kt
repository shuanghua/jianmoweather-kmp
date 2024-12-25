package app.data.database.di

import app.data.database.di.SqlDriverFactory
import app.data.database.LocalDataSource
import org.koin.dsl.module

val sqlDelightDatabaseModule = module {
    single { AppDatabase(SqlDriverFactory()) }
    single<LocalDataSource> { LocalDataSourceImpl(get()) }
}