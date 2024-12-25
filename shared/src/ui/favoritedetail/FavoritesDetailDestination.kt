package ui.favoritedetail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

internal const val cityIdArg: String = "cityId"
internal const val stationNameArg: String = "stationName"

/**
 * 打开
 */
fun NavController.openFavoriteWeather(
	cityId: String,
	stationName: String
) {
	this.navigate(route = "favorite_weather_route/$cityId/$stationName")
	// 导航会自动匹配路径，会自动找到下面函数定义对应的路径，然后自动打开对应的页面
}

/**
 * 页面地址 和 页面事件返回
 */
fun NavGraphBuilder.favoriteWeatherScreen(
	onBackClick: () -> Unit,
	openAirDetailsWebScreen: (String) -> Unit
) {
	composable(
		route = "favorite_weather_route/{$cityIdArg}/{$stationNameArg}",
		arguments = listOf(
			navArgument(cityIdArg) { type = NavType.StringType },
			navArgument(stationNameArg) { type = NavType.StringType }
		)
	) {
		FavoritesDetailRoute(
			onBackClick = onBackClick,
			openAirDetailsWebScreen = openAirDetailsWebScreen
		)
	}
}