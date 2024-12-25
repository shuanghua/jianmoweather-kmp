package app.data.database.converter

import app.data.model.SelectedStation
import app.data.sqldelight.SelectedStationEntity

internal fun SelectedStationEntity.asAppModel(): SelectedStation {
    return SelectedStation(
        districtName = districtName,
        stationId = stationId,
        stationName = stationName,
        isLocation = isLocation,
    )
}


internal fun SelectedStation.asEntity(): SelectedStationEntity {
    return SelectedStationEntity(
        screen = "HomeScreen",
        districtName = districtName,
        stationId = stationId,
        stationName = stationName,
        isLocation = isLocation,
    )
}