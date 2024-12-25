package app.data.database.converter

import app.data.model.Station
import app.data.sqldelight.StationEntity

internal fun StationEntity.asAppModel(): Station {
    return Station(
        districtName = districtName,
        stationId = stationId,
        stationName = stationName,
        isSelected = isSelected,
    )
}

internal fun Station.asEntity(): StationEntity {
    return StationEntity(
        districtName = districtName,
        stationId = stationId,
        stationName = stationName,
        isSelected = isSelected,
    )
}