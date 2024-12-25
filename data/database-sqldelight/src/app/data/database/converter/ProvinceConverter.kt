package app.data.database.converter

import app.data.model.Province
import app.data.sqldelight.ProvinceEntity
import kotlin.jvm.JvmName

@JvmName("asAppProvinceModel")
internal fun ProvinceEntity.asAppModel(): Province {
    return Province(name = province_name)
}

internal fun Province.asEntity(): ProvinceEntity {
    return ProvinceEntity(province_name = name)
}