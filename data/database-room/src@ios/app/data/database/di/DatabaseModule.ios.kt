package app.data.database.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import app.data.database.AppRoomDatabase
import app.data.database.dbFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

// database ios
actual class AppDatabaseFactory {
    actual fun createAppDatabase(): AppRoomDatabase {
        val dbFilePath = documentDirectory() + "/$dbFileName"
        return Room.databaseBuilder<AppRoomDatabase>(
            name = dbFilePath,
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory?.path)
    }
}
