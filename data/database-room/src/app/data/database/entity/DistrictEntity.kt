package app.data.database.entity

import androidx.room.Entity
import androidx.room.Index
import app.data.model.District

@Entity(
    tableName = "district",
    primaryKeys = ["name"],
    indices = [(Index("name"))]
)
data class DistrictEntity(
    val name: String
)

fun DistrictEntity.asExternalModel() = District(
    name = name
)