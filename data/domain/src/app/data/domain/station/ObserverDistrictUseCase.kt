package app.data.domain.station

import app.data.model.District
import app.data.repo.StationRepository
import kotlinx.coroutines.flow.Flow

class ObserverDistrictUseCase(
    private val stationRepository: StationRepository,
) {
    operator fun invoke(): Flow<List<District>> {
        return stationRepository.observerDistricts()
    }
}