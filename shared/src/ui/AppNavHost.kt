package ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ui.city.cityScreen
import ui.city.openCityList
import ui.district.districtScreen
import ui.district.openDistrictList
import ui.favorite.favoriteScreen
import ui.favorite.favoritesRoute
import ui.favoritedetail.favoriteWeatherScreen
import ui.favoritedetail.openFavoriteWeather
import ui.more.moreScreen
import ui.province.openProvinceList
import ui.province.provinceScreen
import ui.settings.openSettings
import ui.settings.settingsScreen
import ui.station.openStationList
import ui.station.stationScreen
import ui.weather.weatherNavigation
import ui.weather.weatherScreen
import ui.web.openWeb
import ui.web.webScreen

/**
 * 传值和导航都在此处处理
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = weatherNavigation, // 告诉 NavBottomBar 进入应用后要打开的哪个item navigation route ,
        modifier = modifier
    ) {
        favoriteScreen(
            openProvinceScreen = { navController.openProvinceList() },
            openFavoriteWeatherScreen = { cityId, stationName ->
                navController.openFavoriteWeather(
                    cityId,
                    stationName
                )
            },
            nestedGraphs = {
                provinceScreen(
                    onBackClick = { navController.popBackStack() },
                    openCityScreen = { provinceName ->
                        navController.openCityList(provinceName)
                    }
                )

                cityScreen(
                    onBackClick = { navController.popBackStack() },
                    openFavoriteScreen = {
                        // 如果 inclusive 为 true: 则目标 TestScreen.Favorite.createRoute(root) 也清除出栈
                        navController.popBackStack(route = favoritesRoute, inclusive = false)
                    }
                )

                favoriteWeatherScreen(
                    onBackClick = { navController.popBackStack() },
                    openAirDetailsWebScreen = { cityId ->
                        navController.openWeb(
                            "https://szqxapp1.121.com.cn/sztq-app/v6/client/h5/aqi?cityid=$cityId"
                        )
                    }
                )
            }
        )

        weatherScreen(
            openAirDetails = { cityId ->
                navController.openWeb(
                    "https://szqxapp1.121.com.cn/sztq-app/v6/client/h5/aqi?cityid=$cityId"
                )
            },
            openDistrictList = { cityId, stationName ->
                navController.openDistrictList(
                    cityId,
                    stationName
                )
            },
            nestedGraphs = {
                districtScreen(
                    onBackClick = { navController.popBackStack() },
                    openStationList = { districtName ->
                        navController.openStationList(districtName)
                    }
                )
                stationScreen(
                    onBackClick = { navController.popBackStack() },
                    openWeatherScreen = {
                        navController.popBackStack(route = "weather_route", inclusive = false)
                    }
                )
            }
        )

        moreScreen(
            openWebLink = { url -> navController.openWeb(url) },
            openSettings = { navController.openSettings() },
            nestedGraphs = {
                webScreen(onBackClick = { navController.popBackStack() })
                settingsScreen(onBackClick = { navController.popBackStack() })
            }
        )
    }
}
