package app.data.database.di

import app.cash.sqldelight.db.SqlDriver

expect class SqlDriverFactory() {
    fun createDriver(): SqlDriver
}