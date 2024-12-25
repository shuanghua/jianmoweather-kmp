package app.data.domain.weather

import AppDispatcher
import app.data.repo.WeatherRepository
import dev.shuanghua.weather.shared.UpdateUseCase

/**
 * 从站点页面选择站点后，更新天气信息
 * 用户手动选择了其他站点，这就不需要定位相关的数据了
 * 因为定位方案，服务器自己会根据用户的位置返回最适合的站点
 */
class UpdateStationWeather(
    private val weatherRepository: WeatherRepository, // 获取天气
) : UpdateUseCase<UpdateStationWeather.Params>() {

    data class Params(val cityId: String, val stationId: String)

    override suspend fun doWork(params: Params) {
        weatherRepository.updateCityOrStationWeather(
            params.cityId, params.stationId
        )
    }
}