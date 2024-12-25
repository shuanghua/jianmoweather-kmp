package app.data.database.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import app.data.sqldelight.SqlDelightDatabase
import org.koin.mp.KoinPlatform.getKoin

actual class SqlDriverFactory {
    //    private val app: Application by KoinJavaComponent.inject(Application::class.java)
    private val context = getKoin().get<Context>()
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(SqlDelightDatabase.Schema, context, dbFileName)
    }
}