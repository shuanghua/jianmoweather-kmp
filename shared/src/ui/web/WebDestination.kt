package ui.web

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


internal val urlArg = "url"

fun NavController.openWeb(url: String) {
    this.navigate(route = "web_route/${encode(url)}")
}

fun NavGraphBuilder.webScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = "web_route/{$urlArg}",
        arguments = listOf(navArgument(urlArg) {
            type = NavType.StringType
            defaultValue = "https://github.com/shuanghua"
        })
    ) { backStackEntry ->  // 在这里直接取出值，是为了可以直接将值传递到 Screen 中使用
        backStackEntry.arguments?.getString(urlArg).let {
            WebRoute(
                webUrl = decode(it!!),
                onBackClick = onBackClick
            )
        }
    }
}


@OptIn(ExperimentalEncodingApi::class)
private fun encode(url: String): String = Base64.UrlSafe.encode(url.encodeToByteArray())

@OptIn(ExperimentalEncodingApi::class)
private fun decode(encodedUrl: String): String = Base64.UrlSafe.decode(encodedUrl).decodeToString()