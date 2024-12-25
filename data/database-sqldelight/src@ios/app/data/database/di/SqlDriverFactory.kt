package app.data.database.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.data.sqldelight.SqlDelightDatabase

actual class SqlDriverFactory{
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(SqlDelightDatabase.Schema, dbFileName)
    }
}