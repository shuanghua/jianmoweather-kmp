package di

import MainViewModel
import app.data.domain.domainModule
import libModule
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ui.city.CitiesViewModel
import ui.district.DistrictsViewModel
import ui.favorite.FavoritesViewModel
import ui.favoritedetail.FavoritesDetailViewModel
import ui.province.ProvincesViewModel
import ui.settings.SettingsViewModel
import ui.station.StationsViewModel
import ui.weather.WeatherViewModel

/**
 * 被多平台代码调用
 */
fun initKoin() {
    startKoin {
        modules(appModule)
    }
}

// shared common module
val appModule = module {
    includes(libModule, domainModule)
    viewModelOf(::WeatherViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::DistrictsViewModel)

    viewModelOf(::StationsViewModel)
    viewModelOf(::FavoritesViewModel)
    viewModelOf(::FavoritesDetailViewModel)
    viewModelOf(::ProvincesViewModel)
    viewModelOf(::CitiesViewModel)
}