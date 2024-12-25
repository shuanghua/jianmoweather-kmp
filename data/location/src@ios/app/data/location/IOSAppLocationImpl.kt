package app.data.location

import app.data.model.Location

actual fun getLocationDataSource(): LocationDataSource = IOSAppLocationImpl()

private class IOSAppLocationImpl : LocationDataSource {
    override suspend fun getLocationResult(): Result<Location> { // 一次性监听
        return Result.success(
            Location(
                "Network Location",
                "Network Location",
                "Network Location",
                "Network Location",
                "Network Location",
            )
        )
    }
}