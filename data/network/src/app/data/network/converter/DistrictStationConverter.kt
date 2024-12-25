package app.data.network.converter

import app.data.model.District
import app.data.model.DistrictStation
import app.data.model.Station
import app.data.network.model.DistrictStationModel
import app.data.network.model.StationModel

/**
 * 针对集合
 */
internal fun List<DistrictStationModel>.asAppModel(): DistrictStation {
    val stations: MutableList<Station> = mutableListOf()
    map { model: DistrictStationModel ->
        model.list.map { stationModel ->
            stations.add(stationModel.asAppStationModel(model.name))
        }
    }
    val districts = map { model: DistrictStationModel ->
        model.asAppDistrictModel()
    }
    return DistrictStation(districts, stations)
}

// 针对单个 DistrictStationModel
//internal fun DistrictStationModel.asAppModel(): DistrictStation {
//    val districts = ArrayList<District>(list.size)
//    val stations = ArrayList<Station>(list.size)
//    list.forEach { networkStationModel ->
//        districts.add(asAppDistrictModel())
//        stations.add(networkStationModel.asAppStationModel(name))
//    }
//    return DistrictStation(districts, stations)
//}

internal fun DistrictStationModel.asAppDistrictModel() = District(name)

internal fun StationModel.asAppStationModel(
    districtName: String
): Station {
    return Station(
        districtName = districtName,
        stationId = stationId,
        stationName = stationName,
        isSelected = isSelect,
    )
}



