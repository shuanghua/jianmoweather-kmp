@file:OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)

package jianmoweather_kmp.`data`.compose_res.generated.resources

import kotlin.OptIn
import org.jetbrains.compose.resources.StringResource

private object CommonMainString0 {
  public val app_name: StringResource by 
      lazy { init_app_name() }

  public val favorite: StringResource by 
      lazy { init_favorite() }

  public val more: StringResource by 
      lazy { init_more() }

  public val weather: StringResource by 
      lazy { init_weather() }
}

internal val Res.string.app_name: StringResource
  get() = CommonMainString0.app_name

private fun init_app_name(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:app_name", "app_name",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/jianmoweather_kmp.data.compose_res.generated.resources/values/strings.commonMain.cvr",
    10, 32),
    )
)

internal val Res.string.favorite: StringResource
  get() = CommonMainString0.favorite

private fun init_favorite(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:favorite", "favorite",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/jianmoweather_kmp.data.compose_res.generated.resources/values/strings.commonMain.cvr",
    43, 24),
    )
)

internal val Res.string.more: StringResource
  get() = CommonMainString0.more

private fun init_more(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:more", "more",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/jianmoweather_kmp.data.compose_res.generated.resources/values/strings.commonMain.cvr",
    68, 20),
    )
)

internal val Res.string.weather: StringResource
  get() = CommonMainString0.weather

private fun init_weather(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:weather", "weather",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/jianmoweather_kmp.data.compose_res.generated.resources/values/strings.commonMain.cvr",
    89, 23),
    )
)
