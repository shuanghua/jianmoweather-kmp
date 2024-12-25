package app.data.database.converter

import app.data.model.District
import app.data.sqldelight.DistrictEntity

internal fun District.asEntity(): DistrictEntity {
    return DistrictEntity(name = name)
}

internal fun DistrictEntity.asAppModel(): District {
    return District(name = name)
}