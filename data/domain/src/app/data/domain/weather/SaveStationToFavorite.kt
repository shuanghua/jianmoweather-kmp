package app.data.domain.weather

import app.data.model.FavoriteStationParams
import app.data.repo.FavoritesRepository
import app.data.repo.LocationRepository
import app.data.repo.StationRepository
import io.github.aakira.napier.Napier

/**
 * 实际上保存的是该站点对应的请求参数: 站点名 + 对应城市ID
 * 方便在收藏页面直接使用该参数请求天气数据
 */
class SaveStationToFavorite(
	private val favoriteRepository: FavoritesRepository,
	private val stationRepository: StationRepository,
	private val locationRepository: LocationRepository,
) {
	suspend operator fun invoke(
		cityId: String,
		stationName: String,
	) {
		// 因为服务器接口现在不返回站点id了，所以只能通过站点名来获取站点id
		// 但如果用户没有打开过站点列表，也无法通过站点名来获取站点id
		// 所以改成存储请求参数
		Napier.d("SaveStationToFavoriteUseCase 保存站点: $cityId, $stationName")

		val location = locationRepository.getLocalLocationData() // datastore
		val stationId = stationRepository.getStationIdByName(stationName) // database station 表获取
		val stationWeatherParams = if (stationId == null) {
			FavoriteStationParams(
				isAutoLocation = "1",
				cityId = cityId,
				stationName = stationName,
				latitude = location.latitude,
				longitude = location.longitude,
				cityName = location.cityName,
				district = location.district
			)
		} else {
			FavoriteStationParams(
				isAutoLocation = "0",
				cityId = cityId,
				stationName = stationName,
				stationId = stationId
			)
		}

		favoriteRepository.saveFavoriteStation(stationWeatherParams)
	}
}