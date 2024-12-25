package app.data.network.converter

import app.data.model.City
import app.data.model.Province
import app.data.model.ProvinceCity
import app.data.network.model.CityModel
import app.data.network.model.ProvinceCityModel
import app.data.network.model.ProvinceModel

internal fun ProvinceCityModel.asAppModel(): ProvinceCity {
    val (provinces: List<Province>, cities: List<City>) =
        allCityWithProvince.flatMap { networkProvinceModel ->
            networkProvinceModel.cityList.map { city ->
                networkProvinceModel.asAppProvinceModel() to city.asAppCityModel(networkProvinceModel.provName)
            }
        }.unzip()
    return ProvinceCity(provinces = provinces, cities = cities)
}

internal fun CityModel.asAppCityModel(provinceName: String = ""): City {
    return City(provinceName, cityId, cityName)
}

internal fun ProvinceModel.asAppProvinceModel(): Province {
    return Province(provName)
}