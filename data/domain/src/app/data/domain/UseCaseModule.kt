package app.data.domain

import app.data.domain.city.ObserverProvinceUseCase
import app.data.domain.city.UpdateProvinceUseCase
import app.data.domain.favorite.GetFavoriteDetailWeatherUseCase
import app.data.domain.favorite.UpdateFavoriteCitiesWeatherUseCase
import app.data.domain.favorite.UpdateFavoriteStationsWeatherUseCase
import app.data.domain.station.ObserverDistrictUseCase
import app.data.domain.station.UpdateDistrictUseCase
import app.data.domain.weather.*
import app.data.repo.di.repoModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    includes(repoModule) // 为 domain 模块添加 repository 模块
    singleOf(::UpdateLocationCityWeather)
    singleOf(::UpdateStationWeather)

    singleOf(::UpdateFavoriteStationsWeatherUseCase)
    singleOf(::UpdateFavoriteCitiesWeatherUseCase)
    singleOf(::GetFavoriteDetailWeatherUseCase)
    singleOf(::UpdateFavoriteStationsWeatherUseCase)
    singleOf(::UpdateFavoriteCitiesWeatherUseCase)
    singleOf(::SaveStationToFavorite)

    singleOf(::UpdateProvinceUseCase)
    singleOf(::ObserverProvinceUseCase)

    singleOf(::UpdateDistrictUseCase)
    singleOf(::ObserverDistrictUseCase)
    singleOf(::ObserverSelectedStation)
    singleOf(::ObserverWeather)
}