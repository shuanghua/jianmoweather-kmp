package app.data.database

interface AppDatabaseFactory {
    fun createAppDatabase(): AppRoomDatabase
}