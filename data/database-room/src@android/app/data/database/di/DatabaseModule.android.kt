package app.data.database.di

import android.app.Application
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import app.data.database.AppRoomDatabase
import app.data.database.dbFileName
import kotlinx.coroutines.Dispatchers
import org.koin.java.KoinJavaComponent.inject

// database android
// androidMain/DatabaseModule.android.kt
actual class AppDatabaseFactory {
    private val app: Application by inject(Application::class.java)
    actual fun createAppDatabase(): AppRoomDatabase {
        println("Network: Creating database")
        val dbFilePath = app.getDatabasePath(dbFileName)
        return try {
            val s = "fsdfd"
            s.ifBlank {  }
            Room.databaseBuilder<AppRoomDatabase>(
                context = app,
                name = dbFilePath.absolutePath,
            )
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .build()
        } catch (e: Exception) {
            throw RuntimeException("Network:Room 错误：${e.message}")
        }
    }
}

