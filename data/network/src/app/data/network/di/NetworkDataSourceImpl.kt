package app.data.network.di

import app.data.model.*
import app.data.network.NetworkDataSource
import app.data.network.api.Api
import app.data.network.converter.asAppModel
import app.data.network.converter.asFavoriteStationWeather
import app.data.network.model.*
import exception.ModuleException
import exceptionCall
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*


internal class NetworkDataSourceImpl(
    private val ktorClient: HttpClient,
) : NetworkDataSource, ModuleException {

    /**
     * 首页天气 + 收藏页-站点天气
     */
    override suspend fun getWeatherByLocation(
        latitude: String,
        longitude: String,
        cityName: String,
        district: String,
    ): Weather = catchCall {
        val response = ktorClient.post("sztq-app/v6/client/index") {
            url {
                parameters.append("lat", latitude)
                parameters.append("lon", longitude)
                parameters.append("pcity", cityName)
                parameters.append("parea", district)
                parameters.append("rainm", Api.RAINM)
                parameters.append("uid", Api.UID)
            }
        }
        val networkData = response.body<CommonResult<NetworkWeatherModel>>()
        networkData.result.asAppModel()
    }


    override suspend fun getWeatherByStationId(
        cityId: String,
        stationId: String,
    ): Weather = catchCall {
        val response = ktorClient.get("sztq-app/v6/client/index") {
            url {
                parameters.append("cityid", cityId)
                parameters.append("obtid", stationId)
                parameters.append("rainm", Api.RAINM)
                parameters.append("uid", Api.UID)
            }
        }
        val networkData = response.body<CommonResult<NetworkWeatherModel>>()
        networkData.result.asAppModel()
    }


    override suspend fun getWeatherByCityId(
        cityId: String,
    ): Weather = catchCall {
        val response = ktorClient.post("sztq-app/v6/client/index") {
            url {
                parameters.append("cityid", cityId)
                parameters.append("rainm", Api.RAINM)
                parameters.append("uid", Api.UID)
            }
        }
        val networkData = response.body<CommonResult<NetworkWeatherModel>>()
        networkData.result.asAppModel()
    }

    /**
     * 观测区县 + 每个区下对应的站点列表
     * 服务器上，非广东城市的站点列表数据为 null
     */
    override suspend fun getDistrictsWithStations(
        cityId: String,
        obtId: String,
        latitude: String,
        longitude: String,
    ): DistrictStation = catchCall {
        val data = ktorClient.post("sztq-app/v6/client/autoStationList") {
            url {
                parameters.append("cityid", cityId)
                parameters.append("obtid", "")
                parameters.append("lat", latitude.ifEmpty { "0" })
                parameters.append("lon", longitude.ifEmpty { "0" })
                parameters.append("uid", Api.UID)
            }
        }
            .body<CommonResult<List<DistrictStationModel>>>()
            .result
        data.asAppModel()
    }

    /**
     * 收藏-城市天气
     * 收藏页面有两个请求接口
     * 站点天气请求和首页是公用的  getMainWeather(params: WeatherParams)
     */
    override suspend fun getFavoriteCityWeather(
        latitude: String,
        longitude: String,
        cityIds: String,
    ): List<FavoriteCityWeather> = catchCall {
        ktorClient.post("sztq-app/v6/client/city/alreadyAddCityList") {
            url {
                parameters.append("lon", latitude)
                parameters.append("lat", longitude)
                parameters.append("cityIds", cityIds)
                parameters.append("uid", Api.UID)
            }
        }
            .body<CommonResult<FavoriteCityWeatherList>>().result.list
            .map(NetworkFavoriteCityWeather::asAppModel)
    }

    override suspend fun getFavoriteStationWeatherByStationId(
        cityId: String,
        stationId: String
    ): FavoriteStationWeather = catchCall {
        getWeatherByStationId(
            cityId,
            stationId
        ).asFavoriteStationWeather()// Weather 转 FavoriteStationWeather
    }

    override suspend fun getFavoriteStationWeatherByLocation(
        latitude: String,
        longitude: String,
        cityName: String,
        district: String
    ): FavoriteStationWeather = catchCall {
        getWeatherByLocation(
            latitude,
            longitude,
            cityName,
            district
        ).asFavoriteStationWeather()// Weather 转 FavoriteStationWeather
    }

    override suspend fun getProvincesWithCities(
        uid: String,
    ): ProvinceCity = catchCall {
        val response = ktorClient.post("sztq-app/v6/client/city/list") {
            url { parameters.append("uid", uid) }
        }
        val networkModel = response.body<CommonResult<ProvinceCityModel>>().result
        networkModel.asAppModel()
    }

    override suspend fun <T> catchCall(block: suspend () -> T): T =
        exceptionCall(tag = "错误来自网络模块", block = block)
}





