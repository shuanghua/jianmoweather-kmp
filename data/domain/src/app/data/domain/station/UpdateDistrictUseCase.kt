package app.data.domain.station

import AppDispatcher
import app.data.repo.LocationRepository
import app.data.repo.StationRepository
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.withContext

class UpdateDistrictUseCase(
	private val dispatchers: AppDispatcher,
	private val stationRepo: StationRepository,
	private val locationRepo: LocationRepository,
) : UpdateUseCase<UpdateDistrictUseCase.Params>() {

	data class Params(val cityId: String)

	override suspend fun doWork(params: Params): Unit = withContext(dispatchers.io) {
		val location = locationRepo.getLocalLocationData() // 从 datastore 或去经纬度信息
		stationRepo.updateStationWithDistrict(
			cityId = params.cityId,
			stationId = "",
			latitude = location.latitude,
			longitude = location.longitude,
		)
	}
}