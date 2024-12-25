package app.data.database.di

import app.data.database.AppRoomDatabase
import org.koin.dsl.module

// commonMain/DatabaseModule.kt
expect class AppDatabaseFactory() {
    fun createAppDatabase(): AppRoomDatabase
}

val databaseModule = module {
//    single<AppDatabase> { AppDatabaseFactory().createAppDatabase() }
//    single { get<AppDatabase>().weatherDao() }
//    single { get<AppDatabase>().favoriteDao() }
//    single { get<AppDatabase>().provinceDao() }
//    single { get<AppDatabase>().cityDao() }
//    single { get<AppDatabase>().districtDao() }
//    single { get<AppDatabase>().stationDao() }
}