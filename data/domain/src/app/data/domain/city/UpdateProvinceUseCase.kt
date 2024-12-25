package app.data.domain.city

import AppDispatcher
import app.data.repo.ProvinceCityRepository
import dev.shuanghua.weather.shared.UpdateUseCase
import kotlinx.coroutines.withContext

class UpdateProvinceUseCase(
    private val provinceCityRepository: ProvinceCityRepository,
    private val dispatchers: AppDispatcher
) : UpdateUseCase<Unit>() {
    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            provinceCityRepository.updateAllProvinceWithCity()
        }
    }
}
