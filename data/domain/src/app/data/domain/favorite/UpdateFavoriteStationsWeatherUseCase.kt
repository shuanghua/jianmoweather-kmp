package app.data.domain.favorite

import app.data.model.FavoriteStationParams
import app.data.repo.FavoritesRepository
import dev.shuanghua.weather.shared.UpdateUseCase

class UpdateFavoriteStationsWeatherUseCase(
    private val favoriteRepository: FavoritesRepository,
) : UpdateUseCase<List<FavoriteStationParams>>() {

    /**
     *  params: List<FavoriteStationParams> 从首页添加的站点的请求参数
     */
    override suspend fun doWork(params: List<FavoriteStationParams>) {
        if (params.isEmpty()) {
            favoriteRepository.clearAllFavoriteStationsWeather() //删除数据库中的界面数据
        } else { // 从服务器获取所有 站点 天气数据
            favoriteRepository.updateFavoriteStationsWeather(params) // 访问网络
        }
    }
}