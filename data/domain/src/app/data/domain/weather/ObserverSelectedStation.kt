package app.data.domain.weather

import app.data.model.SelectedStation
import app.data.repo.StationRepository
import kotlinx.coroutines.flow.Flow

class ObserverSelectedStation(
    private val stationRepo: StationRepository
) {
    operator fun invoke(): Flow<SelectedStation?> {
        return stationRepo.observerSelectedStation()
    }
}