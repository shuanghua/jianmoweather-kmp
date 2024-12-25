package app.data.domain.weather

import app.data.model.Weather
import app.data.repo.WeatherRepository
import dev.shuanghua.weather.shared.ObservableUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

class ObserverWeather(
    private val weatherRepo: WeatherRepository
) {
    operator fun invoke(): Flow<Weather> {
        return weatherRepo.observerWeather()
    }
}