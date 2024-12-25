package app.data.location

import app.data.model.Location

interface LocationDataSource {
    suspend fun getLocationResult(): Result<Location>
}

expect fun getLocationDataSource(): LocationDataSource